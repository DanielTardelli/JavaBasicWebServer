import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

abstract class TypedRequestHandler {
    public abstract void printRequest();
    public abstract void respondRequest();
    public abstract void handleRequest();
}

public class PostRequestHandler extends TypedRequestHandler {
    private StringBuilder stringBuilder = null;
    private String string = null;
    private PrintWriter writer = null;
    private StringBuilder content = null;
    private HashMap<String, String> keyVals = null;
    private HashMap<String, String> headers = null;

    public PostRequestHandler(StringBuilder stringBuilder, OutputStream out) {
        this.stringBuilder = stringBuilder;
        this.string = stringBuilder.toString();
        this.writer = new PrintWriter(out, true);
        this.content = new StringBuilder();
        this.content.append("HTTP/1.1 200 OK\n\n<html><head></head><body>");
        this.keyVals = new HashMap<String,String>();
        this.headers = new HashMap<String,String>();
    }

    @Override
    public void printRequest() {
        System.out.println("//////////////////////////////////////////////////////////////");
        System.out.println("HEADERS:");
        for(Map.Entry<String, String> set : headers.entrySet()) {
            System.out.println("    " + set.getKey() + ": " + set.getValue());
        }
        System.out.println("CONTENT");
        for(Map.Entry<String, String> set : keyVals.entrySet()) {
            System.out.println("    " + set.getKey() + ": " + set.getValue());
        }
        System.out.println("//////////////////////////////////////////////////////////////");
    }

    @Override
    public void respondRequest() {
        this.content.append("</body></html>");
        this.writer.println(content.toString());
    }


    @Override
    public void handleRequest() {
        try {

        } catch (Error e) {
            e.printStackTrace();
        } finally {
            respondRequest();
        }
    }
}
