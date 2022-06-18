package sample;

import sample.Hasher;
import sample.Kuznechik;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {
    public static String fromUser;
    public static String friendIP;
    public static String friendPort;
    public static String myID;
    public static String myLogin;
    public static String pass;
    public static String friendID;
    public Sender(String friendIP, String friendPort, String myID, String friendID, String myLogin, String pass)
    {
        this.friendIP = friendIP;
        this.friendPort = friendPort;
        this.myID = myID;
        this.myLogin = myLogin;
        this.pass = pass;
        this.friendID = friendID;
    }
    public static void Sender1() throws IOException {
        String hostName = friendIP;
        int portNumber = Integer.parseInt(friendPort);
        try {
            Socket fromServer = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(fromServer.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            //String fromUser;

            Stream str = new Stream(fromServer);
            str.start();
            boolean flag = true;
            //while (flag)
            //{
                //fromUser = stdIn.readLine();
                if (fromUser != null)
                {
                    //if (fromUser.equals("5"))
                    //{
                    //    flag = false;
                    //}
                    //else {
                        //ШИФРОВАНИЕ СООБЩЕНИЯ
                        String temp;
                        temp = encryptKuz(fromUser, friendID);
                        //System.out.println(temp);
                        //System.out.println(temp);

                        //ОПРАВКА СООБЩЕНИЯ
                        out.println(myID + temp);
                        FileWriter writer = new FileWriter(friendID + ".txt", true);
                        BufferedWriter bufferedWriter = new BufferedWriter(writer);

                        bufferedWriter.write(encryptKuz1(myLogin + "\t" + fromUser, pass) + "\n");
                        bufferedWriter.close();
                    //}
                }
                fromUser = null;
            //}
        }
        catch (UnknownHostException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }
    static class Stream extends Thread
    {
        private BufferedReader in;
        Stream(Socket socket)  throws IOException
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        public void run()
        {
            String message;
            try
            {
                while ((message = in.readLine()) != null)
                {
                    System.out.println(message);
                }
            }
            catch(IOException e) { e.printStackTrace(); }
        }
    }

    public static String encryptKuz(String message, String IDFriend) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("keys.txt"));
        String line = "";
        while (!line.contains(IDFriend))
        {
            line = reader.readLine();
        }

        String keyTest = line.substring(6, 38);

        Kuznechik kuznechik = new Kuznechik(keyTest);
        Hasher hasher = new Hasher();

        return DatatypeConverter.printHexBinary(hasher.hash(keyTest)) +
                DatatypeConverter.printHexBinary(hasher.hash(message)) + kuznechik.encr(message);
    }

    public static String encryptKuz1(String message, String password)
    {
        //Hasher hasher = new Hasher();
        //String keyTest = DatatypeConverter.printHexBinary(hasher.hash(password + password));
        String pas = "";
        for(int i = 0;i < 7;i++)
        {
            pas = pas + password;
        }
        Kuznechik kuznechik = new Kuznechik(pas.substring(0, 32));
        return kuznechik.encr(message);
    }
}