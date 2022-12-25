package test;

import managers.HistoryManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager manager;

    protected Task addTask() {
        return new Task(1,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
    }

    protected Epic addEpic() {
        return new Epic(2,"Test addNewEpic", "Test addNewEpic description", LocalDateTime.now().plusHours(0), 0);
    }


    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultHistory();
    }

    /** Для HistoryManager — тесты для всех методов интерфейса. Пустая история задач.*/
    @Test
    public void testAddTasksHistoryEmpty() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory(), "История не пустая.");
    }

    /** Для HistoryManager — тесты для всех методов интерфейса. Дублирование.*/
    @Test
    public void testAddTasksHistoryDouble() {
        Task task = addTask();
        manager.add(task);
        Epic epic = addEpic();
        manager.add(epic);
        Task task1 = addTask();
        manager.add(task1);

        assertNotNull(manager.getHistory(), "История пустая.");
        assertNotEquals(List.of(task, epic, task1), manager.getHistory(), "Задачи дублируются.");
    }

    /** Для HistoryManager — тесты для всех методов интерфейса. Удаление из истории: начало, середина, конец.*/
    @Test
    public void testAddTasksHistoryDelete() {
        Task task = addTask();
        manager.add(task);
        Task task1 = new Task(3,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
        manager.add(task1);
        Task task2 = new Task(4,"Test addNewTask", "Test addNewTask description", Status.NEW, LocalDateTime.now().plusHours(1), 10);
        manager.add(task2);
        Epic epic = addEpic();
        manager.add(epic);

        assertNotNull(manager.getHistory(), "История пустая.");
        assertEquals(List.of(task, task1, task2, epic), manager.getHistory(), "Ошибка добавления в историю");

        manager.remove(3);

        assertNotNull(manager.getHistory(), "История пустая.");
        assertEquals(List.of(task, task2, epic), manager.getHistory(), "Задача из истории не удалена.");

        manager.remove(1);

        assertNotNull(manager.getHistory(), "История пустая.");
        assertEquals(List.of(task2, epic), manager.getHistory(), "Задача из истории не удалена.");


        manager.remove(2);

        assertNotNull(manager.getHistory(), "История пустая.");
        assertEquals(List.of(task2), manager.getHistory(), "Задача из истории не удалена.");
    }


}
