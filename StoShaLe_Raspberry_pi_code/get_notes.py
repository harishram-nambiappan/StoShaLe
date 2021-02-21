import socket
import os

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(("192.168.43.100",2800))
while True:
    sock.listen()
    conn, addr = sock.accept()
    path_string = conn.recv(1024)
    path = path_string.decode()
    if len(path.split("/")) == 3:
        file_list = os.listdir(path)
        if(len(file_list)==0):
            files = "None"
        else:
            print(file_list)
            files = file_list[0]
            for i in range(1,len(file_list)):
                print(file_list[i])
                files = files + "/" + file_list[i]
        conn.send(bytes(files,'utf-8'))
    elif len(path.split("/")) == 4:
        read_file = open(path, 'r')
        result = read_file.readlines()[0]
        conn.send(bytes(result, 'utf-8'))
    conn.close()