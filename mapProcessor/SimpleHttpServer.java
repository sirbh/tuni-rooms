import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        // Create a new HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/post", new PostHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is running on http://localhost:8080/post");
    }

    

    static class PostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the request body
                InputStream inputStream = exchange.getRequestBody();
                String requestBody = readInputStream(inputStream);

                // Manually parse JSON body
                String room = parseJsonField(requestBody, "room");
                String location = parseJsonField(requestBody, "location");

                // Print the parameters to the console
                System.out.println("Received POST request with parameters: room=" + room + ", location=" + location);

                // Send a response

                List<Float> coordinates = PDFHighlighter.getMap(location, room.replace(" ","" ));
                List<Float> coordinatesInPixels = new ArrayList<>();
                for (int i = 0; i < coordinates.size(); i++) {
                    coordinatesInPixels.add(convertPointsToPixels(coordinates.get(i), 700));
                }
                String base64Image = encodeImageToBase64("output_page_1.jpeg");

                String jsonResponse = "{\"image\":\"" + base64Image + "\",\"coordinates\":" + coordinatesInPixels.toString() + "}";
                // System.out.println("Sending response: " + jsonResponse);
                exchange.sendResponseHeaders(200, jsonResponse.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(jsonResponse.getBytes());
                outputStream.close();
            } else {
                System.out.println("Unsupported method: " );
                // Handle unsupported methods
                String response = "Only POST requests are supported";
                exchange.sendResponseHeaders(405, response.length()); // 405 Method Not Allowed
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        }

        private String readInputStream(InputStream inputStream) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            return stringBuilder.toString();
        }

        // Function to manually parse a JSON field from the string
        private String parseJsonField(String json, String fieldName) {
            String searchPattern = "\"" + fieldName + "\":\"";
            int startIndex = json.indexOf(searchPattern);
            if (startIndex == -1) {
                return null; // Field not found
            }
            startIndex += searchPattern.length();
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        }

        private String encodeImageToBase64(String imagePath) throws IOException {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(imageBytes);
        }

        private static float convertPointsToPixels(float pointValue, int dpi) {
            return pointValue * dpi / 72;
        }
    }
}
