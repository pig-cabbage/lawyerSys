package cabbage.project.lawyerSys.clock;

import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.dto.AutoFinishTodoItemDTO;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

@Component
public class ScheduleJob implements Job {

  @Autowired
  private ProjectUserTodoItemService projectUserTodoItemService;
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

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    AutoFinishTodoItemDTO finishTodoItemDTO = (AutoFinishTodoItemDTO) jobExecutionContext.getMergedJobDataMap().get("finishTodoItemDTO");
    ProjectBaseEntity project = (ProjectBaseEntity) jobExecutionContext.getMergedJobDataMap().get("project");
    Long eventId = (Long) jobExecutionContext.getMergedJobDataMap().get("eventId");
    int itemKey = finishTodoItemDTO.getItemKey().intValue();
    switch (itemKey) {
      case 1:
        //支付费用过期
        //1、修改项目状态
        //2、用户生成一条系统消息
        //3、修改待办事项状态
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.PAY_PAST_DUE);
        systemMessageService.addMessageWithoutEventId(finishTodoItemDTO.getUserId(), SystemConstant.SystemMessageEnum.PAY_PAST_DUE, finishTodoItemDTO.getDate());
        projectUserTodoItemService.finishItemWithSystem(project.getId(), SystemConstant.PAY_ITEM_KEY, finishTodoItemDTO.getDate());
        break;
      case 2:
        //选择律师操作过期， 系统执行默认操作
        //1、修改项目状态
        //2、生成一条操作记录
        //3、企业生成一条系统消息
        //4、修改待办事项状态
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_DIS_LAWYER);
        ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyerEntity = ProjectCompanyDemandLawyerEntity.builder()
            .project(finishTodoItemDTO.getProjectId())
            .recommendLawyer(finishTodoItemDTO.getDefaultValue())
            .createTime(finishTodoItemDTO.getDate()).build();
        projectCompanyDemandLawyerService.save(projectCompanyDemandLawyerEntity);
        systemMessageService.addMessageWithoutEventId(finishTodoItemDTO.getUserId(), SystemConstant.SystemMessageEnum.CHOOSE_LAWYER_PAST_DUE, finishTodoItemDTO.getDate());
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.CHOOSE_LAWYER_KEY, finishTodoItemDTO.getDate());
        break;
      case 3:
        //律师决定是否承接项目操作过期， 系统执行默认操作
        //1、修改项目状态
        //2、生成一条操作记录
        //3、修改待办事项状态
        //4、律师用户和企业用户生成一条系统消息
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER);
        ProjectLawyerCarryEntity lawyerCarryEntity = ProjectLawyerCarryEntity.builder()
            .carry(finishTodoItemDTO.getDefaultValue())
            .reason("操作过期，系统自动执行拒绝操作")
            .lawyer(eventId)
            .lawyer(1L)
            .createTime(finishTodoItemDTO.getDate()).build();
        projectLawyerCarryService.save(lawyerCarryEntity);
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.LAWYER_DETERMINE_UNDER_TAKE, finishTodoItemDTO.getDate());
        systemMessageService.addMessage(finishTodoItemDTO.getUserId(), SystemConstant.SystemMessageEnum.DETERMINE_UNDER_TAKE_PAST_DUE_TO_LAWYER, String.valueOf(finishTodoItemDTO.getProjectId()), finishTodoItemDTO.getDate());
        systemMessageService.addMessage(finishTodoItemDTO.getOtherId(), SystemConstant.SystemMessageEnum.DETERMINE_UNDER_TAKE_PAST_DUE_TO_COMPANY, finishTodoItemDTO.getUserId(), finishTodoItemDTO.getDate());
        break;
      case 4:
        //处理更换律师请求操作过期，系统执行默认操作
        //1、修改项目状态
        //2、生成一条处理记录
        //3、修改待办事项状态
        //4、律师用户和企业用户生成一条系统消息
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
        ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity = ProjectLawyerDealChangeLawyerEntity.builder()
            .changeLawyer(eventId).result(0).createTime(finishTodoItemDTO.getDate()).build();
        projectLawyerDealChangeLawyerService.save(projectLawyerDealChangeLawyerEntity);
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER, finishTodoItemDTO.getDate());
        systemMessageService.addMessage(finishTodoItemDTO.getUserId(), SystemConstant.SystemMessageEnum.DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_LAWYER, String.valueOf(eventId), finishTodoItemDTO.getDate());
        systemMessageService.addMessageWithoutEventId(finishTodoItemDTO.getOtherId(), SystemConstant.SystemMessageEnum.LAWYER_AGREE_CHANGE_LAWYER, finishTodoItemDTO.getDate());
        break;
      default:
        //其他情况：企业重新选择律师过期
        //1、修改项目状态
        //2、生成一条操作记录
        //3、企业生成一条系统消息
        //4、修改待办事项状态
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_DIS_LAWYER);
        ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyerEntity1 = ProjectCompanyDemandLawyerEntity.builder().project(finishTodoItemDTO.getProjectId()).recommendLawyer(finishTodoItemDTO.getDefaultValue()).createTime(finishTodoItemDTO.getDate()).build();
        projectCompanyDemandLawyerService.save(projectCompanyDemandLawyerEntity1);
        systemMessageService.addMessageWithoutEventId(finishTodoItemDTO.getUserId(), SystemConstant.SystemMessageEnum.CHOOSE_LAWYER_PAST_DUE, finishTodoItemDTO.getDate());
        if (5 == itemKey) {
          projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.RE_CHOOSE_LAWYER, finishTodoItemDTO.getDate());
        } else {
          projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.RE_CHOOSE_LAWYER_LAWYER_AGREE, finishTodoItemDTO.getDate());
        }
        break;
    }
  }
}
