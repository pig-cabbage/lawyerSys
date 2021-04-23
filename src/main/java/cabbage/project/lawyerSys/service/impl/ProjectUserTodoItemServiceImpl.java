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
import cabbage.project.lawyerSys.vo.TodoItemVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("projectUserTodoItemService")
public class ProjectUserTodoItemServiceImpl extends ServiceImpl<ProjectUserTodoItemDao, ProjectUserTodoItemEntity> implements ProjectUserTodoItemService {

  @Autowired
  private ConstantTodoItemService constantTodoItemService;
  @Autowired
  private ProjectMessageService systemMessageService;
  @Autowired
  private ProjectBaseService projectBaseService;
  @Autowired
  private ProjectPlanService projectPlanService;
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
  public Long addItem(ProjectUserTodoItemEntity projectUserTodoItemEntity, String theOtherId, Long eventId, Date date) {
    ConstantTodoItemEntity constantTodoItemEntity = constantTodoItemService.getById(projectUserTodoItemEntity.getItem());
    projectUserTodoItemEntity.setCreateTime(date);
    projectUserTodoItemEntity.setLatestTime(new Date(date.getTime() + constantTodoItemEntity.getDuration() * 24 * 60 * 60 * 1000));
    this.save(projectUserTodoItemEntity);
    try {
      scheduler.deleteJob(new JobKey(String.valueOf(constantTodoItemEntity.getId()), String.valueOf(projectUserTodoItemEntity.getProject())));
      AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(projectUserTodoItemEntity.getItem()).projectId(projectUserTodoItemEntity.getProject()).userId(projectUserTodoItemEntity.getUser()).otherId(theOtherId).date(projectUserTodoItemEntity.getLatestTime()).defaultValue(constantTodoItemEntity.getDefaultValue()).build();
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
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
    return projectUserTodoItemEntity.getId();
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

  @Override
  public List<TodoItemVo> search(Map<String, Object> params) {
    QueryWrapper<ProjectUserTodoItemEntity> wrapper = new QueryWrapper<ProjectUserTodoItemEntity>();
    List<ProjectUserTodoItemEntity> itemEntities = new ArrayList<>();
    if (params.containsKey("project")) {
      wrapper.eq("project", params.get("project")).eq("status", 0);
      if (params.containsKey("user")) {
        wrapper.eq("user", params.get("user"));
      }
      itemEntities = this.list(wrapper);
    } else {
      wrapper.eq("user", params.get("user")).eq("status", 0);
      itemEntities = this.list(wrapper);
    }
    return itemEntities.stream().map(item -> {
      TodoItemVo itemVo = new TodoItemVo();
      BeanUtils.copyProperties(item, itemVo);
      ConstantTodoItemEntity constantItem = constantTodoItemService.getById(item.getItem());
      itemVo.setItemName(constantItem.getName());
      itemVo.setItemContent(constantItem.getContent());
      return itemVo;
    }).collect(Collectors.toList());
  }

  @Override
  public TodoItemVo getByIdNew(Integer id) {
    ProjectUserTodoItemEntity byId = this.getById(id);
    TodoItemVo itemVo = new TodoItemVo();
    BeanUtils.copyProperties(byId, itemVo);
    ConstantTodoItemEntity constantItem = constantTodoItemService.getById(byId.getItem());
    itemVo.setItemName(constantItem.getName());
    itemVo.setItemContent(constantItem.getContent());
    return itemVo;
  }

}