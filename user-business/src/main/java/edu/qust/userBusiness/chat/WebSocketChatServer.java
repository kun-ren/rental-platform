package edu.qust.userBusiness.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Chat server
 *
 */
@Slf4j
@Component
@ServerEndpoint("/chatServer")
public class WebSocketChatServer {

	/**
	 * All active sessions  PS: Use a thread-safe map to store sessions
	 */
	private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();


	/**
	 * When a client connects: add its session and update the online count
	 */
	@OnOpen
	public void onOpen(Session session) {
		onlineSessions.put(session.getId(), session);
		sendMessageToAll(Message.jsonStr(Message.ENTER, "", "", onlineSessions.size()));
	}

	/**
	 * When a client sends a message: read the username and message, then broadcast it
	 * <p>
	 * PS: Messages use JSON so they can carry additional fields.
	 */
	@OnMessage
	public void onMessage(Session session, String jsonStr) throws IOException {
		Message message = new ObjectMapper().readValue(jsonStr, Message.class);
		log.info(jsonStr);
		sendMessageToAll(Message.jsonStr(Message.SPEAK, message.getUsername(), message.getMsg(), onlineSessions.size()));
	}

	/**
	 * When a client disconnects: remove its session and update the online count
	 */
	@OnClose
	public void onClose(Session session) {
		onlineSessions.remove(session.getId());
		sendMessageToAll(Message.jsonStr(Message.QUIT, "", "", onlineSessions.size()));
	}

	/**
	 * Log communication errors
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}

	/**
	 * Broadcast a message to every connected client
	 */
	private static void sendMessageToAll(String msg) {
		onlineSessions.forEach((id, session) -> {
			try {
				session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
