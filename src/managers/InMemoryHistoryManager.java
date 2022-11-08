package managers;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Класс менеджера истории просмотров.
 * Реализует создание и просмотр истории вызванных задач.
 */

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> receivedTasks;
    public Node head;
    public Node tail;

    public InMemoryHistoryManager() {
        this.receivedTasks = new HashMap<>();
    }

    /**  Реализация метода linkLast двусвязного списка. */
    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        receivedTasks.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    /**  Реализация метода getTasks двусвязного списка. */
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (!(currentNode == null)) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    /**  Реализация метода removeNode двусвязного списка. */
    public void removeNode(Node node) {
        if (!(node == null)) {
            final Node next = node.next;
            final Node prev = node.prev;
            node.data = null;
            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    /** Добавление просмотров задач. */
    @Override
    public void add(Task task) {
        if (!(task == null)) {
            remove(task.getId());
            linkLast(task);
        }
    }

    /** Удаление просмотров задач. */
    @Override
    public void remove(int id) {
        removeNode(receivedTasks.get(id));
    }

    /** Вызов истории просмотров задач. */
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}

/** Узел связного списка. */
class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
