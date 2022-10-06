import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

/** Класс менеджера.
 * Запускается на старте программы и управляет всеми задачами.
 */

public class Manager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    /** Добавление новой задачи. */
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    /** Обновление данных существующей задачи. */
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /** Извлечение задачи по идентификатору. */
    public Task getTask(int id) {
        return tasks.getOrDefault(id, null);
    }

    /** Извлечение списка задач. */
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    /** Удаление задачи по идентификатору. */
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    /** Удаление всех задач. */
    public void deleteAllTasks() {
        tasks.clear();
    }

    /** Добавление нового эпика. */
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus("New");
        epics.put(id, epic);
    }

    /** Извлечение эпика по идентификатору. */
    public Epic getEpic(int id) {
        return epics.getOrDefault(id, null);
    }

    /** Извлечение списка эпиков. */
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    /** Удаление эпика и его подзадач по идентификатору. */
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epic.setEpicSubtasks(new ArrayList<>());
        }
    }

    /** Удаление всех эпиков. */
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    /** Добавление новой подзадачи. */
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        subtask.getEpic().getEpicSubtasks().add(id);
        checkEpicStatus(subtask.getEpic());
    }

    /** Обновление данных существующей подзадачи. */
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkEpicStatus(subtask.getEpic());
    }

    /** Извлечение подзадачи по идентификатору. */
    public Subtask getSubtask(int id) {
        return subtasks.getOrDefault(id,null);
    }

    /** Извлечение списка подзадач. */
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    /** Удаление подзадачи по идентификатору. */
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            checkEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    /** Удаление всех подзадач. */
    public void deleteAllSubtasks() {
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setEpicSubtasks(new ArrayList<>());
            if (!epicsForStatusUpdate.contains(subtask.getEpic())) {
                epicsForStatusUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            epic.setStatus("NEW");
        }
    }

    /** Проверка статуса эпика. */
    private void checkEpicStatus(Epic epic) {

        if (epic.getEpicSubtasks().size() == 0) {
            epic.setStatus("NEW");
            return;
        }

        boolean allSubtaskIsNew = true;
        boolean allSubtaskIsDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubtasks()) {
            String status = subtasks.get(epicSubtaskId).getStatus();
            if (!status.equals("NEW")) {
                allSubtaskIsNew = false;
            }
            if (!status.equals("DONE")) {
                allSubtaskIsDone = false;
            }
        }

        if (allSubtaskIsDone) {
            epic.setStatus("DONE");
        } else if (allSubtaskIsNew) {
            epic.setStatus("NEW");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}