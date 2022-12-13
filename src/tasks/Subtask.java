package tasks;

import managers.TaskType;

import java.time.LocalDateTime;


/** Класс подзадачи.
 * Для выполнения масштабной задачи её лучше разбить на подзадачи (англ. subtask).
 * Для каждой подзадачи известно, в рамках какого эпика она выполняется.
 */

public class Subtask extends Task {
    private final Epic epicId;

    /** Конструктор подзадачи.
     * @param id Уникальный идентификатор задачи.
     * @param title Название, кратко описывающее суть подзадачи.
     * @param description Описание, в котором раскрываются детали подзадачи.
     * @param status Статус, отображающий прогресс подзадачи. Этапы жизни подзадачи:
     * NEW — подзадача только создана, но к её выполнению ещё не приступили.
     * IN_PROGRESS — над подзадачей ведётся работа.
     * DONE — подзадача выполнена.
     * @param startTime Дата, когда предполагается приступить к выполнению задачи.
     * @param duration Продолжительность задачи, оценка того, сколько времени она займёт в минутах.
     * @param epicId Уникальный идентификационный номер эпика подзадачи.
     */
    public Subtask(int id, String title, String description, Status status, LocalDateTime startTime, long duration, Epic epicId) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(String title, String description, Status status, LocalDateTime startTime, long duration, Epic epicId) {
        super(title, description, status, startTime, duration);
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
                ", Старт задачи='" + startTime + '\'' +
                ", Конец задачи='" + getEndTime() + '\'' +
                ", Продолжительность выполнения='" + duration +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, startTime, duration, epicId.getId());
    }
}