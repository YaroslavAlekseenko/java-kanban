package tasks;

import managers.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;

/** Класс эпика.
 * Большая задача, которая делится на подзадачи, называется эпиком (англ. epic).
 * Каждый эпик знает, какие подзадачи в него входят.
 */

public class Epic extends Task {
    protected LocalDateTime endTime;
    private ArrayList<Integer> epicSubtaskIDs;

    /** Конструктор эпика.
     * @param id Уникальный идентификатор задачи.
     * @param title Название, кратко описывающее суть эпика.
     * @param description Описание, в котором раскрываются детали эпика.
     * @param startTime Дата, когда предполагается приступить к выполнению задачи.
     * @param duration Продолжительность задачи, оценка того, сколько времени она займёт в минутах.
     */
    public Epic(int id, String title, Status status, String description, LocalDateTime startTime, long duration) {
        super(id, title, description, null, startTime, duration);
        epicSubtaskIDs = new ArrayList<>();
        this.taskType = TaskType.EPIC;
        this.status = status;
        this.endTime = super.getEndTime();
    }

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        epicSubtaskIDs = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, String description, LocalDateTime startTime, long duration) {
        super(id, title, description, null, startTime, duration);
        epicSubtaskIDs = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(String title, String description, LocalDateTime startTime, long duration) {
        super(title, description, null, startTime, duration);
        epicSubtaskIDs = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public ArrayList<Integer> getEpicSubtaskIDs() {
        return epicSubtaskIDs;
    }

    public void setEpicSubtaskIDs(ArrayList<Integer> epicSubtaskIDs) {
        this.epicSubtaskIDs = epicSubtaskIDs;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "№=" + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                ", Старт задачи='" + startTime + '\'' +
                ", Конец задачи='" + getEndTime() + '\'' +
                ", Продолжительность выполнения='" + duration +
                '}';
    }
}