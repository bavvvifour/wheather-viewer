package pet.exception.session;

public class SessionAlreadyExistsException extends RuntimeException {
    public SessionAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
