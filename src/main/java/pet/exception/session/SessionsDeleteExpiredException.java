package pet.exception.session;

public class SessionsDeleteExpiredException extends RuntimeException {
    public SessionsDeleteExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
