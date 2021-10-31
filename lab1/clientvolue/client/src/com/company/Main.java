package com.company;
import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;





public class Main
{
    private static Socket clientSocket; //сокет для общения
    //private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String checksum(MessageDigest digest,
                                   File file)
            throws IOException
    {
        // Get file input stream for reading the file
        // content
        FileInputStream fis = new FileInputStream(file);

        // Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        // read the data from file and update that data in
        // the message digest
        while ((bytesCount = fis.read(byteArray)) != -1)
        {
            digest.update(byteArray, 0, bytesCount);
        };

        // close the input stream
        fis.close();

        // store the bytes returned by the digest() method
        byte[] bytes = digest.digest();

        // this array of bytes has bytes in decimal format
        // so we need to convert it into hexadecimal format

        // for this we create an object of StringBuilder
        // since it allows us to update the string i.e. its
        // mutable
        StringBuilder sb = new StringBuilder();

        // loop through the bytes array
        for (int i = 0; i < bytes.length; i++) {

            // the following line converts the decimal into
            // hexadecimal format and appends that to the
            // StringBuilder object
            sb.append(Integer
                    .toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        // finally we return the complete hash
        return sb.toString();
    }

    public static void main(String[] args)
    {
        try {
            try {
                int port=0;

               try
               {
                   port = Integer.parseInt(System.getenv("port"));
               }
                catch (NumberFormatException exception)
                {
                    System.out.println("Cannot find port.");
                    System.exit(-1);
                }

                clientSocket = new Socket(System.getenv("host"), port); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                //reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write("get_text");
                out.flush();
                try(FileWriter writer = new FileWriter("/clientdata/text.txt", false))
                {
                    String serverWord = in.readLine(); // ждём, что скажет сервер
                    System.out.println(serverWord);
                    writer.write(serverWord);
                    writer.flush();

                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }

                out.write("get_checksum");
                out.flush();
                String serverChecksum = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverChecksum);

                String checksum =  "";
                try(FileReader reader = new FileReader("/clientdata/text.txt"))
                {
                    // читаем посимвольно
                    int c;
                    while((c=reader.read())!=-1){

                        checksum+=(char)c;
                    }
                    System.out.println(getMd5(checksum));

                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }


            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
                //out.close();

            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }

    }
}

