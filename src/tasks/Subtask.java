package tasks;

import managers.TaskType;

/** Класс подзадачи.
 * Для выполнения масштабной задачи её лучше разбить на подзадачи (англ. subtask).
 * Для каждой подзадачи известно, в рамках какого эпика она выполняется.
 */

public class Subtask extends Task {
    private final Epic epicId;

    /** Конструктор подзадачи.
     * @param title Название, кратко описывающее суть подзадачи.
     * @param description Описание, в котором раскрываются детали подзадачи.
     * @param status Статус, отображающий прогресс подзадачи. Этапы жизни подзадачи:
     * NEW — подзадача только создана, но к её выполнению ещё не приступили.
     * IN_PROGRESS — над подзадачей ведётся работа.
     * DONE — подзадача выполнена.
     * @param epicId Уникальный идентификационный номер эпика подзадачи.
     */

    public Subtask(String title, String description, Status status, Epic epicId) {
        super(title, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String description, Status status, Epic epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Epic getEpic() {
        return epicId;
    }

    @Override
    public String toString() {
        return  "Subtask{" +
                "№=" + epicId +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, epicId.getId());
    }
}