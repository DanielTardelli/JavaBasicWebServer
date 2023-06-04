import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;


public class ServiceWorker implements Runnable {
    InputStream in = null;
    OutputStream out = null;
    Socket socket = null;

    public ServiceWorker(Socket socket) throws IOException {
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.socket = socket;
    }

    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringbuilder = new StringBuilder();
        String tmp = "";
        //PrintWriter writer = new PrintWriter(out, true);
        try {
            while ((tmp=bufferedReader.readLine()) != null && tmp.length() > 0) {
                stringbuilder.append(tmp + "\n");
            }
            //System.out.println(stringbuilder.toString());
            if (stringbuilder.indexOf("GET /") > -1) {
                System.out.println("[GET " + LocalDateTime.now() + "]");
                GetRequestHandler getRequestHandler = new GetRequestHandler(stringbuilder, this.out);
                getRequestHandler.handleRequest();
            } else if (stringbuilder.indexOf("POST /") > -1) {
                PostRequestHandler postRequestHandler = new PostRequestHandler(stringbuilder, this.out);
                postRequestHandler.handleRequest();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
