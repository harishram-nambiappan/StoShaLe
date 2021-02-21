import socket
import os

sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
sock.bind(("192.168.43.100",2700))
while True:
    sock.listen()
    conn, addr = sock.accept()
    path_string = conn.recv(1024)
    content = path_string.decode().split("/")[4]
    path = path_string.decode().split("/")[0:4]
    save_file = open(path[0]+"/"+path[1]+"/"+path[2]+"/"+path[3],"w")
    save_file.write(content)
    save_file.close()
    print(content)
    conn.close()