package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static tasks.Status.NEW;

/** Класс менеджера.
 * Запускается на старте программы и управляет всеми задачами.
 */

public class InMemoryTaskManager implements TaskManager{
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;


    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    /** Вызов истории просмотров задач. */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /** Добавление новой задачи. */
    @Override
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    /** Обновление данных существующей задачи. */
    @Override
    public void updateTask(Task task) {
            tasks.put(task.getId(), task);
    }

    /** Извлечение задачи по идентификатору. */
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return tasks.getOrDefault(id, null);
    }

    /** Извлечение списка задач. */
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /** Удаление задачи по идентификатору. */
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    /** Удаление всех задач. */
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    /** Добавление нового эпика. */
    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus(NEW);
        epics.put(id, epic);
    }

    /** Обновление данных существующего эпика. */
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /** Извлечение эпика по идентификатору. */
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epics.getOrDefault(id, null);
    }

    /** Извлечение списка эпиков. */
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /** Удаление эпика и его подзадач по идентификатору. */
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getEpicSubtaskIDs()) {
                subtasks.remove(subtaskId);
            }
            epic.setEpicSubtaskIDs(new ArrayList<>());
        }
    }

    /** Удаление всех эпиков. */
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    /** Добавление новой подзадачи. */
    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        if (subtask.getEpic() != null) {
        subtask.getEpic().getEpicSubtaskIDs().add(id);
        updateEpicStatus(subtask.getEpic());
        }
    }

    /** Обновление данных существующей подзадачи. */
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic());
    }

    /** Извлечение подзадачи по идентификатору. */
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtasks.getOrDefault(id,null);
    }

    /** Извлечение списка подзадач по идентификатору эпика. */
    @Override
    public List<Subtask> getEpicsSubtasks(int id) {
        ArrayList<Subtask> EpicsSubtasks = new ArrayList<>();
        if(epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> epicsSubtasks = epic.getEpicSubtaskIDs();
            for (Integer epicsSubtask : epicsSubtasks) {
                EpicsSubtasks.add(subtasks.get(epicsSubtask));
            }
        }
        return EpicsSubtasks;
    }

    /** Извлечение списка подзадач. */
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /** Удаление подзадачи по идентификатору. */
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtaskIDs().remove((Integer) id);
            updateEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    /** Удаление всех подзадач. */
    @Override
    public void deleteAllSubtasks() {
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setEpicSubtaskIDs(new ArrayList<>());
            if (!epicsForStatusUpdate.contains(subtask.getEpic())) {
                epicsForStatusUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            updateEpicStatus(epic);
        }
    }

    /** Проверка и изменение статуса эпика. */
    @Override
    public void updateEpicStatus(Epic epic) {

        if (epic.getEpicSubtaskIDs().size() == 0) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allSubtaskIsNew = true;
        boolean allSubtaskIsDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubtaskIDs()) {
            Status status = subtasks.get(epicSubtaskId).getStatus();
            if (!status.equals(Status.NEW)) {
                allSubtaskIsNew = false;
            }
            if (!status.equals(Status.DONE)) {
                allSubtaskIsDone = false;
            }
        }

        if (allSubtaskIsDone) {
            epic.setStatus(Status.DONE);
        } else if (allSubtaskIsNew) {
            epic.setStatus(NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}