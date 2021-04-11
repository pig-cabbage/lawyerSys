package cabbage.project.lawyerSys.socket;

import cabbage.project.lawyerSys.dto.ChatRecordDTO;
import cabbage.project.lawyerSys.dto.WebSocketDTO;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class MyWebSocket extends WebSocketServer {

  public MyWebSocket() throws UnknownHostException {
    super();
  }

  public MyWebSocket(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    userLeave(s);
  }

//  @Override
//  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//    //断开连接时候触发的代码
//    userLeave(conn);
//    System.out.println(reason);
//  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    //有用户连接进来
    Map<String, String> obj = (Map<String, String>) JSONObject.parse(message);
    if (obj.containsKey("name")) {
      String username = obj.get("name");
      userJoin(conn, username);
      System.out.println(username + "已经上线了！");
    } else {
      if (obj.containsKey("man")) {
        WsPool.sendMessageToUser(JSONObject.parseObject(message, ChatRecordDTO.class));
      }
    }
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    //错误时候触发的代码
    System.out.println("on error");
    ex.printStackTrace();
  }

  /**
   * 去除掉失效的websocket连接
   */
  private void userLeave(String userId) {
    WsPool.removeUser(userId);
  }

  /**
   * 将websocket加入用户池
   *
   * @param conn
   * @param userId
   */
  private void userJoin(WebSocket conn, String userId) {
    WsPool.addUser(userId, conn);
  }
}

