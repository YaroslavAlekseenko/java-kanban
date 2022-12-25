package http;

import http.exception.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

/** Класс клиента.
 * Переносит хранение состояния менеджера на отдельный сервер.
 */
public class KVTaskClient {
    private final String apiToken;
    private final String url = "http://localhost:8078/";

    public KVTaskClient() {
        this.apiToken = this.register(this.url);
    }

    public String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "register")).header("Content-Type", "application/json").build();
            HttpResponse<String> send = client.send(request, BodyHandlers.ofString());
            return send.body();
        } catch (Exception e) {
            throw new KVTaskClientRegisterException(e);
        }
    }

    /** Метод void put(String key, String json) должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN= */
    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(json)).uri(URI.create(this.url + "save/" + key + "?API_TOKEN=" + this.apiToken)).header("Content-Type", "application/json").build();
            client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientPutException("Не удалось сохранить данные", e);
        }
    }

    /** Метод String load(String key) должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=. */
    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(this.url + "load/" + key + "?API_TOKEN=" + this.apiToken)).header("Content-Type", "application/json").build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        } catch (InterruptedException | IOException e) {
            throw new KVTaskClientLoadException("Во время запроса произошла ошибка", e);
        }
    }
}