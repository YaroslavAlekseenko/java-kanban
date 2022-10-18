package managers;

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
}