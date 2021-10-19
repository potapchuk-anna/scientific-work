import socket
import string
import random

def file_writer():
    file = open('./serverdata/text.txt', 'w')
    file.write(file_generator())
    file.flush()
    file.close()


def file_generator():
    word = ""
    for i in range(1025):
        word = word + random.choice(string.hexdigits)
    return word

def main():
    sock = socket.socket()
    sock.bind(('localhost', 4004))
    sock.listen(1)
    conn, addr = sock.accept()
    file_writer()
    with open("./serverdata/text.txt", "rb") as f:
        piece = f.read()       
        conn.send(piece)
    conn.close()

main()