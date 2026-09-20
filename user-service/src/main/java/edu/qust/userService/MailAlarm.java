package edu.qust.userService;

import edu.qust.common.component.alarm.AlarmApi;
import edu.qust.common.util.ApplicationContextUtil;

/**
 * Email-based alert implementation
 *
 */
public class MailAlarm implements AlarmApi {
	private static MailService mailService;

	/**
	 * Send an alert
	 *
	 * @param content Alert content
	 */
	@Override
	public void alarm(String content) {
		if (mailService == null) {
			mailService = ApplicationContextUtil.getBean(MailService.class);
		}
		mailService.sendEmailAsync(" codethereforam@gmail.com " , "Alert" , content);
	}
}
