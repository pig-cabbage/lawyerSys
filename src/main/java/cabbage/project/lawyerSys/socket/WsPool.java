package cabbage.project.lawyerSys.socket;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.dao.ProjectChatRecordDao;
import cabbage.project.lawyerSys.dto.ChatRecordDTO;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import org.java_websocket.WebSocket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class WsPool {

  @Autowired
  private RedisTemplate redisTemplate;

  private static final Map<String, WebSocket> wsUserMap = new HashMap<>();
  private static WsPool myWsPool;


  @PostConstruct
  public void init() {
    myWsPool = this;
    myWsPool.redisTemplate = this.redisTemplate;
  }


  public static WebSocket getWsByUser(String userId) {
    return wsUserMap.get(userId);
  }

  public static void addUser(String userId, WebSocket conn) {
    wsUserMap.put(SystemConstant.WEBSOCKET_KEY_PREFIX + userId, conn);

  }

  public static Collection<String> getOnlineUser() {
    return wsUserMap.keySet();
  }

  public static boolean removeUser(String userId) {
    if (wsUserMap.containsKey(SystemConstant.WEBSOCKET_KEY_PREFIX + userId)) {
      wsUserMap.remove(SystemConstant.WEBSOCKET_KEY_PREFIX + userId);
      return true;
    }
    return false;

  }

  //向某个用户发送消息
  //将消息记录存储在redis中，定时的保留redis中最新的50条数据，其他的数据保存到redis中
  public static void sendMessageToUser(ChatRecordDTO item) {
    if (wsUserMap.containsKey(SystemConstant.WEBSOCKET_KEY_PREFIX + item.getMan())) {
      WebSocket socket = wsUserMap.get(SystemConstant.WEBSOCKET_KEY_PREFIX + item.getMan());
      socket.send(item.getContent());
    }
    ProjectChatRecordEntity entity = new ProjectChatRecordEntity();
    BeanUtils.copyProperties(item, entity);
    entity.setCreateTime(new Date());
    myWsPool.redisTemplate.boundListOps(String.valueOf(item.getSessionId())).rightPush(entity);
  }


}
