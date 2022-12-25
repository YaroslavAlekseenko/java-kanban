package http.exception;

public class KVTaskClientPutException extends RuntimeException {
    public KVTaskClientPutException(String message, Throwable exception) {
        System.out.println(message);
        exception.printStackTrace();
    }
}
