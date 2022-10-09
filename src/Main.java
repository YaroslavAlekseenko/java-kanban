import managers.Manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        System.out.println("\n\nТестируем добавление и печать списков эпиков, задач и подзадач:\n");

        Manager manager = new Manager();

        Task task1 = new Task("Task 1", "Помыть посуду", "NEW");
        manager.addTask(task1);
        Task task2 = new Task("Task 2", "Позвонить бабушке", "DONE");
        manager.addTask(task2);

        Epic epic1 = new Epic("Epic №1", "Переезд");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Subtask 1", "Собрать коробки", "IN_PROGRESS", epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 2", "Упаковать кошку", "DONE", epic1);
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Epic №2", "Организовать большой семейный праздник");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Subtask 1", "Купить квартиру", "DONE", epic2);
        manager.addSubtask(subtask21);

        System.out.println("\nTasks = " + manager.getTasks());
        System.out.println("\nEpics = " + manager.getEpics());
        System.out.println("\nSubtasks = " + manager.getSubtasks());


        System.out.println("\n\nТестируем получение списка подзадач по идентификатору эпика\n");

        System.out.println("\nSubtasks = " + manager.getEpicsSubtasks(3));


        System.out.println("\n\nТестируем получение задачи по идентификатору\n");

        System.out.println("\nTasks = " + manager.getTask(1));
        System.out.println("\nEpics = " + manager.getEpic(3));
        System.out.println("\nSubtasks = " + manager.getSubtask(4));


        System.out.println("\n\nТестируем изменение/обновление созданных объектов\n");

        Task task1NewStatus = new Task("Task 1", "Помыть посуду", "DONE");
        task1NewStatus.setId(1);
        manager.updateTask(task1NewStatus);

        Epic epic1NewStatus = new Epic("Epic №1", "Организация переезда");
        epic1NewStatus.setId(3);
        manager.updateEpic(epic1NewStatus);

        Subtask subtask11NewStatus = new Subtask("Subtask 2", "Собрать коробки", "DONE", epic1);
        subtask11NewStatus.setId(4);
        manager.updateSubtask(subtask11NewStatus);

        System.out.println("\nTasks = " + manager.getTasks());
        System.out.println("\nEpics = " + manager.getEpics());
        System.out.println("\nSubtasks = " + manager.getSubtasks());


        System.out.println("\n\nТестируем удаление задач по идентификатору\n");

        manager.deleteTask(2);
        manager.deleteEpic(6);
        manager.deleteSubtask(5);

        System.out.println("\nTasks = " + manager.getTasks());
        System.out.println("\nEpics = " + manager.getEpics());
        System.out.println("\nSubtasks = " + manager.getSubtasks());


        System.out.println("\n\nТестируем удаление всех задач\n");

        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        System.out.println("\nTasks = " + manager.getTasks());
        System.out.println("\nEpics = " + manager.getEpics());
        System.out.println("\nSubtasks = " + manager.getSubtasks());
    }
}
