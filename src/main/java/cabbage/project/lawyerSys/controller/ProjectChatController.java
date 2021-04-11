package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.socket.WsPool;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/project/chat")
public class ProjectChatController {
  @Autowired
  private RedisTemplate redisTemplate;

  @RequestMapping("/{session}/latest")
  public R latestRecord(@PathVariable("session") Long sessionId) {
    return R.ok().put("list", redisTemplate.opsForList().range(String.valueOf(sessionId), 0, redisTemplate.opsForList().size(String.valueOf(sessionId))));
  }
}
