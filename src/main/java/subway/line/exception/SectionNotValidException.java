package subway.line.exception;

import subway.exceptions.BaseException;

public class SectionNotValidException extends BaseException {
    public SectionNotValidException(final String message) {
        super(message);
    }
}
