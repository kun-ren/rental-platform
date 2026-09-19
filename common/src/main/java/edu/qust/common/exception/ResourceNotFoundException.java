package edu.qust.common.exception;

/**
 * 资源不存在异常
 *
 */
public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
