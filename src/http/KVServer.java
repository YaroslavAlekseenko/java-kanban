package http;

import http.exception.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/** Класс сервера.
 * Хранит состояние менеджера.
 */
public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken = this.generateApiToken();
    private final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8078), 0);
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        this.server.createContext("/register", this::register);
        this.server.createContext("/save", this::save);
        this.server.createContext("/load", this::load);
    }

    /** Метод, который отвечает за получение данных.
     *  Получает значения по ключу.
     */
    private void load(HttpExchange h) {
        try {
            System.out.println("\n/load");
            if (!this.hasAuth(h)) {
                System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением API-ключа");
                h.sendResponseHeaders(403, 0L);
                return;
            }

            if (!"GET".equals(h.getRequestMethod())) {
                System.out.println("/load ждет GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0L);
                return;
            }

            String key = h.getRequestURI().getPath().substring("/load/".length());
            if (key.isEmpty()) {
                System.out.println("Key для сохранения пустой. Key указывается в пути: /load/{key}");
                h.sendResponseHeaders(400, 0L);
                return;
            }

            if (this.data.containsKey(key)) {
                this.sendText(h, this.data.get(key));
                System.out.println("Значение для ключа " + key + " успешно отправлено в ответ на запрос!");
                return;
            }

            System.out.println("Не могу достать данные для ключа '" + key + "', данные отсутствуют");
            h.sendResponseHeaders(404, 0L);
        } catch (IOException var6) {
            throw new KVServerLoadException("Ошибка метода load в классе KVServer.", var6);
        } finally {
            h.close();
        }

    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (this.hasAuth(h)) {
                if ("POST".equals(h.getRequestMethod())) {
                    String key = h.getRequestURI().getPath().substring("/save/".length());
                    if (key.isEmpty()) {
                        System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                        h.sendResponseHeaders(400, 0L);
                        return;
                    }

                    String value = this.readText(h);
                    if (value.isEmpty()) {
                        System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                        h.sendResponseHeaders(400, 0L);
                        return;
                    }

                    this.data.put(key, value);
                    System.out.println("Значение для ключа " + key + " успешно обновлено!");
                    h.sendResponseHeaders(200, 0L);
                    return;
                }

                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0L);
                return;
            }

            System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
            h.sendResponseHeaders(403, 0L);
        } finally {
            h.close();
        }

    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                this.sendText(h, this.apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0L);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту 8078");
        System.out.println("Открой в браузере http://localhost:8078/");
        System.out.println("API_TOKEN: " + this.apiToken);
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
        System.out.println("Сервер на порту 8078 был остановлен");
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + this.apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
