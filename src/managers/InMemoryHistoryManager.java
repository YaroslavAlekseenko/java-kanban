package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

/** Класс менеджера истории просмотров.
 * Реализует создание и просмотр истории вызванных задач.
 */

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> taskHistory;

    private final static byte MAXLOGHISTORY = 10;

    public InMemoryHistoryManager() {
        taskHistory = new LinkedList<>();
    }

    /** Добавление просмотров задач. */
    @Override
    public void add(Task task) {
        if (!(task == null)) {
            taskHistory.add(task);
            if (taskHistory.size() > MAXLOGHISTORY) {
                taskHistory.removeFirst();
            }
        }
    }

    /** Вызов истории просмотров задач. */
    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}