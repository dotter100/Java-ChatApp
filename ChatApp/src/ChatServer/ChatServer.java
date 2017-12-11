package ChatServer;

import Server.Manger;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ChatServer {


    private ServerSocket serverSocket;

    private Socket socket = null;

    private Manger ClientManger = new Manger();

    protected static HashSet<ObjectOutputStream> writers = new HashSet<>();

    protected static HashSet<String> names = new HashSet<>();

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public ChatServer() throws Exception {
        //setup SocketFactory on selected port(7676)
        ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
        serverSocket = (ServerSocket) socketFactory.createServerSocket(7676);

    }
    //Server waiting for connection to add them to a CachedThreadPool
    private void runServer() {
        System.err.println("Waiting for connections...");
        try {
            while (true) {


                socket = serverSocket.accept();
                System.out.println("waiting for next connection..");
                // new thread for a client
                executor.submit(new ChatThread(socket));
            }


        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ChatServer server = new ChatServer();
        server.runServer();
    }
}
