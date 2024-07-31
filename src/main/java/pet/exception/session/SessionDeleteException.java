package pet.exception.session;

public class SessionDeleteException extends RuntimeException {
    public SessionDeleteException(String message) {
        super(message);
    }

    public SessionDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
