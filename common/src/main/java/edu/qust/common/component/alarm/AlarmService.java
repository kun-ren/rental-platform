package edu.qust.common.component.alarm;

import org.springframework.stereotype.Service;

import java.util.ServiceLoader;

/**
 * Alert service
 *
 */
@Service
public class AlarmService {
	private ServiceLoader<AlarmApi> loader = ServiceLoader.load(AlarmApi.class);

	/**
	 * Send an alert
	 *
	 * @param content Alert content
	 */
	public void alarm(String content) {
		for (AlarmApi alarmApi : loader) {
			alarmApi.alarm(content);
		}
	}

}
