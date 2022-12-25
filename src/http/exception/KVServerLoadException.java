package http.exception;

public class KVServerLoadException extends RuntimeException {
    public KVServerLoadException(String message, Throwable exception) {
        System.out.println(message);
        exception.printStackTrace();
    }
}
