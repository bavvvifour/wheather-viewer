package pet.exception.location;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
