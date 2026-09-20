package edu.qust.common.exception;

/**
 * Resource-not-found exception
 *
 */
public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
