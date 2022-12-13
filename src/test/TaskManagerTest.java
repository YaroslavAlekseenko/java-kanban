package test;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import tasks.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task addTask() {
        return new Task(1,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
    }
    protected Epic addEpic() {
        return new Epic(2,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
    }
    protected Subtask addSubtask() {
        return new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description", Status.NEW, LocalDateTime.now().plusHours(2), 10, manager.getEpic(1));
    }

    /** Для расчёта статуса Epic. Пустой список подзадач.*/
    @Test
    public void CheckStatusEmpty() {
        Epic epic = addEpic();
        manager.addEpic(epic);

        assertEquals(Status.NEW, manager.getEpic(epic.getId()).getStatus(), "Ошибка расчёта статуса Epic.");
    }

    /** Для расчёта статуса Epic. Все подзадачи со статусом NEW.*/
    @Test
    public void CheckStatusNew() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(2,"Test addNewSubtask", "Test addNewSubtask description", Status.NEW, LocalDateTime.now().plusHours(20), 100, manager.getEpic(1)));
        manager.addSubtask(new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description", Status.NEW, LocalDateTime.now().plusHours(30), 100, manager.getEpic(1)));

        assertEquals(Status.NEW, manager.getEpic(epic.getId()).getStatus(), "Ошибка расчёта статуса Epic.");
    }

    /** Для расчёта статуса Epic. Все подзадачи со статусом DONE.*/
    @Test
    public void CheckStatusDone() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(2,"Test addNewSubtask", "Test addNewSubtask description", Status.DONE, LocalDateTime.now().plusHours(20), 100, manager.getEpic(1)));
        manager.addSubtask(new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description", Status.DONE, LocalDateTime.now().plusHours(30), 100, manager.getEpic(1)));

        assertEquals(Status.DONE, manager.getEpic(epic.getId()).getStatus(), "Ошибка расчёта статуса Epic.");
    }

    /** Для расчёта статуса Epic. Подзадачи со статусами NEW и DONE.*/
    @Test
    public void CheckStatusNewDone() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(2,"Test addNewSubtask", "Test addNewSubtask description", Status.NEW, LocalDateTime.now().plusHours(20), 100, manager.getEpic(1)));
        manager.addSubtask(new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description", Status.DONE, LocalDateTime.now().plusHours(30), 100, manager.getEpic(1)));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus(), "Ошибка расчёта статуса Epic.");
    }

    /** Для расчёта статуса Epic. Подзадачи со статусом IN_PROGRESS.*/
    @Test
    public void CheckStatusInProgress() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(2,"Test addNewSubtask", "Test addNewSubtask description", Status.IN_PROGRESS, LocalDateTime.now().plusHours(20), 100, manager.getEpic(1)));
        manager.addSubtask(new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description", Status.IN_PROGRESS, LocalDateTime.now().plusHours(30), 100, manager.getEpic(1)));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus(), "Ошибка расчёта статуса Epic.");
    }

    // Для двух менеджеров задач InMemoryTasksManager и FileBackedTasksManager. Проверка методов.
    @Test
    public void testAddTask() {
        Task task = addTask();
        manager.addTask(task);

        final Task savedTask = manager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void testAddEpic() {
        Epic epic = addEpic();
        manager.addEpic(epic);

        final Epic savedEpic = manager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epic, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(1), "Задачи не совпадают.");
    }

    @Test
    public void testAddSubtask() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        Subtask subtask = addSubtask();
        manager.addSubtask(subtask);

        final Task savedSubtask = manager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(2), "Задачи не совпадают.");
    }

    @Test
    public void testUpdateTask() {
        Task task = addTask();
        manager.addTask(task);

        manager.updateTask(new Task(1,"Test addNewTask", "Test addNewTask description", Status.DONE, LocalDateTime.now().plusHours(10), 100));

        final Task savedTask = manager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertNotEquals(task, savedTask, "Задача не обновилась.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertNotEquals(task, tasks.get(1), "Задача не обновилась.");
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = addEpic();
        manager.addEpic(epic);

        manager.updateEpic(new Epic(1,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(10), 10));

        final Epic savedEpic = manager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertNotEquals(epic, savedEpic, "Задача не обновилась.");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertNotEquals(epic, epics.get(1), "Задача не обновилась.");
    }

    @Test
    public void testUpdateSubtask() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        Subtask subtask = addSubtask();
        manager.addSubtask(subtask);

        manager.updateSubtask(new Subtask(2,"Test addNewSubtask", "Test addNewSubtask description", Status.DONE, LocalDateTime.now().plusHours(20), 100, manager.getEpic(1)));

        final Subtask savedSubtask = manager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertNotEquals(subtask, savedSubtask, "Задача не обновилась.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertNotEquals(subtask, subtasks.get(1), "Задача не обновилась.");
    }

    @Test
    public void testDeleteTask() {
        Task task = addTask();
        manager.addTask(task);

        manager.deleteTask(task.getId());
        assertEquals(Collections.EMPTY_MAP, manager.getTasks(),"Задача не удалена.");
    }

    @Test
    public void testDeleteEpic() {
        Epic epic = addEpic();
        manager.addEpic(epic);

        manager.deleteEpic(epic.getId());
        assertEquals(Collections.EMPTY_MAP, manager.getEpics(),"Задача не удалена.");
    }

    @Test
    public void testDeleteSubtask() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        Subtask subtask = addSubtask();
        manager.addSubtask(subtask);

        manager.deleteSubtask(subtask.getId());
        assertEquals(Collections.EMPTY_MAP, manager.getSubtasks(),"Задача не удалена.");
    }

    @Test
    public void testDeleteAllTasks() {
        Task task = addTask();
        manager.addTask(task);

        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_MAP, manager.getTasks(),"Задачи не удалены.");
    }

    @Test
    public void testDeleteAllEpics() {
        Epic epic = addEpic();
        manager.addEpic(epic);

        manager.deleteAllEpics();
        assertEquals(Collections.EMPTY_MAP, manager.getEpics(),"Задачи не удалены.");
    }

    @Test
    public void testDeleteAllSubtasks() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        Subtask subtask = addSubtask();
        manager.addSubtask(subtask);

        manager.deleteAllSubtasks();
        assertEquals(Collections.EMPTY_MAP, manager.getSubtasks(),"Задачи не удалены.");
    }
}