package subway.line.exception;

import subway.exceptions.BaseException;

public class LineDistanceNotValidException extends BaseException {
    public LineDistanceNotValidException(final String message) {
        super(message);
    }
}
