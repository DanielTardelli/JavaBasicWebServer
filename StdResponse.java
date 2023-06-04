public class StdResponse {
    String hostname = null;
    String content = null;

    public StdResponse(String hostname) {
        this.hostname = hostname;
    }

    public StdResponse() {
        this.hostname = "localhost:3000";
    }

    public String getResponse() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("HTTP/1.1 200 OK\n");
        stringbuilder.append("Content-Type: text/html\n\n");
        stringbuilder.append("<html><head></head><body><p>hello world</p></body></html>\n");
        return stringbuilder.toString();
    }
}
