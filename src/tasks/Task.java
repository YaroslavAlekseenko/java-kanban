package tasks;

import managers.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

/** Класс задачи.
 * Простейшим элементом трекера является задача (англ. task).
 */

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected TaskType taskType;
    protected LocalDateTime startTime;
    protected long duration;

    /** Конструктор задачи.
     * @param id Уникальный идентификатор задачи.
     * @param title Название, кратко описывающее суть задачи (например, «Переезд»).
     * @param description Описание, в котором раскрываются детали задачи.
     * @param status Статус, отображающий прогресс задачи. Этапы жизни задачи:
     * NEW — задача только создана, но к её выполнению ещё не приступили.
     * IN_PROGRESS — над задачей ведётся работа.
     * DONE — задача выполнена.
     * @param startTime Дата, когда предполагается приступить к выполнению задачи.
     * @param duration Продолжительность задачи, оценка того, сколько времени она займёт в минутах.
     */
    public Task(int id, String title, String description, Status status, LocalDateTime startTime, long duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status, LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }

        @Override
    public String toString() {
        return "Task{" +
                "№=" + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                ", Старт задачи='" + startTime + '\'' +
                ", Конец задачи='" + getEndTime() + '\'' +
                ", Продолжительность выполнения='" + duration +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, startTime, duration, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!Objects.equals(title, task.title)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (!Objects.equals(startTime, task.startTime)) return false;
        if (!Objects.equals(duration, task.duration)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }
}