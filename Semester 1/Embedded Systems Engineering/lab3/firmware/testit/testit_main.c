// See LICENSE for license details.

//**************************************************************************
// Median filter bencmark
//--------------------------------------------------------------------------
//
// This benchmark performs a 1D three element median filter. The
// input data (and reference data) should be generated using the
// median_gendata.pl perl script and dumped to a file named
// dataset1.h.


__sync_fetch_and_add_4(){
}
malloc(){}
abort(){}
void quicksort(int *number,int first,int last){
   int i, j, pivot, temp;
   if(first<last){
      pivot=first;
      i=first;
      j=last;
      while(i<j){
         while(number[i]<=number[pivot]&&i<last)
         i++;
         while(number[j]>number[pivot])
         j--;
         if(i<j){
            temp=number[i];
            number[i]=number[j];
            number[j]=temp;
         }
      }
      temp=number[pivot];
      number[pivot]=number[j];
      number[j]=temp;
      quicksort(number,first,j-1);
      quicksort(number,j+1,last);
   }
}
int main( int argc, char* argv[] )
{
  int  p1[10];
  int arr[25];
  int i=0;
  volatile int * out = (int *)0x20000008;
 
  volatile int * data = (int *)0x20000000;
  volatile int * strobe = (int*)0x20000004;
  volatile int * datadone = (int*)0x20000010;
  
  volatile int * tdata = (int *)0x10000000;
  volatile int * tstrobe = (int*)0x10000004;
  volatile int * tdatadone = (int*)0x10000010;
  
 // *out = 9;
  //int y = *data;
  //*out = y+10;
  p1[0] ='h';
  p1[1] = 'e';
  p1[2] = 'l';
  p1[3] = 'l';
  p1[4] = 'o';
 
  while (*strobe){
      int ch = *data;
      *out = ch;
      arr[i++]=ch;
      *datadone = 1;
      } 
 quicksort(arr,0,i-1);
 for (int ii=0;ii<i;ii++)
     *tdata = arr[ii];
      /* *tdata = p1[0];//'h';
       *tdata = p1[1];//'e';
       *tdata = p1[2];//'l' ; 
       *tdata = p1[3];
       *tdata = p1[4];
 */
           
  int *p = (int *)0x30000000;
  *p = 56789;
}
