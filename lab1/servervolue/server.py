import socket
import string
import random
import hashlib
import os

def file_writer():
    file = open('/serverdata/text.txt', 'w')
    file.write(file_generator())
    file.flush()
    file.close()

def file_reader(filepath):
    text=""
    with open(filepath, "r") as f:
            piece = f.read()
            text = text+piece
    return text

def get_checksum(filepath):
    text = file_reader(filepath)
    return hashlib.md5(text.encode("utf_8")).hexdigest()

def file_generator():
    word = ""
    for i in range(1024):
        word = word + random.choice(string.hexdigits)
    return word

def main():
    sock = socket.socket()
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    port = int(os.environ.get('port'))
    sock.bind(('', port))
    sock.listen(1)
    conn, addr = sock.accept()   
    file_writer()   
    while True:    
        data = conn.recv(1024).decode("utf_8")
        if not data:
            break
        
        if data == "get_text":
            conn.send((file_reader("/serverdata/text.txt")+"\n").encode("utf_8"))
        elif data == "get_checksum":
            conn.send((get_checksum("/serverdata/text.txt")+"\n").encode("utf_8"))      
        else:
            break       
    conn.close()

main()
