package edu.qust.common.exception;

import edu.qust.common.exception.BusinessException;

public class ContentIllegalException extends BusinessException {
    public ContentIllegalException(String message) {
        super(message);
    }
}
