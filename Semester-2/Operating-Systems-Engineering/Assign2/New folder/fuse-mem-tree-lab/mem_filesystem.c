#include <fuse.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


struct Dir_ {
struct Dir_ * parent;
struct List * dir;
char * name;
};

struct Fil {
char * name;
int size;
char * data;
};
union ListItem{
struct Fil * file;
struct Dir_ * root;
};
struct List{
int type;
union ListItem li;
struct List * next;
};



struct Dir_* head;
struct Dir_ * current;

struct Dir_* newDir_(char * name,struct Dir_ * parent){
struct Dir_ * n = malloc(sizeof(struct Dir_));
n->parent = parent;
n->dir = NULL;
n->name = (char *)malloc(strlen(name)+1);
strcpy(n->name,name);
return n;
}

void listdir(){
printf("listdir\n");
for (struct List*l=current->dir;l;l=l->next)
    if (l->type ==0)
       if (l->li.file)
          printf("file-- %s\n",l->li.file->name);
}

void copyit(char *src,char*dest){
   printf("copyit src=%s= dest=%s=\n",src,dest);
   struct List *l = malloc(sizeof(struct List));
   struct Fil * f = malloc(sizeof(struct Fil));

   FILE *fp;
   f->name = malloc(strlen(dest)+1);
   strcpy(f->name,dest);
   
   fp = fopen(src,"r");
   if (!fp){
      printf("cant open %s\n",src);
      return;
      }
   fseek(fp,0l,SEEK_END);
   long len = ftell(fp);
   printf("len = %ld\n",len);
   fseek(fp,0l,SEEK_SET);
   f->data = malloc((int)len);
    f->size=(int)len;
   for (int i=0;i<(int)len;i++)
      f->data[i] = fgetc(fp);
  
   fclose(fp);
   l->li.file = f;
   l->type =0;
   l->next = current->dir;
   current->dir=l;
}

void catit(char * file){
printf("catit\n");
for (struct List*l=current->dir;l;l=l->next)
    if (l->type ==0 && l->li.file && !strcmp(file,l->li.file->name)){
       struct Fil *f = l->li.file;
        printf("found-- %s %d\n",f->name,f->size);
       for (int i=0;i<f->size;i++)
       printf("%c",f->data[i]);
       }
}
// walk down the directory tree to find the file in path.
// return:
// 1) the containing directory Dir_ 
// 2) the actual file name.
// e.g. if path == /dir1/dir2/asdf.h
// file name will be asdf.h
// return a point to the dir2 data structure
struct Dir_ * walk(struct Dir_* n, char * path,char **filename){

   path++;
   while(1){
   if (!strchr(path,'/')){
      printf("walk end %s\n",path);
      *filename = path;
      return n;
      }
   else{
      char * p = strchr(path,'/');
      if (p){
         printf("walk1 %s\n",path);
         for (struct List *l = n->dir;l;l=l->next){
            *p =0;
          /*  if (l->type ==1)
               printf("walk2 path=%s %s===\n",path,l->li.root->name);
            else
               printf("walk2 path=%s %s===\n",path,l->li.file->name);
           */
            if (l->type == 1 && l->li.root && !strcmp(path,l->li.root->name)){
              
               printf("walking remaining path = %s dir =%s\n",p+1,l->li.root->name);
               path = p+1;
               n = l->li.root;
            }
             *p='/';
            }
         }
         else
            return n;
     } 
     }
}

// gets full path to the file could be a dir or an ordinary file
// return 1 for file and its size
// return 2 for directory
int statit(char *name,int *psize){
printf("statit %s\n",name);
    *psize=0;
    char * file=NULL;
    struct Dir_ * n = walk(head,name,&file);
    printf("after walk %s %s\n",n->name,file);
    if (!file){
         printf("walk did not find it %s\n",name);
         return 0;
         }
   // we have the directory data structure and the filename extracted from the path
   for (struct List*l=n->dir;l;l=l->next)
      if (l->type ==0 && l->li.file && !strcmp(file,l->li.file->name)){
         struct Fil *f = l->li.file;
         printf("found-- %s %d\n",f->name,f->size);
         *psize=f->size;
        return 1;
        }
     else
        if (l->type ==1 && l->li.root && !strcmp(file,l->li.root->name)){
           struct Dir_ *n = l->li.root;
           printf("found dir-- %s \n",n->name);
           return 2;
        }
}

// read some contents from the file
// gets full path to the file could be a dir or an ordinary file
// if it is file then fill buffer starting at offset in the file
int do_read( char *path, char *buffer, size_t size, off_t offset, struct fuse_file_info *fi){
int i=0;
  printf( "--> Trying to read %s, %d, %d\n", path, (int)offset, (int)size );
  char * file=NULL;
  struct Dir_* d = walk(head,path,&file);
  printf("after walk %s %s\n",d->name,file);
  
  /* TODO 
  find the file in the list given by d->dir 
  copy the contents of the file to the buffer.
  return the amount copied.
  */
  char buf[400];
  sprintf(buf,"path=%s=file=%s= hello world\n",path,file);
  int sz = strlen(buf);
  // put in some dummy stuff into the file
  for (i=0;i<size && i<sz;i++){
     buffer[i] = buf[i];
    }
   struct List *cur = d->dir;
  while (cur != NULL) {
    if (cur->type == 0 && strcmp(cur->li.file->name, file) == 0) {
      int to_copy = cur->li.file->size - offset;
      if (to_copy > size) {
        to_copy = size;
      }
      printf("****read found %s, size %d\n", cur->li.file->name, to_copy);
      memcpy(buffer, cur->li.file->data + offset, to_copy);
      
      return to_copy;
    }
    cur = cur->next;
  }
  return i;
}

// return the contents of the directory
// use the function filler to add to the list of files in the directory
int do_readdir( char *path, void *buffer, fuse_fill_dir_t filler, off_t offset, struct fuse_file_info *fi ){
printf( "--> Getting The List of Files of %s\n", path );
	
   filler( buffer, ".", NULL, 0 ); // Current Directory
   filler( buffer, "..", NULL, 0 ); // Parent Directory
   char * file=NULL;
   struct Dir_* d = walk(head,path,&file);
   printf("after walk %s %s\n",d->name,file);
   
   // input could be dir1/dir2
   // d points to the dir1 data structure
   // file is dir2
   // need to look for the dir2 data structure
   if (*file){
   for (struct List*l=d->dir;l;l=l->next){
      if (l->type ==1 && l->li.root&& !strcmp(file,l->li.root->name)){
         d = l->li.root;
         printf("****readdir found dir-- %s\n",d->name);
         }
      }
  }
  
  for (struct List*l=d->dir;l;l=l->next){
    if (l->type ==0 && l->li.file ){
       struct Fil *f = l->li.file;
        printf("read dir found-- %s %d\n",f->name,f->size);
        filler( buffer, f->name, NULL, 0 );
       }
    else
    if (l->type ==1 && l->li.root ){
       struct Dir_ *n = l->li.root;
        printf("readdir found dir-- %s\n",n->name);
        filler( buffer, n->name, NULL, 0 );
       }
       }
   return 0;
	
}
struct Dir_ *mkDir_(char *dir){
   printf("mkdir\n");
   struct Dir_ * n;
   struct List *l = malloc(sizeof(struct List));
   l->li.root =n =  newDir_(dir,current);
   l->type =1;
   l->next = current->dir;
   current->dir=l;
   return n;
}

// setup some directories and some files
/*  
   /ss
   /test1
   /dir1
   /dir1/t1
   /dir1/ss1.c
   /dir1/dir2
   /dir1/dir2/t11
   /dir1/dir2/ss11.c
*/
void init(){
   head =newDir_("/",NULL);
   current = head;
   copyit("ssfs.c","ss");
   copyit("test1","test1");
   struct Dir_ * n = mkDir_("dir1");
   current =n;
   copyit("test1","t1");
   copyit("ssfs.c","ss1.c");
   n = mkDir_("dir2");
   current =n;
   copyit("test1","t11");
   copyit("ssfs.c","ss11.c");
}

int main1(){
char line[200];
init();
while(1){
fgets(line, 200, stdin);
line[strlen(line)] =0;     // remove the CR

//printf("line =%s=\n",line);
if (!strncmp(line,"ls",2)){
    listdir();
}
else
   if (!strncmp(line,"copy",4)){
       char * src = line+5;
       char * dest;
       dest = strrchr(line,' ');
       *dest=0;
       if (dest){
           dest++;
           if (dest-line < strlen(line)){
              printf("error in command\n");
              continue; 
              }
           copyit(src,dest);
           }   
      }  
   else
     if (!strncmp(line,"cat",3)){
        char * src = line+4;
        catit(src);  
       } 
   else
     if (!strncmp(line,"mkdir",5)){
        char * src = line+6;
        mkDir_(src);  
       }  
   

}
return 0;
}
