package subway.line.exception;

import subway.exceptions.BaseException;

public class LineHasNoStationException extends BaseException {
    public LineHasNoStationException(final String message) {
        super(message);
    }
}
