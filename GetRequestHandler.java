import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

abstract class TypedRequestHandler {
    public abstract void printRequest();
    public abstract void respondRequest();
    public abstract void handleRequest();
}

public class GetRequestHandler extends TypedRequestHandler {
    private StringBuilder stringBuilder = null;
    private String string = null;
    private PrintWriter writer = null;
    private StringBuilder content = null;
    private HashMap<String, String> params = null;
    private HashMap<String, String> headers = null;

    public GetRequestHandler(StringBuilder stringBuilder, OutputStream out) {
        this.stringBuilder = stringBuilder;
        this.string = stringBuilder.toString();
        this.writer = new PrintWriter(out, true);
        this.content = new StringBuilder();
        this.content.append("HTTP/1.1 200 OK\n\n<html><head></head><body>");
        this.params = new HashMap<String,String>();
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
        for(Map.Entry<String, String> set : params.entrySet()) {
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
        // find index of ? and index of HTTP, if ? is before HTTP, then there are params
        // use ? token to identify start and whitespace to identify end
        // use & as first delim and then on sub params use = as delim.
        // lhs = key, rhs = val

        // should also create a hm of headers which can be readily accessed
        try {
            String[] s = stringBuilder.toString().split("\\r?\\n|\\r");
            for (int i = 0; i < s.length; i++) {
                if (i == 0) {
                    int index1 = s[i].indexOf("?") + 1;
                    int index2 = s[i].indexOf("HTTP");
                    this.content.append("<table><tbody>");
                    if (index1 != 0) {
                        // 0 is because of offset for index1, would usually be -1
                        this.content.append("<tr><td colspan=\"2\" style=\"text-align: center\"> PARAMETERS: </td></tr>");
                        String[] paramsSplit = s[i].substring(index1, index2).split("&");
                        for (int j = 0; j < paramsSplit.length; j++) {
                            this.content.append("<tr>");
                            String[] keyVal = paramsSplit[j].split("=");
                            this.content.append("<td>" + keyVal[0] + "</td>");
                            this.content.append("<td>" + keyVal[1] + "</td>");
                            params.put(keyVal[0], keyVal[1]);
                            this.content.append("</tr>");
                        }
                    }
                    this.content.append("<tr><td colspan=\"2\" style=\"text-align: center\">HEADERS:</td></tr>");
                } else {
                    String[] keyValSplit = s[i].split(": ");
                    this.content.append("<tr><td>" + keyValSplit[0] + "</td><td>" + keyValSplit[1] + "</td></tr>");
                    headers.put(keyValSplit[0], keyValSplit[1]);
                }
            }
            this.content.append("</tbody></table>");
        } catch (Error e) {
            e.printStackTrace();
        } finally {
            this.respondRequest();
        }
    }
}
