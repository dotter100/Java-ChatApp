package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class Server {

    private static final int PORT = 9001;
    protected static HashSet<String> names = new HashSet<String>();
    protected static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private static ExecutorService executor = Executors.newCachedThreadPool();



    public static void main(String[] args) {
	// write your code here
        System.out.println("The chat server is running.");
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
            while (true) {
                executor.submit(new ChatThread(listener.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
