import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpWebServer {

    private static final int PORT = 8080;
    private static final int MAX_THREADS = 50;

    public static void main(String[] args) {

        ExecutorService threadPool =
                Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            System.out.println("=================================");
            System.out.println("      Java HTTP Web Server");
            System.out.println("=================================");
            System.out.println(
                    "Server started on port: " + PORT
            );
            System.out.println(
                    "Open: http://localhost:" + PORT
            );
            System.out.println(
                    "Maximum client threads: " + MAX_THREADS
            );
            System.out.println("=================================");

            while (!serverSocket.isClosed()) {

                Socket socket =
                        serverSocket.accept();

                System.out.println(
                        "Client connected: " +
                        socket.getInetAddress()
                );

                threadPool.execute(() -> {

                    ClientHandler clientHandler =
                            new ClientHandler(socket);

                    clientHandler.handle();
                });
            }

        } catch (IOException e) {

            System.out.println(
                    "Server error: " + e.getMessage()
            );

        } finally {

            threadPool.shutdown();

            System.out.println(
                    "Server stopped."
            );
        }
    }
}