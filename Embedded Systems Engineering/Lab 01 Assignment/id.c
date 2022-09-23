#include <ctype.h>
#include <stdio.h>
main(){
char buf[200];
FILE *fp;
fp=fopen("test.txt","r");

char id[100];
int j;
while (fgets(buf,100,fp)){
  int i=0;
  int len =strlen(buf);
  for (j=0,i=0;i<len;i++){
      if (isalpha(buf[i]) || buf[i]=='_'){
        while (buf[i] && (isalnum(buf[i]) || buf[i]=='_'))
           id[j++] = buf[i++];
        // have id
        id[j]=0;
        printf("id = %s\n",id);
        j=0;
        }
      
      }


   }
}

     
   
     
