import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        System.out.println("\n\nТестируем менеджер истории спринт №5\n");

        Epic epic1 = new Epic("Epic №1", "Переезд");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Subtask 1", "Собрать коробки", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Subtask 2", "Упаковать кошку", Status.DONE, epic1);
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Subtask 3", "Погрузить вещи в авто", Status.DONE, epic1);
        manager.addSubtask(subtask13);

        Epic epic2 = new Epic("Epic №2", "Организовать большой семейный праздник");
        manager.addEpic(epic2);

        manager.getEpic(1);
        manager.getSubtask(2);
        manager.getSubtask(3);
        manager.getSubtask(4);
        manager.getEpic(5);
        System.out.println("\nИстория просмотров = " + manager.getHistory());
        manager.getEpic(5);
        manager.getSubtask(4);
        manager.getSubtask(3);
        manager.getSubtask(2);
        manager.getEpic(1);
        System.out.println("\nИстория просмотров = " + manager.getHistory());
        manager.deleteAllSubtasks();
        System.out.println("\nИстория просмотров = " + manager.getHistory());
        manager.deleteAllEpics();
        System.out.println("\nИстория просмотров = " + manager.getHistory());
    }
}
