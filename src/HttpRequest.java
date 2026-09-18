public class HttpRequest {

    private final String method;
    private final String path;
    private final String version;

    public HttpRequest(String requestLine) {

        if (requestLine == null || requestLine.isBlank()) {
            throw new IllegalArgumentException(
                    "Empty HTTP request"
            );
        }

        String[] parts =
                requestLine.trim().split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Malformed HTTP request"
            );
        }

        this.method = parts[0];
        this.path = parts[1];
        this.version = parts[2];

        if (!version.equals("HTTP/1.0")
                && !version.equals("HTTP/1.1")) {

            throw new IllegalArgumentException(
                    "Unsupported HTTP version: " + version
            );
        }

        if (path.isBlank()) {
            throw new IllegalArgumentException(
                    "Empty request path"
            );
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }
}