package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.clock.ScheduleJob;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectUserTodoItemDao;
import cabbage.project.lawyerSys.dto.AutoFinishTodoItemDTO;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("projectUserTodoItemService")
public class ProjectUserTodoItemServiceImpl extends ServiceImpl<ProjectUserTodoItemDao, ProjectUserTodoItemEntity> implements ProjectUserTodoItemService {

  @Autowired
  private ConstantTodoItemService constantTodoItemService;
  @Autowired
  private SystemMessageService systemMessageService;
  @Autowired
  private ProjectBaseService projectBaseService;
  @Autowired
  private ProjectCompanyDemandLawyerService projectCompanyDemandLawyerService;
  @Autowired
  private ProjectLawyerCarryService projectLawyerCarryService;
  @Autowired
  private ProjectLawyerDealChangeLawyerService projectLawyerDealChangeLawyerService;
  @Autowired
  private Scheduler scheduler;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectUserTodoItemEntity> page = this.page(
        new Query<ProjectUserTodoItemEntity>().getPage(params),
        new QueryWrapper<ProjectUserTodoItemEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public void addItem(ProjectUserTodoItemEntity projectUserTodoItemEntity, String theOtherId, Long eventId, Date date) {
    ConstantTodoItemEntity constantTodoItemEntity = constantTodoItemService.getById(projectUserTodoItemEntity.getItem());
    projectUserTodoItemEntity.setLatestTime(new Date(date.getTime() + constantTodoItemEntity.getDuration() * 24 * 60 * 60 * 1000));
    this.save(projectUserTodoItemEntity);
    AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(projectUserTodoItemEntity.getItem()).projectId(projectUserTodoItemEntity.getProject()).userId(projectUserTodoItemEntity.getUser()).otherId(theOtherId).date(date).defaultValue(constantTodoItemEntity.getDefaultValue()).build();
    JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(constantTodoItemEntity.getId()), String.valueOf(projectUserTodoItemEntity.getProject())).build();
    jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
    jobDetail.getJobDataMap().put("eventId", eventId);
    jobDetail.getJobDataMap().put("project", projectBaseService.getById(projectUserTodoItemEntity.getProject()));
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(constantTodoItemEntity.getId()), String.valueOf(projectUserTodoItemEntity.getProject())).startAt(projectUserTodoItemEntity.getLatestTime()).build();
    try {
      scheduler.scheduleJob(jobDetail, trigger);
      scheduler.start();
    } catch (SchedulerException e) {
      throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
    }
  }

  @Override
  public void finishItemWithUser(Long id, Long itemKey, Date date) {
    Assert.isNotNull(this.baseMapper.getItem(id, itemKey), item -> {
      item.setStatus(1);
      item.setFinishTime(date);
      item.setType(0);
      this.updateById(item);
      try {
        scheduler.deleteJob(new JobKey(String.valueOf(itemKey), String.valueOf(id)));
      } catch (SchedulerException e) {
        throw RunException.builder().code(ExceptionCode.DELETE_SCHEDULE_ERROR).build();
      }
    });
  }

  @Override
  public void finishItemWithSystem(Long id, Long itemKey, Date date) {
    Assert.isNotNull(this.baseMapper.getItem(id, itemKey), item -> {
      item.setStatus(1);
      item.setFinishTime(date);
      item.setType(1);
      this.updateById(item);
    });
  }

}