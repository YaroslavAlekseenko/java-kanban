package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskManager;
import http.LocalDateTimeAdapter;

import java.io.File;
import java.time.LocalDateTime;

/** Утилитарный класс.
 * Отвечает за создание менеджера задач.
 */

public class Managers {

    /** Возврат объекта реализующего интерфейс TaskManager */
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    /** Возврат объекта реализующего интерфейс HistoryManager */
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    /** Возврат объекта реализующего FileBackedTasksManager */
    public static FileBackedTasksManager getDefaultFileBackedTasksManager(){
        return new FileBackedTasksManager(new File("src/data/data.csv"));
    }

    /** Возврат объекта реализующего HttpTaskManager */
    public static HttpTaskManager getDefaultHttpTaskManager() {
        return new HttpTaskManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}