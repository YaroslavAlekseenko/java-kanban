package http.exception;

public class KVTaskClientLoadException extends RuntimeException {
    public KVTaskClientLoadException(String message, Throwable exception) {
        System.out.println(message);
        exception.printStackTrace();
    }
}

