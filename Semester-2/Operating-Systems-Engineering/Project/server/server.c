/*
    C socket server example, handles multiple clients using threads
*/
 
#include<stdio.h>
#include<string.h>    //strlen
#include <sys/ioctl.h>
#include<stdlib.h>    //strlen
#include<sys/socket.h>
#include<arpa/inet.h> //inet_addr
#include<unistd.h>    //write
#include<pthread.h> //for threading , link with lpthread
#include <errno.h>
#include "server.h"

#define min(a, b) ((a) < (b) ? (a) : (b))


size_t rio_writen(int fd, const char *usrbuf, size_t n) 
{
    size_t nleft = n;
    ssize_t nwritten;
    const char *bufp = usrbuf;

    while (nleft > 0) {
        if ((nwritten = write(fd, bufp, nleft)) <= 0) {
            return 0;
        }
        nleft -= nwritten;
        bufp += nwritten;
    }

    return n;
}
int parse_request(const char *req_str, request_t *req_info) {
    if (sscanf(req_str, "%s %s %[^\r\n]", req_info->method, req_info->uri, req_info->version) != 3) {
        fprintf(stderr, "malformed http request\n");
        return -1;
    }
    printf("method %s uri %s\n",req_info->method, req_info->uri);
}



void send_response(int connfd, status_t status, 
                   const char *content, size_t content_length) {
    char buf[128];

    if (status == NF) {
        sprintf(buf, "HTTP/1.0 404 Not Found\r\n");
    } else if (status == OK) {
        sprintf(buf, "HTTP/1.0 200 OK\r\n");
    } else {
        sprintf(buf, "HTTP/1.0 500 Internal Servere Error\r\n");
    }

    sprintf(buf, "%sContent-Length: %lu\r\n\r\n", buf, content_length);

    size_t buf_len = strlen(buf);

    if (rio_writen(connfd, buf, buf_len) < buf_len) {
        fprintf(stderr, "error while sending response\n");
        return;
    }
    if (rio_writen(connfd, content, content_length) < content_length) {
        fprintf(stderr, "error while sending response\n");
    }

    printf("%s%s\n", buf, content);
}


//the thread function
void *connection_handler(void *socket_desc);

int main(int argc , char *argv[])
{
    int socket_desc , client_sock , c , *new_sock;
    struct sockaddr_in server , client;
    pthread_t thread_id;
     
    //Create socket
    socket_desc = socket(AF_INET , SOCK_STREAM , 0);
    if (socket_desc == -1)
    {
        printf("Could not create socket");
    }
    puts("Socket created");
     
    //Prepare the sockaddr_in structure
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons( 3000 );
     
    //Bind
    if( bind(socket_desc,(struct sockaddr *)&server , sizeof(server)) < 0)
    {
        //print the error message
        perror("bind failed. Error");
        return 1;
    }
    puts("bind done");
     
    //Listen
    listen(socket_desc , 3);
     
    //Accept incoming connection
    puts("Waiting for incoming connections...");
    c = sizeof(struct sockaddr_in);

    while(1 )
    {
        //Accept and incoming connection
        client_sock = accept(socket_desc, (struct sockaddr *)&client, (socklen_t*)&c);
        if (client_sock < 0)
        {
            perror("accept failed");
            return 1;
        }
        puts("Connection accepted");
         
        //Create a new thread to handle the incoming connection
        new_sock = malloc(1);
        *new_sock = client_sock;
         
        if( pthread_create( &thread_id, NULL, connection_handler, (void*) new_sock) < 0)
        {
            perror("could not create thread");
            return 1;
        }

        // Detach the thread to avoid memory leak
        pthread_detach(thread_id);

        puts("Handler assigned");
    }
     
    return 0;
}

void *connection_handler(void *socket_desc)
{
    //Get the socket descriptor
    int sock = *(int*)socket_desc;
    int sz;
    char *message , data[2000];
     
    //Receive a message from client
    while( (sz = recv(sock , data , 2000 , 0)) > 0 )
    {
        request_t req_info;
        char *response = NULL;
        size_t response_length = 0;
        
        // Parse HTTP request
        if (parse_request(data, &req_info) < 0) {
            send_response(sock, NF, NULL, 0);
            continue;
        }
        // Handle HTTP GET request
        printf("Client Message %s \n", data);
        if (strcmp(req_info.method, "GET") == 0) {
            // Process GET request and set response
            char *response = "Hello World!";
            size_t response_length = strlen(response);
            send_response(sock, OK, response, response_length);
        } else {
            // Return 404 Not Found for other request methods
            send_response(sock, NF, NULL, 0);
            continue;
        }

        // Try to open the requested file
        FILE *fp = fopen("test", "rb");
        if (fp == NULL) {
            send_response(sock, NF, NULL, 0);
            continue;
        }

        // Get the file size
        fseek(fp, 0, SEEK_END);
        long file_size = ftell(fp);
        fseek(fp, 0, SEEK_SET);

        // Read the file content into a buffer
        char *file_content = malloc(file_size);
        fread(file_content, 1, file_size, fp);
        fclose(fp);

        // Send the file content as the response
        send_response(sock, OK, file_content, file_size);
        free(file_content);
    }
     
    if(sz == 0)
    {
        puts("Client disconnected");
        fflush(stdout);
    }
    else if(sz == -1)
    {
        perror("recv failed");
    }
         
    //Free the socket pointer
    free(socket_desc);
     
    return 0;
}
