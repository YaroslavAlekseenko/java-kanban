package managers;

import java.io.File;

/** Утилитарный класс.
 * Отвечает за создание менеджера задач.
 */

public class Managers {

    /** Возврат объекта реализующего интерфейс TaskManager */
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    /** Возврат объекта реализующего интерфейс HistoryManager */
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    /** Возврат объекта реализующего FileBackedTasksManager */
    public static FileBackedTasksManager getDefaultFileBackedTasksManager(){
        return new FileBackedTasksManager(new File("src/data/data.csv"));
    }
}