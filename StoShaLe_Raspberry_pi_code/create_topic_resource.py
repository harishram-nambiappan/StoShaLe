import os
import sys
import socket

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("192.168.43.100",2500))
while True:
    server.listen()
    conn, addr = server.accept()
    req_string = conn.recv(1024)
    print(req_string)
    str_split = req_string.decode().split("/")
    if str_split[0] == "Create_Topic":
        path = str_split[1]
        path_split = path.split("_")
        if not os.path.exists(path_split[0]):
            os.mkdir(path_split[0])
        os.mkdir(path_split[0]+"/"+path_split[1])
    elif str_split[0] == "Create_Resource":
        main_path = str_split[1].split("_")
        if not os.path.exists(main_path[0]):
            os.mkdir(main_path[0])
        if not os.path.exists(main_path[0]+"/"+main_path[1]):
            os.mkdir(main_path[0]+"/"+main_path[1])
        os.mkdir(main_path[0]+"/"+main_path[1]+"/"+str_split[2])
    conn.close()
'''
str_split = req_string.split("_")

print(len(str_split))
 
if not os.path.exists(str_split[0]):
    os.makedir(str_split[0])
os.makedir(str_split[0]+"/"+str_split[1])

'''