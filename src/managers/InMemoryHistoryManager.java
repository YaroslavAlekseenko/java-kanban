package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

/** Класс менеджера истории просмотров.
 * Реализует создание и просмотр истории вызванных задач.
 */

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> taskHistory;

    public InMemoryHistoryManager() {
        taskHistory = new ArrayList<>();
    }

    /** Добавление просмотров задач. */
    @Override
    public void add(Task task) {
        if (!(task == null)) {
            taskHistory.add(task);
            if (taskHistory.size() > 10) {
                taskHistory.remove(0);
            }
        }
    }

    /** Вызов истории просмотров задач. */
    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}