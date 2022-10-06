package tasks;

import java.util.ArrayList;

/** Класс эпика.
 * Большая задача, которая делится на подзадачи, называется эпиком (англ. epic).
 * Каждый эпик знает, какие подзадачи в него входят.
 */

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasks;

    /** Конструктор эпика.
     * @param title Название, кратко описывающее суть эпика.
     * @param description Описание, в котором раскрываются детали эпика.
     */

    public Epic(String title, String description) {
        super(title, description, "");
        epicSubtasks = new ArrayList<>();
    }

    public ArrayList<Integer> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(ArrayList<Integer> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "№=" + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }
}