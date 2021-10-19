package com.company;
import java.net.*;
import java.io.*;


public class Main
{
    private static Socket clientSocket; //сокет для общения
    //private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    //private static BufferedWriter out; // поток записи в сокет
    public static void main(String[] args)
    {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                //reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                //out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                try(FileWriter writer = new FileWriter("./clientdata/text.txt", false))
                {
                    String serverWord = in.readLine(); // ждём, что скажет сервер
                    System.out.println(serverWord);
                    writer.write(serverWord);
                    writer.flush();
                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }

            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                //out.close();

            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }

    }
}

