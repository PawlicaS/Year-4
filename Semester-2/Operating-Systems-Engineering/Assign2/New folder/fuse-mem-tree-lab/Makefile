COMPILER = gcc
FILESYSTEM_FILES = ssfs.c mem_filesystem.c

build: $(FILESYSTEM_FILES)
	$(COMPILER) $(FILESYSTEM_FILES) -o memfs `pkg-config fuse --cflags --libs`
	echo 'To Mount: ./memfs -f [mount point]'

clean:
	rm memfs
