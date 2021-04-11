package cabbage.project.lawyerSys.clock;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import cabbage.project.lawyerSys.service.ProjectChatService;
import cabbage.project.lawyerSys.valid.Assert;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时将redis的数据持久化到mysql，redis只保留一定数量的数据
 */
@Component
public class RedisToMysqlTask implements Job {

  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  ProjectChatRecordService projectChatRecordService;
  @Autowired
  ProjectChatService projectChatService;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    List<ProjectChatEntity> list = projectChatService.list();
    Assert.isNotEmpty(list, list1 -> {
      list1.forEach(session -> {
        Long size = redisTemplate.opsForList().size(String.valueOf(session.getId()));
        for (int i = 0; i < size - SystemConstant.MAX_CHAR_RECORD_REDIS; i++) {
          projectChatRecordService.save((ProjectChatRecordEntity) redisTemplate.opsForList().leftPop(String.valueOf(session.getId())));
        }
      });
    });
  }
}
