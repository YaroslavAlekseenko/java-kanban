package managers;

import tasks.Task;

import java.util.List;

/**  Интерфейс менеджера истории просмотров.
 * Отвечает за список методов, которые должны быть у любого объекта-менеджера истории просмотров.
 */

public interface HistoryManager {

    /** Добавление просмотров задач. */
    void add(Task task);

    /** Удаление задачи из просмотра. */
    void remove(int id);

    /** Вызов истории просмотров задач. */
    List<Task> getHistory();

}