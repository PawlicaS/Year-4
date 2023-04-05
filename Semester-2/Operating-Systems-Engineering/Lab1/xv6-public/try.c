#include "types.h"
#include "stat.h"
#include "user.h"

int
main(int argc, char *argv[])
{
  trace(1);
  int fd = open("README", 0);
  close(fd);
  printf(1, "Hello World\n");
  
  trace(0);
  int fd1 = open("README", 0);
  close(fd1);
  exit();
}
