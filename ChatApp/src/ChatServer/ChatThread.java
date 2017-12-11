package ChatServer;

import ChatShare.Request;
import ChatShare.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static ChatServer.ChatServer.names;
import static ChatServer.ChatServer.writers;

public class ChatThread extends Thread {

    private Socket socket;
    private  ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;


    private ChatServer chatserver;



    public ChatThread(Socket socket) {
        this.socket = socket;
    }
    public void run() {

        try {


            System.err.println("Connecting started");


            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            Request request = new Request(RequestType.name);

            output.writeObject(request);
            //MessageHandler if socket is still connected
            while (socket.isConnected()) {
                Object recieved = input.readObject();
                if(recieved.getClass() == Request.class) {
                    Request recievedRequest = (Request) recieved;
                    //Switch to handler different request by requestType
                    switch (recievedRequest.requestType){
                        //Response on message request
                        case sendmsg:
                            Request answer = new Request(RequestType.msg,recievedRequest.Message,name);
                            for (ObjectOutputStream writer : writers) {
                                writer.writeObject(answer);
                            }
                            break;
                        //Response on set name
                        case name:
                            //synchrinized name list check
                            synchronized (names) {
                                //check if name is already in use
                                name = recievedRequest.username;
                                if (!names.contains(name)) {

                                    names.add(name);
                                    writers.add(output);
                                    Request requestitem = new Request(RequestType.NameCorrect);
                                    output.writeObject(requestitem);
                                }else{
                                    output.writeObject(request);

                                }
                            }
                            break;


                    }

                }



            }
            output.close();
            input.close();

            socket.close();

        } catch (IOException ioException) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            //If client disconnect to remove there name out the names list and there write outputstream
            if (name != null) {
                names.remove(name);
            }
            if (output != null) {
                writers.remove(output);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
