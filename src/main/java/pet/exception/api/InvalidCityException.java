package pet.exception.api;

public class InvalidCityException extends RuntimeException {
        public InvalidCityException(String message, Throwable cause) {
            super(message, cause);
        }
    }