package subway.line.exception;

public class InvalidDownStationException extends RuntimeException {
    public InvalidDownStationException(final String message) {
        super(message);
    }
}
