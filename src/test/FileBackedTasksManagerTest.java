package test;

import managers.FileBackedTasksManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    File file = new File("data.csv");
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
    }

    @Test
    public void testLoadDataFromFile() {
        Task task = new Task(1,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
        manager.addTask(task);
        Epic epic = new Epic(2,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
        manager.addEpic(epic);
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(task, manager.getTasks(),"Загрузка из файла не работает.");
        assertEquals(epic, manager.getEpics(), "Загрузка из файла не работает.");
    }

    @Test
    public void testLoadDataFromFileEmpty() {
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_MAP, manager.getTasks(),"Загрузка из файла не работает.");
        assertEquals(Collections.EMPTY_MAP, manager.getEpics(), "Загрузка из файла не работает.");
    }
}
