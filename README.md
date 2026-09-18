# Java HTTP Web Server

A lightweight HTTP web server built from scratch using Java socket programming.

This project demonstrates the fundamentals of how an HTTP web server works internally, including TCP socket communication, HTTP request parsing, concurrent client handling, static file serving, HTTP response generation, error handling, and basic path traversal protection.

The server is implemented using core Java APIs without Spring Boot or any external web-server framework.

---

## 🚀 Features

- HTTP/1.0 and HTTP/1.1 request parsing
- HTTP `GET` request support
- Static file serving
- HTML and CSS file support
- Multi-client handling using a fixed thread pool
- Up to 50 worker threads
- Automatic `index.html` serving for `/`
- Query parameter handling
- URL decoding
- MIME type detection
- HTTP response headers
- `Content-Type` support
- `Content-Length` support
- `Connection: close` support
- Custom `404 Not Found` page
- `400 Bad Request` handling
- `403 Forbidden` handling
- `404 Not Found` handling
- `405 Method Not Allowed` handling
- Directory traversal protection
- Clean request and response logging
---

## 🏗️ Architecture

```text
                    ┌────────────────────┐
                    │    Web Browser     │
                    └─────────┬──────────┘
                              │
                              │ HTTP Request
                              ▼
                    ┌────────────────────┐
                    │   HttpWebServer    │
                    │   ServerSocket     │
                    │      Port 8080     │
                    └─────────┬──────────┘
                              │
                              │ Accept Connection
                              ▼
                    ┌────────────────────┐
                    │    Thread Pool     │
                    │   50 Worker Threads│
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │   ClientHandler    │
                    │ Request Processing │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │    HttpRequest     │
                    │ Method / Path /    │
                    │ HTTP Version       │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │      webroot       │
                    │   Static Files     │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │   HTTP Response    │
                    │ Status + Headers   │
                    │ + File Content     │
                    └────────────────────┘
##📁 Project Structure
HTTP-Web-Server-in-java--main/
│
├── src/
│   ├── ClientHandler.java
│   ├── HttpRequest.java
│   └── HttpWebServer.java
│
├── webroot/
│   ├── index.html
│   ├── about.html
│   ├── contact.html
│   ├── 404.html
│   └── style.css
│
├── docs/
│   └── HTTP_Web_Server_Java_Report.pdf
│
├── .gitignore
└── README.md

🛠️ Technologies Used
Backend
-Java
-Java Socket Programming
-ServerSocket
-Socket
-Java I/O
-Java NIO
-ExecutorService
-Fixed Thread Pool
Frontend
-HTML5
-CSS3
Networking
-TCP
-HTTP/1.0
-HTTP/1.1

---

## ⚙️ How the Server Works

### 1. Server Startup

`HttpWebServer` creates a `ServerSocket` on port `8080`.

```text
http://localhost:8080
2. Accepting Client Connections

When a browser connects to the server, the connection is accepted and submitted to a fixed thread pool.

The current configuration uses:

50 worker threads

This allows multiple client connections to be processed concurrently.

3. HTTP Request Parsing

The incoming HTTP request line is passed to HttpRequest.

Example:

GET /index.html HTTP/1.1

The request is parsed into:

Method  → GET
Path    → /index.html
Version → HTTP/1.1

The server currently accepts:

HTTP/1.0
HTTP/1.1
4. Request Processing

ClientHandler performs the following operations:

Reads the HTTP request line
Validates the request
Reads the HTTP headers
Checks the HTTP method
Removes query parameters
Decodes URL-encoded paths
Maps / to /index.html
Resolves the requested file
Validates the requested path
Determines the MIME type
Reads the requested file
Sends the HTTP response
5. Static File Serving

Files are served from the webroot directory.

For example:

/index.html

maps to:

webroot/index.html

The root URL:

/

automatically serves:

webroot/index.html

6. HTTP Response

The server generates an HTTP response containing:

HTTP status code
Content-Type
Content-Length
Connection
Requested file content

Example:

HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Content-Length: 1234
Connection: close
## 🔐 Security

The server includes basic protection against directory traversal attacks.

Before serving a requested resource, the requested path is normalized and checked to ensure that it remains inside the `webroot` directory.

Requests attempting to access resources outside the webroot are rejected with:

```text
403 Forbidden
🎨 Web Interface

The project includes a simple web interface to demonstrate static file serving.

Home Page
/

The home page provides an overview of the server and its features.

About Page
/about.html

Provides information about the project architecture, technologies, and security.

Contact Page
/contact.html

Provides basic project and developer information.

Custom 404 Page
/404.html

A custom error page is displayed when a requested resource cannot be found.
## ▶️ Getting Started

### Prerequisites

You need:

- Java JDK 17 or later
- Command Prompt or terminal
- A modern web browser

Check your Java installation:

```bash
java -version
Check the Java compiler:

javac -version
💻 Running on Windows

Open Command Prompt and navigate to the project directory.

1. Compile the Project
javac -d bin src\*.java

This compiles all Java source files and places the generated .class files inside the bin directory.

2. Start the Server
java -cp bin HttpWebServer

Expected output:

=================================
      Java HTTP Web Server
=================================
Server started on port: 8080
Open: http://localhost:8080
Maximum client threads: 50
=================================
3. Open the Server

Open your browser and visit:

http://localhost:8080/
🧪 Testing
Test the Home Page

Open:

http://localhost:8080/

Expected response:

200 OK
Test the About Page

Open:

http://localhost:8080/about.html

Expected response:

200 OK
Test the Contact Page

Open:

http://localhost:8080/contact.html

Expected response:

200 OK
Test the CSS File

Open:

http://localhost:8080/style.css

The stylesheet should be served successfully.

Test the Custom 404 Page

Open:

http://localhost:8080/doesnotexist.html

Expected response:

404 Not Found

The custom 404.html page should be displayed.

📋 Example Server Output

A successful request produces clean logs similar to:

=================================
      Java HTTP Web Server
=================================
Server started on port: 8080
Open: http://localhost:8080
Maximum client threads: 50
=================================

Client connected: /0:0:0:0:0:0:0:1
Request: GET / HTTP/1.1
Response: 200 OK

Client connected: /0:0:0:0:0:0:0:1
Request: GET /style.css HTTP/1.1
Response: 200 OK
🧠 Key Concepts Demonstrated

This project provides hands-on experience with:

TCP socket programming
Client-server architecture
HTTP protocol fundamentals
HTTP request parsing
HTTP response construction
Java multithreading
Thread pools
File handling
Java NIO
MIME type detection
HTTP status codes
URL decoding
Query parameter handling
Static resource serving
Basic web security
---

## 📚 Class Responsibilities

### `HttpWebServer.java`

Responsible for:

- Starting the HTTP server
- Creating the `ServerSocket`
- Listening on port `8080`
- Accepting incoming client connections
- Managing the fixed thread pool
- Assigning client connections to worker threads

---

### `ClientHandler.java`

Responsible for:

- Processing individual client connections
- Reading HTTP requests
- Reading HTTP headers
- Checking the HTTP method
- Processing requested paths
- Resolving files from `webroot`
- Preventing directory traversal
- Determining MIME types
- Reading file contents
- Generating HTTP responses
- Handling HTTP errors

---

### `HttpRequest.java`

Responsible for:

- Parsing the HTTP request line
- Extracting the HTTP method
- Extracting the requested path
- Extracting the HTTP version
- Validating the request format
- Rejecting unsupported HTTP versions

---

## 📈 Concurrency

The server uses Java's `ExecutorService` with a fixed thread pool:

```java
Executors.newFixedThreadPool(50);
When a client connects, its socket is submitted to the thread pool for processing.

This allows multiple clients to connect and request resources concurrently without creating an unlimited number of threads.

🔄 Request Flow

A typical request follows this flow:
Browser
   │
   │ GET /about.html HTTP/1.1
   ▼
ServerSocket
   │
   ▼
Thread Pool
   │
   ▼
ClientHandler
   │
   ▼
HttpRequest
   │
   ▼
Path Validation
   │
   ▼
webroot/about.html
   │
   ▼
HTTP 200 Response
   │
   ▼
Browser
🧪 Error Flow

If something goes wrong while processing a request:
HTTP Request
     │
     ▼
Request Validation
     │
     ├── Invalid Request ──────► 400 Bad Request
     │
     ├── Unsupported Method ───► 405 Method Not Allowed
     │
     ├── Unsafe Path ──────────► 403 Forbidden
     │
     ├── File Not Found ───────► 404 Not Found
     │
     └── Valid File ───────────► 200 OK
     🔮 Future Improvements

Possible future improvements include:

Support for HEAD requests
Support for POST requests
HTTP keep-alive connections
Configurable server port
Configurable webroot directory
Improved HTTP request parsing
Additional MIME type mappings
Access logging
Error logging
Graceful server shutdown
Automated unit tests
Integration testing
Load and performance testing
HTTPS/TLS support
📄 Documentation

Additional technical documentation is available in:

docs/HTTP_Web_Server_Java_Report.pdf

The report contains additional information about the project implementation and concepts.

🎯 Project Objective

The main objective of this project is to understand how a basic HTTP web server works internally by implementing its core functionality directly using Java networking and file-handling APIs.

The complete request lifecycle can be summarized as:

TCP Connection
      ↓
HTTP Request
      ↓
Request Parsing
      ↓
Path Validation
      ↓
File Resolution
      ↓
File Reading
      ↓
HTTP Response
      ↓
Client Browser
⭐ Project Highlights
Built from scratch using core Java
No web framework required
Socket-based HTTP communication
HTTP request parsing
Multi-client support
Fixed thread pool
Static file serving
HTTP error handling
Custom 404 page
Directory traversal protection
HTML/CSS web interface
Technical project documentation
📌 Disclaimer

This project was developed for educational purposes to understand networking, HTTP, Java sockets, multithreading, file handling, and basic web-server architecture.

It is not intended to replace production-grade web servers such as Apache HTTP Server or Nginx.

👩‍💻 Author

Raheena

GitHub: P188-raheena