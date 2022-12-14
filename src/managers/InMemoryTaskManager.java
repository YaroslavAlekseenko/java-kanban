package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

import static tasks.Status.NEW;

/** Класс менеджера.
 * Запускается на старте программы и управляет всеми задачами.
 */
public class InMemoryTaskManager implements TaskManager{
    protected int id;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager;
    protected Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

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
        addPrioritizedTask(task);
    }

    /** Обновление данных существующей задачи. */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        addPrioritizedTask(task);
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
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    /** Удаление задачи по идентификатору. */
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    /** Удаление всех задач. */
    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
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
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    /** Удаление эпика и его подзадач по идентификатору. */
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);
            for (Integer subtaskId : epic.getEpicSubtaskIDs()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epic.setEpicSubtaskIDs(new ArrayList<>());
        }
    }

    /** Удаление всех эпиков. */
    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
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
        updateTimeEpic(subtask.getEpic());
        addPrioritizedTask(subtask);
        }
    }

    /** Обновление данных существующей подзадачи. */
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic());
        updateTimeEpic(subtask.getEpic());
        addPrioritizedTask(subtask);
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
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    /** Удаление подзадачи по идентификатору. */
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtaskIDs().remove((Integer) id);
            updateEpicStatus(epic);
            updateTimeEpic(epic);
            subtasks.remove(id);
            historyManager.remove(id);
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
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            updateEpicStatus(epic);
            updateTimeEpic(epic);
        }
    }

    /** Проверка и изменение статуса эпика. */
    private void updateEpicStatus(Epic epic) {

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

    /** Изменение времени эпика. */
    public void updateTimeEpic(Epic epic) {
            List<Subtask> subtasks = getEpicsSubtasks(epic.getId());
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;
            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
                if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            long duration = ((long) (endTime.getYear() - startTime.getYear()) * 365 * 24 * 60 + (endTime.getDayOfYear() - startTime.getDayOfYear()) * 24 * 60 + (endTime.getHour() - startTime.getHour()) * 60 + (endTime.getMinute() - startTime.getMinute()));
            epic.setDuration(duration);
    }

    /** Добавление задач в список в порядке приоритета. */
    public void addPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        checkTaskPriority();
    }

    /** Проверка времени. */
    public boolean doesTimeOverlap(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        for (Task prioritizedTask : tasks) {
            if (task.getStartTime().isBefore(prioritizedTask.getStartTime())
                    && task.getEndTime().isAfter(prioritizedTask.getStartTime())) {
                return true;
            } else if (task.getStartTime().isBefore(prioritizedTask.getEndTime())
                    && task.getEndTime().isAfter(prioritizedTask.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    /** Проверка задач на пересечение. */
    private void checkTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();
        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (doesTimeOverlap(task)) {
                throw new ManagerCheckException(
                        "Задача №" + task.getId() + " пересекается с текущими задачами, нужно изменить время");
            }
        }
    }

    /** Извлечение списка задач в порядке приоритета. */
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}