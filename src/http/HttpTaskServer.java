package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;


import static java.nio.charset.StandardCharsets.UTF_8;
import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

/** Класс сервера.
 * Содержит эндпоинты, соответствующие вызовам базовых методов интерфейса TaskManager.
 */
public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final HttpTaskManager httpTaskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultHttpTaskManager());
    }

    public HttpTaskServer(HttpTaskManager defaultHttpTaskManager) throws IOException {
        this.httpTaskManager = defaultHttpTaskManager;
        this.httpTaskManager.loadFromFile();
        this.gson = Managers.getGson();
        this.server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        this.server.createContext("/tasks", this::handler);
        System.out.println("HTTP-сервер запускаем на " + PORT + " порту!");
    }

    private void handler(HttpExchange exchange) {
        try {
            final String path = exchange.getRequestURI().getPath().replaceFirst("/tasks/", "");
            switch (path) {
                case "task":
                    handleTasks(exchange);
                    break;
                case "subtask":
                    handleSubtask(exchange);
                    break;
                case "epic":
                    handleEpic(exchange);
                    break;
                case "history":
                    handleHistory(exchange);
                    break;
                default:
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при обработке запроса");
        } finally {
            exchange.close();
        }
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    String idTask = query.substring(3);
                    Task task = httpTaskManager.getTask(Integer.parseInt(idTask));
                    String response = gson.toJson(task);
                    sendText(exchange, response);
                    return;
                } else {
                    String response = gson.toJson(httpTaskManager.getTasks());
                    sendText(exchange, response);
                    return;
                }
            }
            case "POST": {
                try {
                    Task task = gson.fromJson(body, Task.class);
                    httpTaskManager.addTask(task);
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            case "DELETE": {
                if (query != null) {
                    String idTask = query.substring(3);
                    httpTaskManager.deleteTask(Integer.parseInt(idTask));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllTasks();
                    exchange.sendResponseHeaders(200, 0);
                }
            }
        }
    }

    private void handleEpic(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    String idEpic = query.substring(3);
                    Epic epic = httpTaskManager.getEpic( Integer.parseInt(idEpic));
                    String response = gson.toJson(epic);
                    sendText(exchange, response);
                    return;
                } else {
                    String response = gson.toJson(httpTaskManager.getEpics());
                    sendText(exchange, response);
                    return;
                }
            }
            case "POST": {
                try {
                    Epic epic = gson.fromJson(body, Epic.class);
                    httpTaskManager.addEpic(epic);
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            case "DELETE": {
                if (query != null) {
                    String idEpic = query.substring(3);
                    httpTaskManager.deleteEpic(Integer.parseInt(idEpic));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllEpics();
                    exchange.sendResponseHeaders(200, 0);
                }
            }
        }
    }

    private void handleSubtask(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        switch (requestMethod) {
            case "GET": {
                if (query != null) {
                    String idSubTask = query.substring(3);
                    Subtask subtask = httpTaskManager.getSubtask(Integer.parseInt(idSubTask));
                    String response = gson.toJson(subtask);
                    sendText(exchange, response);
                    return;
                } else {
                    String response = gson.toJson(httpTaskManager.getSubtasks());
                    sendText(exchange, response);
                    return;
                }
            }
            case "POST": {
                try {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    httpTaskManager.addSubtask(subtask);
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            case "DELETE": {
                if (query != null) {
                    String idSubTask = query.substring(3);
                    httpTaskManager.deleteSubtask(Integer.parseInt(idSubTask));
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    httpTaskManager.deleteAllSubtasks();
                    exchange.sendResponseHeaders(200, 0);
                }
            }
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            List<Task> history = httpTaskManager.getHistory();
            String response = gson.toJson(history);
            sendText(exchange, response);
        }
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту" + PORT);
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }
}
