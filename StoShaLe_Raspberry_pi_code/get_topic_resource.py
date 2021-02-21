import socket
import os

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("192.168.43.100", 2600))
while True:
    server.listen()
    conn, addr = server.accept()
    req_string = conn.recv(1024)
    print(req_string)
    res = ""
    str_split = req_string.decode().split("/")
    if str_split[0] == "Get_Topic":
        if not os.path.exists(str_split[1]):
            res = "None"
        else:
            topics = os.listdir(str_split[1])
            res = topics[0]
            for i in range(1, len(topics)):
                res = res+"/"+topics[i]
        conn.send(bytes(res, 'utf-8'))
    if str_split[0] == "Get_Videos":
        path_list = str_split[1].split("_")
        videos = os.listdir(path_list[0]+"/"+path_list[1])
        if(len(videos)==0):
            res = "None"
        else:
            res = videos[0]
            for i in range(1, len(videos)):
                res = res + "/" + videos[i]
        conn.send(bytes(res, 'utf-8'))
    conn.close()