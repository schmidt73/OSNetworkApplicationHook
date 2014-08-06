#include <stdio.h>
#include <unistd.h>
#include <dlfcn.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <pwd.h>
#include <netdb.h>
#include <time.h>
#include <pthread.h>
#include <errno.h>

/*
 
 COMPILE WITH "gcc -flat_namespace -dynamiclib -m32 -o Hook.dylib /Path/To/Hook.C"
 MAKE SURE IT IS x86 AND HAS A FLAT NAMESPACE
 
 */

//This is the port we will send the data to. This is to communicate with the process that will parse the information.
#define PORT "42333"

//Structure to hold data to pass for write_data_threaded call. Capable of being threaded for future use.
struct write_data{
    const void *buf;
    size_t nbytes;
};

//Connects to localhost on PORT and writes data. Capable of being threaded for future use.
void* write_data_threaded(void* data);

//Function to connect to server.
int connect_server_tcp(int* fd, char* host, char* port);

/*############################################################
 #                                                            #
 #                 Start of Hooking Functions                 #
 #                                                            #
 ############################################################*/

//Hooked write function.
ssize_t write(int fd, const void *buf, size_t nbytes);

//Original write function that we will call in our hooked write function.
static ssize_t (*original_write)(int, const void *, size_t) = NULL;

/*############################################################
 #                                                            #
 #                 End of Hooking Functions                   #
 #                                                            #
 ############################################################*/

ssize_t write(int fd, const void *buf, size_t nbytes){
    if(!original_write) original_write = dlsym(RTLD_NEXT, "write");
    
    struct write_data data;
    data.buf = buf;
    data.nbytes = nbytes;
    
    write_data_threaded(&data);
    
    return original_write(fd, buf, nbytes);
}

void* write_data_threaded(void* data){
    if(!original_write) original_write = dlsym(RTLD_NEXT, "write");
    
    int sendfd = 0;
    connect_server_tcp(&sendfd, "127.0.0.1", PORT);
    
    original_write(sendfd, ((struct write_data *) data)->buf, ((struct write_data *) data)->nbytes);
    close(sendfd);

    return NULL;
}

int connect_server_tcp(int* fd, char* host, char* port){
    int status;
    struct addrinfo hints;
    struct addrinfo *p, *servinfo;
    
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE;
    
    if ((status = getaddrinfo(host, port, &hints, &servinfo)) != 0) {
        return -1;
    }
    
    for(p = servinfo; p != NULL; p = p->ai_next){
        status = *fd = socket(p->ai_family, p->ai_socktype, p->ai_protocol);
        if(status == -1) continue;
        status = connect(*fd, p->ai_addr, p->ai_addrlen);
        if(status == -1) shutdown(*fd, SHUT_RDWR);
        break;
    }
    
    freeaddrinfo(servinfo);
    if(status == -1) return -1;
    return 0;
}