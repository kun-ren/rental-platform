package edu.qust.common.exception;

/**
 * 资源已存在异常
 *
 */
public class ResourceAlreadyExistsException extends BusinessException {
	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
