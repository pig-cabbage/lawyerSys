package cabbage.project.lawyerSys.clock;

import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.dto.AutoFinishTodoItemDTO;
import cabbage.project.lawyerSys.dto.EndServiceDTO;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleJob implements Job {

  @Autowired
  private ProjectUserTodoItemService projectUserTodoItemService;
  @Autowired
  private SystemMessageService systemMessageService;
  @Autowired
  private ProjectMessageService projectMessageService;
  @Autowired
  private ProjectBaseService projectBaseService;
  @Autowired
  private ProjectCompanyDemandLawyerService projectCompanyDemandLawyerService;
  @Autowired
  private ProjectLawyerCarryService projectLawyerCarryService;
  @Autowired
  private ProjectLawyerDealChangeLawyerService projectLawyerDealChangeLawyerService;
  @Autowired
  private ProjectChatService projectChatService;
  @Autowired
  private StatisticalLawyerService statisticalLawyerService;
  @Autowired
  private ProjectCompanyEvaluationService projectCompanyEvaluationService;
  @Autowired
  private ConstantTodoItemService constantTodoItemService;
  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ServiceLevelService serviceLevelService;


  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    AutoFinishTodoItemDTO finishTodoItemDTO = (AutoFinishTodoItemDTO) jobExecutionContext.getMergedJobDataMap().get("finishTodoItemDTO");
    ProjectBaseEntity project = (ProjectBaseEntity) jobExecutionContext.getMergedJobDataMap().get("project");
    Long eventId = (Long) jobExecutionContext.getMergedJobDataMap().get("eventId");
    Double chargeStandard = (Double) jobExecutionContext.getMergedJobDataMap().get("chargeStandard");
    int itemKey = finishTodoItemDTO.getItemKey().intValue();

    switch (itemKey) {
      case 1:
        //支付费用过期
        //1、修改项目状态
        //2、用户生成一条系统消息
        //3、修改待办事项状态
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.PAY_PAST_DUE);
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.PAY_PAST_DUE).createTime(finishTodoItemDTO.getDate().getTime()).build());
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
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.CHOOSE_LAWYER_PAST_DUE).createTime(finishTodoItemDTO.getDate().getTime()).build());
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
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.DETERMINE_UNDER_TAKE_PAST_DUE_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());
        systemMessageService.save(SystemMessageEntity.builder().receiver(finishTodoItemDTO.getUserId()).content(SystemConstant.DETERMINE_UNDER_TAKE_PAST_DUE_TO_LAWYER).createTime(finishTodoItemDTO.getDate().getTime()).build());
        break;
      case 4:
        //处理更换律师请求操作过期，系统执行默认操作
        //1、修改项目状态
        //2、生成一条处理记录
        //3、律师用户生成一个代办事项
        //3、修改待办事项状态
        //4、律师用户和企业用户生成一条系统消息
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
        ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity = ProjectLawyerDealChangeLawyerEntity.builder()
            .changeLawyer(eventId).result(0).createTime(finishTodoItemDTO.getDate()).build();
        projectLawyerDealChangeLawyerService.save(projectLawyerDealChangeLawyerEntity);
        ProjectUserTodoItemEntity userTodoItemEntity1 = ProjectUserTodoItemEntity.builder().project(finishTodoItemDTO.getProjectId()).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.DEAL_PAST_DUE).createTime(finishTodoItemDTO.getDate()).build();
        projectUserTodoItemService.addItem(userTodoItemEntity1, null, null, finishTodoItemDTO.getDate());
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER, finishTodoItemDTO.getDate());
        EndServiceDTO endServiceDTO = EndServiceDTO.builder().project(finishTodoItemDTO.getProjectId()).lawyer(project.getNowLawyer()).company(project.getCompany()).date(finishTodoItemDTO.getDate()).chargeStandard(serviceLevelService.getById(servicePlanService.getById(project.getPlan()).getServiceLevel()).getChargeStandard()).build();
        statisticalLawyerService.endService(endServiceDTO, finishTodoItemDTO.getDate());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_LAWYER).createTime(finishTodoItemDTO.getDate().getTime()).build());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());
        break;
      case 8:
        //处理续费项目过期， 系统执行默认拒绝操作
        //对项目做出评价
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), SystemConstant.RENEW_PROJECT, finishTodoItemDTO.getDate());
        ProjectCompanyEvaluationEntity projectCompanyEvaluationEntity = new ProjectCompanyEvaluationEntity();
        projectCompanyEvaluationEntity.setProject(finishTodoItemDTO.getProjectId());
        projectCompanyEvaluationEntity.setScore(5);
        projectCompanyEvaluationEntity.setContent("用户没有评价，系统自动处理");
        projectCompanyEvaluationEntity.setCreateTime(finishTodoItemDTO.getDate());
        projectCompanyEvaluationService.save(projectCompanyEvaluationEntity);
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.RENEW_PROJECT_PAST_DUE_COMPANY)
            .appendContent("{评分: 5, 评价内容: 用户没有评价，系统自动处理。}").createTime(finishTodoItemDTO.getDate().getTime()).build());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.RENEW_PROJECT_PAST_DUE_LAWYER)
            .appendContent("{评分: 5, 评价内容: 用户没有评价，系统自动处理。}").createTime(finishTodoItemDTO.getDate().getTime()).build());
        break;
      case 99:
        //提醒用户续费项目
        ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(finishTodoItemDTO.getProjectId()).user(finishTodoItemDTO.getUserId()).projectName(project.getProjectName()).item(SystemConstant.RENEW_PROJECT).createTime(finishTodoItemDTO.getDate()).build();
        projectUserTodoItemService.addItem(userTodoItemEntity, "", null, finishTodoItemDTO.getDate());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.REMIND_RENEW_PROJECT).appendContent("代办事项").itemId(String.valueOf(userTodoItemEntity.getId())).createTime(finishTodoItemDTO.getDate().getTime()).build());
        break;
      case 9:
        //律师开始服务时发生的事件
        //1、增加一条聊天对象记录
        //2、修改项目属性
        //3、企业用户和律师用户都新增一条系统消息
        ProjectChatEntity projectChatEntity = ProjectChatEntity.builder().project(finishTodoItemDTO.getProjectId()).lawyer(finishTodoItemDTO.getUserId()).lawyerName(finishTodoItemDTO.getUserName()).company(finishTodoItemDTO.getOtherId()).companyName(finishTodoItemDTO.getOtherName()).build();
        projectChatService.save(projectChatEntity);
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.LAWYER_START_SERVICE_TO_LAWYER).createTime(finishTodoItemDTO.getDate().getTime()).build());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.LAWYER_START_SERVICE_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());

      case 10:
        //律师服务结束
        //1、删除聊天对象记录
        //2、企业用户和律师用户新增一条系统消息
        projectChatService.remove(new QueryWrapper<ProjectChatEntity>().eq("project", finishTodoItemDTO.getProjectId()).eq("lawyer", finishTodoItemDTO.getUserId()));
        systemMessageService.save(SystemMessageEntity.builder().receiver(finishTodoItemDTO.getUserId()).content("您对项目：" + projectBaseService.getById(finishTodoItemDTO.getProjectId()).getProjectName() + " 已结束。").createTime(finishTodoItemDTO.getDate().getTime()).build());
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.END_SERVICE_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());
      case 11:
        //项目开始
        //1、修改项目状态
        //2、用户新增一条系统消息
        projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.START_PROJECT_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());
        break;
      case 12:
        //项目结束
        //1、修改项目状态
        //2、设置服务记录的结束时间
        //3、删除聊天对象记录
        //4、企业用户和律师用户新增一条系统消息
        StatisticalLawyerEntity lawyerEntity = statisticalLawyerService.getOne(new QueryWrapper<StatisticalLawyerEntity>().eq("project", finishTodoItemDTO.getProjectId()).isNull("end_time"));
        lawyerEntity.setEndTime(finishTodoItemDTO.getDate());
        lawyerEntity.setCost((lawyerEntity.getEndTime().getTime() - lawyerEntity.getStartTime().getTime()) / (SystemConstant.MS_OF_DAY * SystemConstant.MONTH_DAY) * chargeStandard);
        statisticalLawyerService.updateById(lawyerEntity);
        projectChatService.remove(new QueryWrapper<ProjectChatEntity>().eq("project", finishTodoItemDTO.getProjectId()).eq("lawyer", finishTodoItemDTO.getUserId()));
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.END_PROJECT_TO_COMPANY).createTime(finishTodoItemDTO.getDate().getTime()).build());
        systemMessageService.save(SystemMessageEntity.builder().receiver(finishTodoItemDTO.getOtherId()).content("您对项目：" + projectBaseService.getById(finishTodoItemDTO.getProjectId()).getProjectName() + " 已结束。").createTime(finishTodoItemDTO.getDate().getTime()).build());
        if (projectCompanyEvaluationService.getOne(new QueryWrapper<ProjectCompanyEvaluationEntity>().eq("project", finishTodoItemDTO.getProjectId())) != null) {
          projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.END_HAVE_EVALUATION);
        } else {
          projectBaseService.updateStatus(project, ProjectConstant.ProjectStatusEnum.END_WAIT_TO_EVALUATION);
        }
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
        projectMessageService.save(ProjectMessageEntity.builder().project(finishTodoItemDTO.getProjectId()).receiver(SystemConstant.ROLE_COM).content(SystemConstant.CHOOSE_LAWYER_PAST_DUE).createTime(finishTodoItemDTO.getDate().getTime()).build());
        projectUserTodoItemService.finishItemWithSystem(finishTodoItemDTO.getProjectId(), finishTodoItemDTO.getItemKey(), finishTodoItemDTO.getDate());
        break;
    }
  }
}
