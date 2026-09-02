import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                int port = 8010;
                try{
                    InetAddress address = InetAddress.getByName("localhost");
                    Socket socket = new Socket(address, port);
                    try{
                        PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        toSocket.println("Hello from client" + socket.getLocalSocketAddress());
                        String line = fromSocket.readLine();
                        System.out.println("Client received: " + line);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
    }
    public static void main(String args[]){
        Client client = new Client();
        for(int i = 0; i < 100; i++){
            try{
                Thread thread = new Thread(Client.getRunnable());
                thread.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
