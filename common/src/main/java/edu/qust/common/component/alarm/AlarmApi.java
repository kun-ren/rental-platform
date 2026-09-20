package edu.qust.common.component.alarm;

/**
 * Alert interface
 * <pre>
 *     Define an SPI-based alert interface; clients may deliver alerts through email, SMS, or another channel
 * </pre>
 *
 */
public interface AlarmApi {
	/**
	 * Send an alert
	 *
	 * @param content Alert content
	 */
	void alarm(String content);
}
