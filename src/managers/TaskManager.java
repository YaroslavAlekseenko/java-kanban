package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

/**  Интерфейс менеджера задач.
* Отвечает за список методов, которые должны быть у любого объекта-менеджера.
 */

public interface TaskManager {

    /** Вызов истории просмотров задач. */
    List<Task> getHistory();

    /** Добавление новой задачи. */
    void addTask(Task task);

    /** Обновление данных существующей задачи. */
    void updateTask(Task task);

    /** Извлечение задачи по идентификатору. */
    Task getTask(int id);

    /** Извлечение списка задач. */
    HashMap<Integer, Task> getTasks();

    /** Удаление задачи по идентификатору. */
    void deleteTask(int id);

    /** Удаление всех задач. */
    void deleteAllTasks();

    /** Добавление нового эпика. */
    void addEpic(Epic epic);

    /** Обновление данных существующего эпика. */
    void updateEpic(Epic epic);

    /** Извлечение эпика по идентификатору. */
    Epic getEpic(int id);

    /** Извлечение списка эпиков. */
    HashMap<Integer, Epic> getEpics();

    /** Удаление эпика и его подзадач по идентификатору. */
    void deleteEpic(int id);

    /** Удаление всех эпиков. */
    void deleteAllEpics();

    /** Добавление новой подзадачи. */
    void addSubtask(Subtask subtask);

    /** Обновление данных существующей подзадачи. */
    void updateSubtask(Subtask subtask);

    /** Извлечение подзадачи по идентификатору. */
    Subtask getSubtask(int id);

    /** Извлечение списка подзадач по идентификатору эпика. */
    List<Subtask> getEpicsSubtasks(int id);

    /** Извлечение списка подзадач. */
    HashMap<Integer, Subtask> getSubtasks();

    /** Удаление подзадачи по идентификатору. */
    void deleteSubtask(int id);


    /** Удаление всех подзадач. */
    void deleteAllSubtasks();
}
