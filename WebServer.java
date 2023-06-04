import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class WebServer {
    private int PORT;
    ServerSocket serverSocket = null;

    public WebServer(int port) {
        this.PORT = port;
    }

    public int getPort() {
        return PORT; 
    }

    public void runServer() {
        try {
            serverSocket = new ServerSocket(getPort());
            
            while (true) {
                // do server things
                Socket socket = serverSocket.accept();
                ServiceWorker serviceWorker = new ServiceWorker(socket);
                new Thread(serviceWorker).start();
            }
        } catch (IOException e) {

        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("[MAJOR ERROR] " + e);
            }
        }
    }
}
