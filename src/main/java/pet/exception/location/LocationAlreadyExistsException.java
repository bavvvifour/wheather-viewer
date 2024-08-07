package pet.exception.location;

public class LocationAlreadyExistsException extends RuntimeException {
    public LocationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
