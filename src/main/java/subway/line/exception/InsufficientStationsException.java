package subway.line.exception;

public class InsufficientStationsException extends RuntimeException {
    public InsufficientStationsException(final String message) {
        super(message);
    }
}
