package http.exception;

public class KVTaskClientRegisterException extends RuntimeException {
    public KVTaskClientRegisterException(Throwable exception) {
        exception.printStackTrace();
    }
}

