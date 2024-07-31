package pet.exception;

public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
