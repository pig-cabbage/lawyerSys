package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.clock.ScheduleJob;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.StatisticalLawyerDao;
import cabbage.project.lawyerSys.dto.AutoFinishTodoItemDTO;
import cabbage.project.lawyerSys.dto.EndServiceDTO;
import cabbage.project.lawyerSys.dto.StartServiceDTO;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectMessageEntity;
import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.LawyerMathDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("statisticalLawyerService")
public class StatisticalLawyerServiceImpl extends ServiceImpl<StatisticalLawyerDao, StatisticalLawyerEntity> implements StatisticalLawyerService {

  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ServiceLevelService serviceLevelService;
  @Autowired
  private ProjectChatService projectChatService;
  @Autowired
  private Scheduler scheduler;
  @Autowired
  private ProjectMessageService projectMessageService;
  @Autowired
  private SystemMessageService systemMessageService;
  @Autowired
  private ProjectBaseService projectBaseService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<StatisticalLawyerEntity> page = this.page(
        new Query<StatisticalLawyerEntity>().getPage(params),
        new QueryWrapper<StatisticalLawyerEntity>()
    );

    return new PageUtils(page);
  }

//  @Override
//  public void endService(ProjectBaseEntity projectBaseEntity, String nowLawyer, Date date) {
//    QueryWrapper<StatisticalLawyerEntity> wrapper = new QueryWrapper<StatisticalLawyerEntity>().eq("project", projectBaseEntity.getId());
//    wrapper.and(new Consumer<QueryWrapper<StatisticalLawyerEntity>>() {
//      @Override
//      public void accept(QueryWrapper<StatisticalLawyerEntity> statisticalLawyerEntityQueryWrapper) {
//        statisticalLawyerEntityQueryWrapper.eq("lawyer", nowLawyer);
//      }
//    });
//    StatisticalLawyerEntity statisticalLawyerEntity = this.list(wrapper).get(0);
//    Date newDate = new Date(date.getTime() + SystemConstant.CHANGE_LAWYER_GOV * 24 * 60 * 60 * 1000);
//    statisticalLawyerEntity.setEndTime(new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()));
//    statisticalLawyerEntity.setCost((statisticalLawyerEntity.getEndTime().getTime() - statisticalLawyerEntity.getStartTime().getTime()) / (30 * 24 * 60 * 60 * 1000) * serviceLevelService.getById(servicePlanService.getById(projectBaseEntity.getPlan()).getServiceLevel()).getChargeStandard().doubleValue());
//    this.updateById(statisticalLawyerEntity);
//  }

//  @Override
//  public void endServiceFinal(ProjectBaseEntity projectBaseEntity, String nowLawyer) {
//    QueryWrapper<StatisticalLawyerEntity> wrapper = new QueryWrapper<StatisticalLawyerEntity>().eq("project", projectBaseEntity.getId());
//    wrapper.and(new Consumer<QueryWrapper<StatisticalLawyerEntity>>() {
//      @Override
//      public void accept(QueryWrapper<StatisticalLawyerEntity> statisticalLawyerEntityQueryWrapper) {
//        statisticalLawyerEntityQueryWrapper.eq("lawyer", nowLawyer);
//      }
//    });
//    StatisticalLawyerEntity statisticalLawyerEntity = this.list(wrapper).get(0);
//    statisticalLawyerEntity.setEndTime(projectBaseEntity.getEndTime());
//    statisticalLawyerEntity.setCost((statisticalLawyerEntity.getEndTime().getTime() - statisticalLawyerEntity.getStartTime().getTime()) / (30 * 24 * 60 * 60 * 1000) * serviceLevelService.getById(servicePlanService.getById(projectBaseEntity.getPlan()).getServiceLevel()).getChargeStandard().doubleValue());
//    this.updateById(statisticalLawyerEntity);
//  }

  /**
   * @param isFirst 1、创建一条记录
   *                2、设置一个定时任务，增加一条聊天对象记录
   */
  @Override
  public void startService(StartServiceDTO startServiceDTO, boolean isFirst) {
    StatisticalLawyerEntity statisticalLawyerEntity = new StatisticalLawyerEntity();
    BeanUtils.copyProperties(startServiceDTO, statisticalLawyerEntity);
    if (!isFirst) {
      StatisticalLawyerEntity latestRecord = this.baseMapper.getLatestRecord(startServiceDTO.getProject());
      Assert.isNotNull(latestRecord, latestRecord1 -> {
//        statisticalLawyerEntity.setStartTime(new Date(latestRecord1.getEndTime().getTime() + SystemConstant.OLD_TO_NEW_LAWYER * 24 * 60 * 60 * 1000));
        statisticalLawyerEntity.setStartTime(new Date());
      });
    }
    this.save(statisticalLawyerEntity);
    projectMessageService.save(ProjectMessageEntity.builder().project(startServiceDTO.getProject()).receiver(SystemConstant.ROLE_COM).content("律师的服务将于 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(statisticalLawyerEntity.getStartTime()) + "开始").createTime(new Date().getTime()).build());
    projectMessageService.save(ProjectMessageEntity.builder().project(startServiceDTO.getProject()).receiver(SystemConstant.ROLE_LAW).content("您对项目的服务将于 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(statisticalLawyerEntity.getStartTime()) + "开始").createTime(new Date().getTime()).build());
    //创建定时任务
    ProjectChatEntity projectChatEntity = ProjectChatEntity.builder().project(startServiceDTO.getProject()).lawyer(statisticalLawyerEntity.getLawyer()).lawyerName(startServiceDTO.getLawyerName()).company(statisticalLawyerEntity.getCompany()).companyName(startServiceDTO.getCompanyName()).build();
    projectChatService.save(projectChatEntity);
    projectMessageService.save(ProjectMessageEntity.builder().project(startServiceDTO.getProject()).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.LAWYER_START_SERVICE_TO_LAWYER).createTime(new Date().getTime()).build());
    projectMessageService.save(ProjectMessageEntity.builder().project(startServiceDTO.getProject()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.LAWYER_START_SERVICE_TO_COMPANY).createTime(new Date().getTime()).build());
//    try {
//      scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.ADD_CHAT_RECORD), String.valueOf(startServiceDTO.getProject())));
//      AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(SystemConstant.ADD_CHAT_RECORD).projectId(startServiceDTO.getProject()).date(statisticalLawyerEntity.getStartTime()).userId(statisticalLawyerEntity.getLawyer()).userName(startServiceDTO.getLawyerName()).otherId(statisticalLawyerEntity.getCompany()).otherName(startServiceDTO.getCompanyName()).build();
//      JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(SystemConstant.ADD_CHAT_RECORD), String.valueOf(startServiceDTO.getProject())).build();
//      jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
//      Trigger trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(SystemConstant.ADD_CHAT_RECORD), String.valueOf(startServiceDTO.getProject())).startAt(statisticalLawyerEntity.getStartTime()).build();
//      try {
//        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.start();
//      } catch (SchedulerException e) {
//        throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
//      }
//    } catch (SchedulerException e) {
//      e.printStackTrace();
//    }

  }


  /**
   * @param date 1、设置律师服务记录的endTime
   *             2、设置一个定时任务来删除聊天对象记录
   */
  @Override
  public void endService(EndServiceDTO endServiceDTO, Date date) {
    StatisticalLawyerEntity lawyerEntity = this.getOne(new QueryWrapper<StatisticalLawyerEntity>().eq("project", endServiceDTO.getProject()).eq("lawyer", endServiceDTO.getLawyer()).isNull("end_time"));
    if (date.getTime() < lawyerEntity.getStartTime().getTime()) {
      this.removeById(lawyerEntity);
    } else {
//      Date endTime = new Date(date.getTime() + SystemConstant.CHANGE_LAWYER_GOV * SystemConstant.MS_OF_DAY);
//      endTime.setTime(new Date(endTime.getYear(), endTime.getMonth(), endTime.getDate()).getTime());
      Date endTime = date;
      lawyerEntity.setEndTime(endTime);
      lawyerEntity.setCost((double) (endTime.getTime() - lawyerEntity.getStartTime().getTime()) / (SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY) * endServiceDTO.getChargeStandard().doubleValue());
      this.updateById(lawyerEntity);
      projectMessageService.save(ProjectMessageEntity.builder().project(endServiceDTO.getProject()).receiver(SystemConstant.ROLE_COM).content("律师的服务将于 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime) + "终止").createTime(date.getTime()).build());
      projectMessageService.save(ProjectMessageEntity.builder().project(endServiceDTO.getProject()).receiver(SystemConstant.ROLE_LAW).content("您对该项目的服务将于 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime) + "终止").createTime(date.getTime()).build());
      //创建定时任务
      projectChatService.remove(new QueryWrapper<ProjectChatEntity>().eq("project", endServiceDTO.getProject()).eq("lawyer", endServiceDTO.getLawyer()));
      systemMessageService.save(SystemMessageEntity.builder().receiver(endServiceDTO.getLawyer()).content("您对项目：" + projectBaseService.getById(endServiceDTO.getLawyer()).getProjectName() + " 已结束。").createTime(date.getTime()).build());
      projectMessageService.save(ProjectMessageEntity.builder().project(endServiceDTO.getProject()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.END_SERVICE_TO_COMPANY).createTime(date.getTime()).build());
//      try {
//        scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.DELETE_CHAT_RECORD), String.valueOf(endServiceDTO.getProject())));
//        AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(SystemConstant.DELETE_CHAT_RECORD).projectId(endServiceDTO.getProject()).date(endTime).userId(endServiceDTO.getLawyer()).otherId(endServiceDTO.getCompany()).build();
//        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(SystemConstant.DELETE_CHAT_RECORD), String.valueOf(endServiceDTO.getProject())).build();
//        jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(SystemConstant.DELETE_CHAT_RECORD), String.valueOf(endServiceDTO.getProject())).startAt(endTime).build();
//        try {
//          scheduler.scheduleJob(jobDetail, trigger);
//          scheduler.start();
//        } catch (SchedulerException e) {
//          throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
//        }
//      } catch (SchedulerException e) {
//        e.printStackTrace();
//      }

    }

  }

  @Override
  public List<LawyerMathDetailVo> getData(String account, String startDates, String endDates) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try {
      Date startDate = sdf.parse(startDates);
      Date endState = sdf.parse(startDates);
      return this.baseMapper.getDate(account, startDate, endState);
    } catch (ParseException e) {
      throw RunException.builder().code(ExceptionCode.DATE_TRANS_WRONG).build();

    }
  }

}