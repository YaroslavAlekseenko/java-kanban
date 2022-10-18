package managers;

import tasks.Task;

import java.util.List;

/**  Интерфейс менеджера истории просмотров.
 * Отвечает за список методов, которые должны быть у любого объекта-менеджера истории просмотров.
 */

public interface HistoryManager {

    /** Добавление просмотров задач. */
    void add(Task task);

    /** Вызов истории просмотров задач. */
    List<Task> getHistory();

}