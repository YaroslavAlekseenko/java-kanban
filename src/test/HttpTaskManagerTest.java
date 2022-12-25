package test;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import http.HttpTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import managers.Managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer kvServerTest;
    HttpTaskServer httpServerTest;
    Gson gson = Managers.getGson();

    @BeforeEach
    void startHttpTaskManagerTest() throws IOException, InterruptedException {
        kvServerTest = new KVServer();
        kvServerTest.start();
        httpServerTest = new HttpTaskServer();
        httpServerTest.start();
        HttpTaskManager manager = new HttpTaskManager();

        Epic epic = new Epic("Epic 1", "Переезд");

        Subtask subtask = new Subtask("Subtask 1", "Собрать вещи", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 11, 11), 11, manager.getEpic(1));

        Task task = new Task("Task 1", "Помыть посуду", Status.NEW,
                LocalDateTime.of(2111, 1, 1, 11, 11), 11);

        String str = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String>  responseEpic = client.send(request, HttpResponse.BodyHandlers.ofString());

        str = gson.toJson(subtask);
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String>  responseSub = client.send(request, HttpResponse.BodyHandlers.ofString());

        str = gson.toJson(task);
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String>  responseTask = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseEpic.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertEquals(200, responseSub.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertEquals(200, responseTask.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @AfterEach
    void stopHttpTaskManagerTest() {
        httpServerTest.stop();
        kvServerTest.stop();
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<HashMap<Integer, Epic>>() {
        }.getType();
        HashMap<Integer, Epic> list = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Неверное количество задач");
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType();
        HashMap<Integer, Subtask> list = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Неверное количество задач");
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<HashMap<Integer, Task>>() {
        }.getType();
        HashMap<Integer, Task> list = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Неверное количество задач");
    }

    @Test
    void testGetEpicById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/epic?id=1")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testGetSubtaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/subtask?id=2")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "ЗЗапрос неуспешен, неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8080/tasks/task?id=3")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task received = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
        assertNotNull(received, "Задачи не возвращаются");
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        HttpTaskManager manager = new HttpTaskManager();
        Subtask newSubtask = new Subtask("AddSubtask", "Description", Status.NEW,
                LocalDateTime.of(2555, 5, 15, 15, 15), 15, manager.getEpic(1));

        String str = gson.toJson(newSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask?id=5");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task newTask = new Task("AddTask", "Description", Status.NEW,
                LocalDateTime.of(2444, 4, 14, 14, 14), 14);

        String str = gson.toJson(newTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteAllTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteAllEpic() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Запрос неуспешен, неверный статус-код");
    }
}