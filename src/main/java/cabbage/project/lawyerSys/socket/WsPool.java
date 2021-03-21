package cabbage.project.lawyerSys.socket;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.*;

public class WsPool {

  @Autowired
  private static RedisTemplate redisTemplate;
  private static final Map<WebSocket, String> wsUserMap = new HashMap<WebSocket, String>();


  public static WebSocket getWsByUser(String userId) {
    return (WebSocket) redisTemplate.opsForValue().get(userId);
  }

  public static void addUser(String userId, WebSocket conn) {
    redisTemplate.opsForValue().set(SystemConstant.WEBSOCKET_KEY_PREFIX + userId, conn);
  }

  public static Collection<String> getOnlineUser() {
    return redisTemplate.keys(SystemConstant.WEBSOCKET_KEY_PREFIX + "*");
  }

  public static boolean removeUser(String userId) {
    if (redisTemplate.hasKey(SystemConstant.WEBSOCKET_KEY_PREFIX + userId)) {
      redisTemplate.delete(SystemConstant.WEBSOCKET_KEY_PREFIX + userId);
      return true;
    } else {
      return false;
    }
  }

  //向某个用户发送消息
  public static void sendMessageToUser(String userId, String message) {
    if (redisTemplate.hasKey(SystemConstant.WEBSOCKET_KEY_PREFIX + userId)) {
      WebSocket socket = (WebSocket) redisTemplate.opsForValue().get(SystemConstant.WEBSOCKET_KEY_PREFIX + userId);
      socket.send(message);
    } else {
      throw RunException.builder().code(ExceptionCode.USER_OFFLINE).build();
    }
  }

//  //向包含某些特征的用户发送消息
//  public static void sendMessageToSpecialUser(String message,String special) {
//    Set<WebSocket> keySet = wsUserMap.keySet();
//    if (special == null) {
//      special = "";
//    }
//    synchronized (keySet) {
//      for (WebSocket conn:keySet) {
//        String user = wsUserMap.get(conn);
//        try {
//          if (user != null) {
//            String [] cus = user.split("_");
//            if (!StringUtils.isEmpty(cus[0])) {
//              String cusDot = "," + cus[0] + ",";
//              if (cusDot.contains(","+special+",")) {
//                conn.send(message);
//              }
//            }else {
//              conn.send(message);
//            }
//          }
//        }catch (Exception e) {
//          e.printStackTrace();
//          //wsUserMap.remove(conn);
//        }
//      }
//
//    }
//  }
//
//  //向所有用户发送数据
//  public static void sendMessageToAll(String message) {
//    Set<WebSocket> keySet = wsUserMap.keySet();
//    synchronized (keySet) {
//      for (WebSocket conn : keySet) {
//        String user = wsUserMap.get(conn);
//        if (user != null) {
//          conn.send(message);
//        }
//      }
//    }
//  }

}
