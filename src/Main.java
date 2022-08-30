import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args){
        try {
            HttpServer server = makeServer();
            server.start();
            initRoutes(server);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static HttpServer makeServer() throws IOException {
        String host = "localhost";
        InetSocketAddress address = new InetSocketAddress(host, 9889);

        System.out.printf("Запускаем сервер по адресу: http://%s:%s%n", address.getHostName(), address.getPort());

        HttpServer server = HttpServer.create(address, 50);
        System.out.println("Удачно!");
        return server;
    }

    private static void initRoutes(HttpServer server){
        server.createContext("/", Main::handleRequest);

        server.createContext("/apps/", Main::handleApps);

        server.createContext("/apps/profile",Main::handleAppsProfile);
    }

    private static void handleRequest(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int response = 200;
            int length = 0;
            exchange.sendResponseHeaders(response, length);

            try(PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext().getPath();

                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                writeHeaders(writer, "Заголовки запроса", exchange.getRequestHeaders());
                writeData(writer, exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void handleApps(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int response = 200;
            int length = 0;
            exchange.sendResponseHeaders(response, length);

            try(PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext().getPath();

                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                write(writer, "Приложение", "ZOR");
                writeData(writer, exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void handleAppsProfile(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int response = 200;
            int length = 0;
            exchange.sendResponseHeaders(response, length);

            try(PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext().getPath();

                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                write(writer,"Ваш профиль", "Master");
                write(writer,"Имя", "Жоомарт");
                write(writer,"Фамилия", "Эркинбек");
                write(writer,"Титул", "Хокаге");
                write(writer,"Клан", "Черик");
                writeData(writer, exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void writeHeaders(Writer writer, String type, Headers headers) {
        write(writer, type, "");
        headers.forEach( (k, v) -> write(writer, "\t" + k, v.toString()) );
    }

    private static void write(Writer writer, String message, String method) {
        String data = String.format("%s: %s%n%n", message, method);
        try {
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static PrintWriter getWriterFrom(HttpExchange exchange) {
        OutputStream output = exchange.getResponseBody();
        Charset charset = StandardCharsets.UTF_8;
        return new PrintWriter(output, false, charset);
    }

    private static BufferedReader getReader(HttpExchange exchange){
        InputStream input = exchange.getRequestBody();
        Charset charset = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input, charset);
        return new BufferedReader(isr);
    }

    private static void writeData(Writer writer, HttpExchange exchange){
        try(BufferedReader reader = getReader(exchange)){
            if(!reader.ready()) return;

            write(writer, "Блок данных", "");
            reader.lines().forEach( e -> write(writer, "\t", e));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
