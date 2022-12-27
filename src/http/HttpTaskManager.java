package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.*;
import tasks.*;

import java.lang.reflect.Type;
import java.util.List;

/** Класс менеджера.
 * Запускается на старте программы, управляет всеми задачами и хранит информацию на сервере.
 */
public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public HttpTaskManager() {
        super(null);
        this.gson = Managers.getGson();
        this.client = new KVTaskClient();
    }

    public void load() {
        Type tasksType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(client.load("tasks"), tasksType);
        if (tasks != null) {
            tasks.forEach(task -> {
                int id = task.getId();
                this.tasks.put(id, task);
                this.prioritizedTasks.add(task);
                if (id > this.id) {
                    this.id = id;
                }
            });
        }

        Type subtasksType = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> subtasks = gson.fromJson(client.load("subtasks"), subtasksType);
        if (subtasks != null) {
            subtasks.forEach(subtask -> {
                int id = subtask.getId();
                this.subtasks.put(id, subtask);
                this.prioritizedTasks.add(subtask);
                if (id > this.id) {
                    this.id = id;
                }
            });
        }

        Type epicsType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(client.load("epics"), epicsType);
        if (epics != null) {
            epics.forEach(epic -> {
                int id = epic.getId();
                this.epics.put(id, epic);
                this.prioritizedTasks.add(epic);
                if (id > this.id) {
                    this.id = id;
                }
            });
        }

        Type historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(client.load("history"), historyType);

        if (history != null) {
            for (Task task:history) {
                inMemoryHistoryManager.add(this.findTask(task.getId()));
            }
        }
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(getTasks());
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(getEpics());
        client.put("epics", jsonEpics);
        String jsonSubTask = gson.toJson(getSubtasks());
        client.put("subtasks" , jsonSubTask);
        String jsonHistoryView = gson.toJson(getHistory());
        client.put("history" , jsonHistoryView);
    }

    protected Task findTask(Integer id) {
        if (tasks.get(id) != null) {
            return tasks.get(id);
        }
        if (epics.get(id) != null) {
            return epics.get(id);
        }
        return subtasks.get(id);
    }
}