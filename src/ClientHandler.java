import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientHandler {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void handle() {

        try (
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            OutputStream out = socket.getOutputStream()
        ) {

            // Read the HTTP request line
            String requestLine = in.readLine();

            if (requestLine == null || requestLine.isBlank()) {
                return;
            }

            System.out.println("Request: " + requestLine);

            // Parse the HTTP request
            HttpRequest request;

            try {

                request = new HttpRequest(requestLine);

            } catch (IllegalArgumentException e) {

                sendResponse(
                        out,
                        "400 Bad Request",
                        "<html>" +
                        "<head><title>400 Bad Request</title></head>" +
                        "<body>" +
                        "<h1>400 - Bad Request</h1>" +
                        "<p>The HTTP request is invalid.</p>" +
                        "</body>" +
                        "</html>"
                );

                return;
            }
            // Read remaining HTTP headers
String line;

while ((line = in.readLine()) != null && !line.isEmpty()) {
    // Headers are read to complete the HTTP request.
}
            

            // Only GET requests are supported
            if (!request.getMethod().equalsIgnoreCase("GET")) {

                sendResponse(
                        out,
                        "405 Method Not Allowed",
                        "<html>" +
                        "<head><title>405 Method Not Allowed</title></head>" +
                        "<body>" +
                        "<h1>405 - Method Not Allowed</h1>" +
                        "<p>Only GET requests are supported.</p>" +
                        "</body>" +
                        "</html>"
                );

                return;
            }

            String path = request.getPath();

            // Remove query parameters
            int queryIndex = path.indexOf('?');

            if (queryIndex >= 0) {
                path = path.substring(0, queryIndex);
            }

            // Decode URL-encoded characters
            path = URLDecoder.decode(
                    path,
                    StandardCharsets.UTF_8
            );

            // Root URL serves index.html
            if (path.equals("/")) {
                path = "/index.html";
            }

            // Web root directory
            Path webRoot = Path.of("webroot")
                    .toAbsolutePath()
                    .normalize();

            // Resolve requested file
            Path requestedFile = webRoot
                    .resolve(path.substring(1))
                    .normalize();

            // Prevent directory traversal attacks
            if (!requestedFile.startsWith(webRoot)) {

                sendResponse(
                        out,
                        "403 Forbidden",
                        "<html>" +
                        "<head><title>403 Forbidden</title></head>" +
                        "<body>" +
                        "<h1>403 - Forbidden</h1>" +
                        "<p>Access to this resource is not allowed.</p>" +
                        "</body>" +
                        "</html>"
                );

                return;
            }

            // Check whether the file exists
           // Check whether the requested file exists
if (!Files.exists(requestedFile)
        || !Files.isRegularFile(requestedFile)) {

    // Try to serve the custom 404 page
    Path notFoundPage = webRoot.resolve("404.html").normalize();

    if (Files.exists(notFoundPage)
            && Files.isRegularFile(notFoundPage)) {

        byte[] errorPage =
                Files.readAllBytes(notFoundPage);

        sendFileResponse(
                out,
                "404 Not Found",
                "text/html; charset=UTF-8",
                errorPage
        );
        System.out.println("Response: 404 Not Found");

    } else {

        // Fallback if 404.html is missing
        sendResponse(
                out,
                "404 Not Found",
                "<html>" +
                "<head><title>404 Not Found</title></head>" +
                "<body>" +
                "<h1>404 - Page Not Found</h1>" +
                "<p>The requested page does not exist.</p>" +
                "</body>" +
                "</html>"
        );
    }

    return;
}

            // Read requested file
            byte[] fileBytes = Files.readAllBytes(requestedFile);

            // Determine content type
            String contentType = getContentType(requestedFile);

            // Send file
            sendFileResponse(
                    out,
                    "200 OK",
                    contentType,
                    fileBytes
            );
            System.out.println("Response: 200 OK");

        } catch (IOException e) {

            System.out.println(
                    "Client error: " + e.getMessage()
            );

        } finally {

            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Sends an HTML response.
     */
    private void sendResponse(
            OutputStream out,
            String status,
            String body) throws IOException {

        byte[] bodyBytes =
                body.getBytes(StandardCharsets.UTF_8);

        String response =
                "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + bodyBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        out.write(
                response.getBytes(StandardCharsets.UTF_8)
        );

        out.write(bodyBytes);
        out.flush();
    }

    /**
     * Sends a file response.
     */
    private void sendFileResponse(
            OutputStream out,
            String status,
            String contentType,
            byte[] fileBytes) throws IOException {

        String response =
                "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        out.write(
                response.getBytes(StandardCharsets.UTF_8)
        );

        out.write(fileBytes);
        out.flush();
    }

    /**
     * Determines the MIME type of a file.
     */
    private String getContentType(Path file)
            throws IOException {

        String contentType =
                Files.probeContentType(file);

        if (contentType != null) {
            return contentType;
        }

        String fileName =
                file.getFileName()
                        .toString()
                        .toLowerCase();

        if (fileName.endsWith(".html")
                || fileName.endsWith(".htm")) {

            return "text/html; charset=UTF-8";
        }

        if (fileName.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }

        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        if (fileName.endsWith(".txt")) {
            return "text/plain; charset=UTF-8";
        }

        return "application/octet-stream";
    }
}