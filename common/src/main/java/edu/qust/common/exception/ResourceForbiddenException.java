package edu.qust.common.exception;

/**
 * 资源无权限访问异常
 *
 */
public class ResourceForbiddenException extends BusinessException {
	public ResourceForbiddenException(String message) {
		super(message);
	}
}
