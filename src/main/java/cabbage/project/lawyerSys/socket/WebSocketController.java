package cabbage.project.lawyerSys.socket;

import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.dto.ChatRecordDTO;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.vo.MessageVo;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/ws/api/im/{userId}")
@Component
public class WebSocketController {

  @Autowired
  private RedisTemplate redisTemplate;


  static Log log = LogFactory.get(WebSocketServer.class);
  /*静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
//  private static int onlineCount = 0;
  /**
   * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
   */
  private static ConcurrentHashMap<String, WebSocketController> webSocketMap = new ConcurrentHashMap<>();
  /**
   * 与某个客户端的连接会话，需要通过它来给客户端发送数据
   */
  private Session session;
  /**
   * 接收userId
   */
  private String userId = "";

  private static WebSocketController webSocketController;

  @PostConstruct
  public void init() {
    webSocketController = this;
    webSocketController.redisTemplate = this.redisTemplate;
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("userId") String userId) {
    this.session = session;
    this.userId = userId;
    if (webSocketMap.containsKey(userId)) {
      webSocketMap.remove(userId);
      webSocketMap.put(userId, this);
      //加入set中
    } else {
      webSocketMap.put(userId, this);
    }
    try {
      sendMessage("连接成功");
    } catch (IOException e) {
      log.error("用户:" + userId + ",网络异常!!!!!!");
    }
  }

  /**
   * @param session
   * @param error
   */
  @OnError
  public void onError(Session session, Throwable error) {
    log.error("发生错误");
    error.printStackTrace();
  }

  @OnClose
  public void onClose() {
    if (webSocketMap.containsKey(userId)) {
      webSocketMap.remove(userId);
    }
    log.info("fail");
//    log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
  }


  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("用户消息:" + userId + ",报文:" + message);
    //可以群发消息
    //消息保存到数据库、redis
    if (StringUtils.isNotBlank(message)) {
      ChatRecordDTO item = JSONObject.parseObject(message, ChatRecordDTO.class);
      if (webSocketMap.containsKey(item.getMan())) {
        try {
          webSocketMap.get(item.getMan()).sendMessage(JSONObject.toJSONString(MessageVo.builder().content(item.getContent()).createTime(item.getCreateTime()).sessionId(item.getSessionId()).build()));
        } catch (IOException e) {
          throw RunException.builder().code(ExceptionCode.SEND_MESSAGE_FAIL).message(e.getMessage()).build();
        }
      }
      ProjectChatRecordEntity entity = new ProjectChatRecordEntity();
      BeanUtils.copyProperties(item, entity);
      entity.setCreateTime(new Date(item.getCreateTime()));
      if (Boolean.FALSE == webSocketController.redisTemplate.hasKey(String.valueOf(item.getSessionId()))) {
        webSocketController.redisTemplate.boundListOps(String.valueOf(item.getSessionId())).expire(1000000000, TimeUnit.MILLISECONDS);
      }
      webSocketController.redisTemplate.boundListOps(String.valueOf(item.getSessionId())).rightPush(entity);
    }
  }

  /**
   * 实现服务器主动推送
   */
  public void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);
  }


}
