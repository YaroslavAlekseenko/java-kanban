import managers.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        System.out.println("\n\nТестируем спринт №6\n");

        File file = new File("src/data/data.csv");

        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedTasksManager();

        Epic epic1 = new Epic("Epic №1", "Переезд");
        fileBackedTasksManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Subtask 1", "Собрать коробки", Status.IN_PROGRESS, epic1);
        fileBackedTasksManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 2", "Упаковать кошку", Status.DONE, epic1);
        fileBackedTasksManager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Subtask 3", "Погрузить вещи в авто", Status.NEW, epic1);
        fileBackedTasksManager.addSubtask(subtask13);

        Epic epic2 = new Epic("Epic №2", "Организовать большой семейный праздник");
        fileBackedTasksManager.addEpic(epic2);

        Task task1 = new Task("Task 1", "Выполнить спринт 6", Status.DONE);
        fileBackedTasksManager.addTask(task1);

        fileBackedTasksManager.getEpic(1);
        fileBackedTasksManager.getSubtask(2);
        fileBackedTasksManager.getSubtask(3);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getEpic(5);
        fileBackedTasksManager.getTask(6);
        System.out.println("\nИстория просмотров = " + fileBackedTasksManager.getHistory());

        FileBackedTasksManager.loadFromFile(file);
     }
}
