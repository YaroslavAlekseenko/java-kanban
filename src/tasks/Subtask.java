package tasks;

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

    public Subtask(String title, String description, String status, Epic epicId) {
        super(title, description, status);
        this.epicId = epicId;
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
}