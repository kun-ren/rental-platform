package edu.qust.userBusiness.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket Chat message
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class Message {
	public static final String ENTER = "ENTER";
	public static final String SPEAK = "SPEAK";
	public static final String QUIT = "QUIT";
	/**
	 * Message type
	 */
	private String type;
	/**
	 * Sender
	 */
	private String username;
	/**
	 * Send a message
	 */
	private String msg;
	/**
	 * Online user count
	 */
	private int onlineCount;

	static String jsonStr(String type, String username, String msg, int onlineTotal) {
		String jsonStr = "";
		try {
			jsonStr = new ObjectMapper().writeValueAsString(new Message(type, username, msg, onlineTotal));
		} catch (JsonProcessingException e) {
			log.error("JSON serialization error", e);
		}
		return jsonStr;
	}

}
