import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server{

    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try (PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true)) {
                toClient.println("Hello from server" + clientSocket.getInetAddress());
            }catch(IOException e){
                e.printStackTrace();
            }
        };
    }
    

    public static void main(String args[]){
        int port = 8010;
        Server s = new Server();
        try{
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(10000);
            System.out.println("Server started at port " + port);
            while(true){
                Socket socket = server.accept();
                System.out.println("Client connected");
                Thread thread = new Thread(() -> s.getConsumer().accept(socket));
                thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();    
        }   
    }
}