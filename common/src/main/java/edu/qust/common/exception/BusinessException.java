package edu.qust.common.exception;

/**
 * Business exception that must be handled
 *
 */
public abstract class BusinessException extends RuntimeException {
	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
