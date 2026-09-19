package edu.qust.userService;

import edu.qust.common.component.alarm.AlarmApi;
import edu.qust.common.util.ApplicationContextUtil;

/**
 * 用邮件实现的告警
 *
 */
public class MailAlarm implements AlarmApi {
	private static MailService mailService;

	/**
	 * 发送告警
	 *
	 * @param content 告警内容
	 */
	@Override
	public void alarm(String content) {
		if (mailService == null) {
			mailService = ApplicationContextUtil.getBean(MailService.class);
		}
		mailService.sendEmailAsync(" codethereforam@gmail.com " , "告警" , content);
	}
}
