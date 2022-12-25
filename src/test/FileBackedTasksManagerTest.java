package test;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("data.test.csv");
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(file);
    }

    /** Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния.*/
    @Test
    public void testLoadDataFromFile() {
        Task task = new Task(1,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
        manager.addTask(task);
        Epic epic = new Epic(2,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
        manager.addEpic(epic);
        FileBackedTasksManager.loadFromFile(file);

        assertNotNull(manager.getTasks(), "Задачи не возвращаются.");
        assertNotNull(manager.getEpics(), "Задачи не возвращаются.");

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");

        assertEquals(task, manager.getTask(1),"Загрузка из файла не работает.");
        assertEquals(epic, manager.getEpic(2), "Загрузка из файла не работает.");
    }

    /** Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. Пустой список задач.*/
    @Test
    public void testLoadDataFromFileEmpty() {
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_MAP, manager.getTasks(),"Загрузка из файла не работает.");
        assertEquals(Collections.EMPTY_MAP, manager.getEpics(), "Загрузка из файла не работает.");
    }

    /** Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. Эпик без подзадач.*/
    @Test
    public void testLoadDataFromFileEpicWithoutSubtasks() {
        Epic epic = new Epic(1,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
        manager.addEpic(epic);
        FileBackedTasksManager.loadFromFile(file);

        assertNotNull(manager.getEpics(), "Задачи не возвращаются.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        assertEquals(epic, manager.getEpic(1),"Загрузка из файла не работает.");
        assertEquals(Collections.EMPTY_MAP, manager.getSubtasks(), "Загрузка из файла не работает.");
    }

    /** Для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. Пустой список истории.*/
    @Test
    public void testLoadDataFromFileEmptyHistory() {
        Task task = new Task(1,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
        manager.addTask(task);
        Epic epic = new Epic(2,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
        manager.addEpic(epic);
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, manager.getHistory(), "Загрузка из файла не работает.");
    }
}

