package ChatClient;


import ChatShare.Request;
import ChatShare.RequestType;
import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    ObjectInputStream input;
    ObjectOutputStream out;


    public ChatClient() {
    }

    private String getServerAddress() {

        System.out.println("Enter Chat Server IP:");
        Scanner in = new Scanner(System.in);


        String s = in.nextLine();

        return s;
    }


    private Request getName() {

        System.out.println("Enter A username:");
        Scanner in = new Scanner(System.in);

        String s = in.next();

        Request requestitem = new Request(RequestType.name,s);

        return requestitem;
    }

    private void SendMessage(){

        System.out.println("Send Message");
        Scanner in = new Scanner(System.in);
        try {
        while (true) {
            String s = in.nextLine();

            Request requestitem = new Request(RequestType.sendmsg,s,null);
            out.writeObject(requestitem);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void run() throws IOException {


        try {
            String serverAddress = getServerAddress();
            Socket socket = new Socket(serverAddress, 7676);
            //output stream input stream
            out = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            //Recieve message from server of the the request
            while (socket.isConnected()) {
                Object recieved = input.readObject();

                if (recieved.getClass() == Request.class) {

                    Request requestrecieved = (Request) recieved;

                    switch (requestrecieved.requestType){
                        case name:
                            out.writeObject(getName());
                            break;
                        case NameCorrect:
                            Thread item = new Thread(() -> SendMessage());
                            item.start();
                            break;
                        case msg:
                            System.out.println(requestrecieved.username + " : " + requestrecieved.Message);
                            break;
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        ChatClient chat = new ChatClient();
        chat.run();

    }




}