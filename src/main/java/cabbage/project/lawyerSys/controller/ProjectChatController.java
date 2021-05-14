package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import cabbage.project.lawyerSys.service.ProjectChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/project/chat")
public class ProjectChatController {
  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private ProjectChatRecordService projectChatRecordService;

  @RequestMapping("/{session}/latest")
  public R latestRecord(@PathVariable("session") String sessionId) {
    return R.ok().put("list", redisTemplate.boundListOps(sessionId).range(0, redisTemplate.opsForList().size(sessionId)));
  }

  @RequestMapping("/{session}/more")
  public R moreRecord(@PathVariable("session") String sessionId, @RequestParam Map<String, Object> params) {
    PageUtils page = projectChatRecordService.queryPage(sessionId, params);
    return R.ok().put("page", page);
  }

}
