package cabbage.project.lawyerSys.dto;

import lombok.Data;
import org.java_websocket.WebSocket;

import java.io.Serializable;

@Data
public class WebSocketDTO implements Serializable {

  private WebSocket webSocket;

  public WebSocketDTO(WebSocket conn) {
    this.webSocket = conn;
  }

  public void send(String message) {
    this.webSocket.send(message);
  }
}
