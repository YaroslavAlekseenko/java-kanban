package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Класс менеджера.
 * Запускается на старте программы, управляет всеми задачами и хранит информацию в файле.
 */

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected final File file;

    /** Создание файла. */
    public FileBackedTasksManager(HistoryManager defaultHistory, File file) {
        this.file = file;
        String fileName = "src/data/data.csv";
        file = new File(fileName);
        if (!file.isFile()) {
            try {
                Files.createFile(Path.of(fileName));
            } catch (IOException e) {
                System.out.println("Ошибка создания файла.");
            }
        }
    }

    /** Восстановление данных менеджера из файла при запуске программы. */
    public static void loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        String data;
        try {
            Path pathToFile = Paths.get("src/data/data.csv");
            data = Files.readString(pathToFile.toAbsolutePath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        String[] lines = data.split("\n");
        List<String> epics = new ArrayList<>();
        List<String> subtasks = new ArrayList<>();
        List<String> tasks = new ArrayList<>();
        String lineOfHistory = "";
        boolean isTitle = true;
        boolean itsTask = true;
        for (String line : lines) {
            if (isTitle) {
                isTitle = false;
                continue;
            }
            if (line.isEmpty() || line.equals("\r")) {
                itsTask = false;
                continue;
            }
            if (itsTask) {
                TaskType taskType = TaskType.valueOf(line.split(",")[1]);
                switch (taskType) {
                    case EPIC:
                        epics.add(line);
                        break;
                    case SUBTASK:
                        subtasks.add(line);
                        break;
                    case TASK:
                        tasks.add(line);
                        break;
                }
            } else {
                lineOfHistory = line;
            }
        }
        int maxId = 0;
        for (String epicLine : epics) {
            Epic epic = (Epic) fromString(epicLine, TaskType.EPIC, fileBackedTasksManager);
            assert epic != null;
            int id = epic.getId();
            if (id > maxId) {
                maxId = id;
            }
            fileBackedTasksManager.epics.put(id, epic);
        }
        for (String subtaskLine : subtasks) {
            Subtask subtask = (Subtask) fromString(subtaskLine, TaskType.SUBTASK, fileBackedTasksManager);
            assert subtask != null;
            fileBackedTasksManager.prioritizedTasks.add(subtask);
            int id = subtask.getId();
            if (id > maxId) {
                maxId = id;
            }
            fileBackedTasksManager.subtasks.put(id, subtask);
            subtask.getEpic().getEpicSubtaskIDs().add(id);
        }
        for (String taskLine : tasks) {
            Task task = fromString(taskLine, TaskType.TASK, fileBackedTasksManager);
            assert task != null;
            fileBackedTasksManager.prioritizedTasks.add(task);
            int id = task.getId();
            if (id > maxId) {
                maxId = id;
            }
            fileBackedTasksManager.tasks.put(id, task);
        }
        fileBackedTasksManager.id = maxId;
        List<Integer> ids = fromString(lineOfHistory);
        for (Integer taskId : ids) {
            fileBackedTasksManager.historyManager.add(getTaskAll(taskId, fileBackedTasksManager));
        }
    }

    private static Task getTaskAll(int id, InMemoryTaskManager inMemoryTaskManager) {
        Task task = inMemoryTaskManager.getTasks().get(id);
        if (!(task == null)) {
            return task;
        }
        Task epic = inMemoryTaskManager.getEpics().get(id);
        if (!(epic == null)) {
            return epic;
        }
        Task subtask = inMemoryTaskManager.getSubtasks().get(id);
        if (!(subtask == null)) {
            return subtask;
        }
        return null;
    }

    /** Сохранение менеджера истории в CSV. */
    private static String toString(HistoryManager manager) {
        List<String> s = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            s.add(String.valueOf(task.getId()));
        }
        return String.join(",", s);
    }

    /** Восстановление менеджера истории из CSV. */
    private static List<Integer> fromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksId = new ArrayList<>();
        for (String idString : idsString) {
            tasksId.add(Integer.valueOf(idString));
        }
        return tasksId;
    }

    /** Создание задачи из строки. */
    private static Task fromString(String value, TaskType taskType, FileBackedTasksManager fileBackedTasksManager) {
        String[] dataOfTask = value.split(",", 8);
        int id = Integer.parseInt(dataOfTask[0]);
        String name = dataOfTask[2];
        Status status = Status.valueOf(dataOfTask[3]);
        String description = dataOfTask[4];
        LocalDateTime startTime = LocalDateTime.parse(dataOfTask[5]);
        long duration = Long.parseLong(dataOfTask[6]);
        String epicIdString = (dataOfTask[7].trim());
        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status, startTime, duration);
            case SUBTASK:
                return new Subtask(id, name, description, status, startTime, duration, fileBackedTasksManager.epics.get(Integer.valueOf(epicIdString)));
            case EPIC:
                return new Epic(id, name, status, description, startTime, duration);
            default:
                return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    /** Сохранение текущего состояния менеджера в указанный файл. */
    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic,startTime,duration\n");
            HashMap<Integer, String> allTasks = new HashMap<>();
            HashMap<Integer, Task> tasks = super.getTasks();
            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toStringFromFile());
            }
            HashMap<Integer, Subtask> subtasks = super.getSubtasks();
            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toStringFromFile());
            }
            HashMap<Integer, Epic> epics = super.getEpics();
            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toStringFromFile());
            }
            for (String value : allTasks.values()) {
                writer.write(String.format("%s\n", value));
            }
            writer.write("\n");
            writer.write(toString(this.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла.");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}