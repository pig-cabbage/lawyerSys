package cabbage.project.lawyerSys.config;

import cabbage.project.lawyerSys.clock.RedisToMysqlTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheConfig {
  @Bean
  public JobDetail RedisToMysqlQuartz() {
    // 执行定时任务
    return JobBuilder.newJob(RedisToMysqlTask.class).withIdentity("REDIS_MYSQL", "DATABASE").storeDurably().build();
  }

  @Bean
  public Trigger CallPayQuartzTaskTrigger(JobDetail jobDetail) {
    //cron方式，从每月1号开始，每隔三天就执行一次
    return TriggerBuilder.newTrigger().forJob(jobDetail)
        .withIdentity("CallPayQuartzTask")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0,8 * * ?"))
        .build();
  }
}
