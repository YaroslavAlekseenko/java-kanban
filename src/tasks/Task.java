package tasks;

/** Класс задачи.
 * Простейшим элементом трекера является задача (англ. task).
 */

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;

    /** Конструктор задачи.
     * @param title Название, кратко описывающее суть задачи (например, «Переезд»).
     * @param description Описание, в котором раскрываются детали задачи.
     * @param status Статус, отображающий прогресс задачи. Этапы жизни задачи:
     * NEW — задача только создана, но к её выполнению ещё не приступили.
     * IN_PROGRESS — над задачей ведётся работа.
     * DONE — задача выполнена.
     */
    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
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

    @Override
    public String toString() {
        return "Task{" +
                "№=" + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }
}