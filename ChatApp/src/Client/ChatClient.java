package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    BufferedReader in;
    PrintWriter out;


    public ChatClient() {
    }

    private String getServerAddress() {

        System.out.println("Enter Chat Server IP:");
        Scanner in = new Scanner(System.in);


        String s = in.nextLine();

        return s;
    }


    private String getName() {

        System.out.println("Enter A username:");
        Scanner in = new Scanner(System.in);

        String s = in.next();

        return s;
    }

    private void SendMessage(){

        System.out.println("Send Message");
        Scanner in = new Scanner(System.in);
        while (true) {
            String s = in.nextLine();
            out.println(s);
        }

    }


    private void run() throws IOException {

        // Make connection and initialize streams
        try {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);

            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                Thread item = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendMessage();
                    }
                });
                item.start();
            } else if (line.startsWith("MESSAGE")) {
                System.out.println(line.substring(8) + "\n");
            }
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        ChatClient chat = new ChatClient();
        chat.run();

    }




    }
