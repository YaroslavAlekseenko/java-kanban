package tasks;

import java.util.ArrayList;

/** Класс эпика.
 * Большая задача, которая делится на подзадачи, называется эпиком (англ. epic).
 * Каждый эпик знает, какие подзадачи в него входят.
 */

public class Epic extends Task {

    private ArrayList<Integer> epicSubtaskIDs;

    /** Конструктор эпика.
     * @param title Название, кратко описывающее суть эпика.
     * @param description Описание, в котором раскрываются детали эпика.
     */

    public Epic(String title, String description) {
        super(title, description, "");
        epicSubtaskIDs = new ArrayList<>();
    }

    public ArrayList<Integer> getEpicSubtaskIDs() {
        return epicSubtaskIDs;
    }

    public void setEpicSubtaskIDs(ArrayList<Integer> epicSubtaskIDs) {
        this.epicSubtaskIDs = epicSubtaskIDs;
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