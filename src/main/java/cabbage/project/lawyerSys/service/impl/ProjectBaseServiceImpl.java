package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.clock.ScheduleJob;
import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.dao.ProjectBaseDao;
import cabbage.project.lawyerSys.dto.AutoFinishTodoItemDTO;
import cabbage.project.lawyerSys.dto.EndServiceDTO;
import cabbage.project.lawyerSys.dto.StartServiceDTO;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("projectBaseService")
public class ProjectBaseServiceImpl extends ServiceImpl<ProjectBaseDao, ProjectBaseEntity> implements ProjectBaseService {

  @Autowired
  private Scheduler scheduler;
  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private UserCompanyService userCompanyService;
  @Autowired
  private UserLawyerService userLawyerService;
  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ServiceLevelService serviceLevelService;
  @Autowired
  private ProjectAuditService projectAuditService;
  @Autowired
  private ProjectPlanService projectPlanService;
  @Autowired
  private ProjectUserTodoItemService projectUserTodoItemService;
  @Autowired
  private ProjectMessageService projectMessageService;
  @Autowired
  private ConstantTodoItemService constantTodoItemService;
  @Autowired
  private ProjectCompanyPayService projectCompanyPayService;
  @Autowired
  private ProjectCompanyDemandLawyerService projectCompanyDemandLawyerService;
  @Autowired
  private ProjectLawyerService projectLawyerService;
  @Autowired
  private ProjectLawyerCarryService projectLawyerCarryService;
  @Autowired
  private ProjectUserChangeLawyerService projectUserChangeLawyerService;
  @Autowired
  private ProjectChangeLawyerAuditService projectChangeLawyerAuditService;
  @Autowired
  private ProjectLawyerDealChangeLawyerService projectLawyerDealChangeLawyerService;
  @Autowired
  private ProjectCompanyObjectionService projectCompanyObjectionService;
  @Autowired
  private ProjectCompanyDemandService projectCompanyDemandService;
  @Autowired
  private ProjectCompanyEvaluationService projectCompanyEvaluationService;
  @Autowired
  private ProjectArchiveService projectArchiveService;
  @Autowired
  private StatisticalLawyerService statisticalLawyerService;
  @Autowired
  private ProjectComplaintService projectComplaintService;
  @Autowired
  private SystemMessageService systemMessageService;

  @Override
  public List<ProjectBaseEntity> query(Map<String, String> params) {
    QueryWrapper<ProjectBaseEntity> wrapper = new QueryWrapper<>();
    wrapper.in("status", JSONObject.parseArray(params.get("status"), Integer.class));
    System.out.println(params.get("key"));
    Assert.isNotBlank((String) params.get("key"), key -> {
      wrapper.like("project_name", key);
    });
    return this.list(wrapper);
  }

  /**
   * 新增一条审核记录
   * 修改项目状态
   * 企业用户新增一条系统消息
   */
  @Override
  @Transactional
  public void audit(Long id, ProjectAuditEntity audit) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.REGISTER_SUCCESS);
    Date date = new Date();
    audit.setProject(id);
    audit.setAuditTime(new Date());
    projectAuditService.save(audit);
    String appendMessage = "审核意见: " + audit.getAdvice();
    if (Integer.valueOf(1).equals(audit.getResult())) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_SUCCESS);
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.AUDIT_SUCCESS).appendContent(appendMessage).createTime(date.getTime()).build());
    } else {
      if (Integer.valueOf(0).equals(audit.getResult())) {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_FAIL);
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.AUDIT_FAIL).appendContent(appendMessage).createTime(date.getTime()).build());
      } else {
        throw RunException.builder().code(ExceptionCode.WRONG_DATA_CODE).build();
      }
    }
  }

  /**
   * 审核通过后开始分配服务方案
   * 1、新增一条分配记录
   * 2、修改项目状态
   * 3、企业用户新增一条系统消息
   * 4、企业用户新增一条待办事项
   */
  @Override
  @Transactional
  public void distributePlan(Long id, ProjectPlanVo projectPlan) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_SUCCESS);
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
    ProjectPlanEntity projectPlanEntity = new ProjectPlanEntity();
    BeanUtils.copyProperties(projectPlan, projectPlanEntity);
    projectPlanEntity.setProject(id);
    projectPlanEntity.setCreateTime(date);
    projectPlanEntity.setCost(servicePlanService.calculateCost(projectPlanEntity.getPlan(), -(projectPlanEntity.getEndTime().getTime() - projectPlanEntity.getStartTime().getTime()) / (30 * 24 * 60 * 60 * 1000)));
    projectPlanService.save(projectPlanEntity);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_PAY);
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.PAY_ITEM_KEY).createTime(date).build();
    Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, null, projectPlanEntity.getId(), date);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.WAIT_TO_PAY).createTime(date.getTime()).appendContent("代办事项").itemId(String.valueOf(itemId)).build());

  }

  /**
   * 提醒用户支付费用
   * 1、企业用户增加一条系统消息
   */
  @Override
  public void remindPay(Long id) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_PAY);
    Date date = new Date();
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.PAY_REMIND).createTime(date.getTime()).build());
  }

  /**
   * 分配完服务后用户支付
   * 1、新增一条支付记录
   * 2、修改项目状态，补充项目基本信息
   * 3、企业用户新增一条系统消息
   * 4、企业用户新增一条待办事项
   * 5、修改企业用户待办事项的状态
   */
  @Override
  @Transactional
  public void pay(Long id) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_PAY);
    Date date = new Date();
    ProjectPlanEntity projectPlanEntity = projectPlanService.getByProjectIdLatest(id);
    ProjectCompanyPayEntity projectCompanyPayEntity = ProjectCompanyPayEntity.builder()
        .distributePlan(projectPlanEntity.getId())
        .createTime(date).build();
    projectCompanyPayService.save(projectCompanyPayEntity);
    project.setPlan(projectPlanEntity.getPlan());
    project.setStartTime(projectPlanEntity.getStartTime());
    project.setEndTime(projectPlanEntity.getEndTime());
    project.setCost(projectPlanEntity.getCost());
    project.setStatus(ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER.getCode());
    this.updateById(project);
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.CHOOSE_LAWYER_KEY).createTime(date).build();
    Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, null, projectCompanyPayEntity.getId(), date);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.PAY_INFO).createTime(date.getTime()).appendContent("代办事项").itemId(String.valueOf(itemId)).build());
    //项目开始的定时任务
    this.startProject(project);
    //提醒续费的定时任务
    this.remindReNew(project, date);
    //项目结束的定时任务
    this.endProject(project);
    projectUserTodoItemService.finishItemWithUser(id, SystemConstant.PAY_ITEM_KEY, date);
  }

  /**
   * 支付完成后用户选择律师
   * 1、新增一条选择律师记录
   * 2、修改项目状态
   * 3、企业用户新增一条系统消息
   * 4、修改企业用户待办事项的状态
   */
  @Override
  @Transactional
  public void chooseLawyer(Long id, ChooseLawyerVo chooseLawyerVo) {
    Assert.isNotNull(id);
    Assert.isNotNull(chooseLawyerVo);
    ProjectBaseEntity project = this.getById(id);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
    Date date = new Date();
    ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyerEntity = ProjectCompanyDemandLawyerEntity.builder().build();
    BeanUtils.copyProperties(chooseLawyerVo, projectCompanyDemandLawyerEntity);
    projectCompanyDemandLawyerEntity.setProject(id);
    projectCompanyDemandLawyerEntity.setCreateTime(date);
    projectCompanyDemandLawyerService.save(projectCompanyDemandLawyerEntity);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.WAIT_TO_DIS_LAWYER).appendContent("律师信息").itemId(chooseLawyerVo.getRecommendLawyer() == 0 ? chooseLawyerVo.getDemandLawyer() : "系统推荐").createTime(date.getTime()).build());
    if (ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER.getCode() == project.getStatus()) {
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.CHOOSE_LAWYER_KEY, date);
    } else {
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RE_CHOOSE_LAWYER, date);
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RE_CHOOSE_LAWYER_LAWYER_AGREE, date);
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RE_CHOOSE_LAWYER_REFUSE_LAWYER, date);
    }
    updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_DIS_LAWYER);

  }

  /**
   * 管理员分配律师
   * 1、新增一条分配记录
   * 2、修改项目状态
   * 3、企业用户新增一条系统消息
   * 4、指定的律师新增一条系统消息
   * 5、指定的律师新增一条待办事项
   */
  @Override
  @Transactional
  public void distributeLawyer(Long id, DistributeLawyerVo distributeLawyerVo) {
    Assert.isNotNull(id);
    Assert.isNotNull(distributeLawyerVo);
    ProjectBaseEntity project = this.getById(id);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_DIS_LAWYER);
    Date date = new Date();
    Long entityId = projectLawyerService.addRecord(distributeLawyerVo, date);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_UNDER_TAKE);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.WAIT_TO_UNDER_TAKE).appendContent("律师信息").itemId(distributeLawyerVo.getLawyerId()).createTime(date.getTime()).build());
    userAccountService.addMessage(distributeLawyerVo.getLawyerId());
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(distributeLawyerVo.getLawyerId()).projectName(project.getProjectName()).item(SystemConstant.LAWYER_DETERMINE_UNDER_TAKE).createTime(date).build();
    Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), entityId, date);
    systemMessageService.save(SystemMessageEntity.builder().receiver(distributeLawyerVo.getLawyerId()).content(SystemConstant.LAWYER_DETERMINE_UNDER_TAKE_MES).appendContent("项目信息").itemId(String.valueOf(itemId)).createTime(date.getTime()).build());
  }

  /**
   * 提醒律师承接项目
   * 1、律师用户新增一条系统消息
   */
  @Override
  public void remindUnderTake(Long id, Long distributeRecordId) {
    Assert.isNotNull(id);
    Assert.isNotNull(distributeRecordId);
    ProjectLawyerEntity projectLawyerEntity = projectLawyerService.getById(distributeRecordId);
    systemMessageService.save(SystemMessageEntity.builder().receiver(projectLawyerEntity.getLawyer()).content(SystemConstant.UNDER_TAKE_REMIND).appendContent("项目信息").itemId(String.valueOf(id)).createTime(new Date().getTime()).build());
    userAccountService.addMessage(projectLawyerEntity.getLawyer());
  }

  /**
   * 律师用户决定是否承接项目
   * 1、新增一条操作记录
   * 2、1️⃣若律师承接项目，则修改项目状态，修改项目信息(若项目存在nowLawyer，则表明为重新选择律师,修改项目状态为正在服务，为nowLawyer为空，则表明为第一次分配律师，秀海项目状态为待开始服务)，需要新增一条服务记录, 服务记录的开始服务时间为：①若是项目的第一位律师，则设置为项目的开始时间；②若不是，则设置为上一位律师的结束服务时间的第二天
   * 2️⃣若律师拒绝承接项目，则修改项目状态，用户新增一条代办事项
   * 3、新增一条系统消息
   * 4、修改企业待办事项状态
   */
  @Override
  @Transactional
  public void determineUnderTake(Long id, ProjectLawyerCarryVo projectLawyerCarryVo) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectLawyerCarryVo);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Date date = new Date();
    ProjectLawyerCarryEntity projectLawyerCarryEntity = ProjectLawyerCarryEntity.builder().lawyer(projectLawyerCarryVo.getLawyer())
        .carry(projectLawyerCarryVo.getCarry()).createTime(date)
        .reason(projectLawyerCarryVo.getReason()).build();
    projectLawyerCarryService.save(projectLawyerCarryEntity);
    if (Integer.valueOf(0).equals(projectLawyerCarryEntity.getCarry())) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER);
      ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER_LAWYER_REFUSE).createTime(date).build();
      Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, null, projectLawyerCarryEntity.getId(), date);
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.REFUSE_UNDER_TAKE).appendContent("拒绝理由：" + projectLawyerCarryVo.getReason()).itemId(String.valueOf(itemId)).createTime(new Date().getTime()).build());
    } else {
      ProjectLawyerEntity projectLawyerEntity = projectLawyerService.getById(projectLawyerCarryEntity.getLawyer());
      boolean isFirst = true;
      if (project.getNowLawyer() == null) {
        project.setStatus(ProjectConstant.ProjectStatusEnum.WAIT_TO_START_SERVICE.getCode());

      } else {
        isFirst = false;
        project.setStatus(ProjectConstant.ProjectStatusEnum.SERVICING.getCode());
      }
      project.setNowLawyer(projectLawyerEntity.getLawyer());
      this.updateById(project);
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.ACCEPT_UNDER_TAKE).createTime(new Date().getTime()).build());
      StartServiceDTO startServiceDTO = StartServiceDTO.builder().project(id)
          .company(project.getCompany()).companyName(userCompanyService.getByAccount(project.getCompany()).getName())
          .lawyer(projectLawyerEntity.getLawyer()).lawyerName(userLawyerService.getByAccount(projectLawyerEntity.getLawyer()).getName()).plan(project.getPlan()).startTime(project.getStartTime()).build();
      statisticalLawyerService.startService(startServiceDTO, isFirst);
    }
    projectUserTodoItemService.finishItemWithUser(id, SystemConstant.LAWYER_DETERMINE_UNDER_TAKE, date);
  }

  /**
   * 用户申请更换律师
   * 1、新增一条申请记录
   * 2、更改项目状态
   */
  @Override
  @Transactional
  public void changeLawyer(Long id, ProjectUserChangeLawyerEntity projectUserChangeLawyerEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectUserChangeLawyerEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_START_SERVICE, ProjectConstant.ProjectStatusEnum.SERVICING);
    Date date = new Date();
    projectUserChangeLawyerEntity.setProject(id);
    projectUserChangeLawyerEntity.setLawyer(project.getNowLawyer());
    projectUserChangeLawyerEntity.setCreateTime(date);
    projectUserChangeLawyerEntity.setStatus(0);
    projectUserChangeLawyerService.save(projectUserChangeLawyerEntity);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.CHANGE_LAWYER_STAGE);
  }

  /**
   * 系统审核更换律师申请
   * 1、1️⃣审核不通过：修改项目状态， 发起方新增一条系统消息
   * 2️⃣审核通过：（1）发起方为企业：律师新增一条系统消息和待办事项， 企业新增一条系统消息,修改项目状态
   * (2) 发起方为律师:律师新增一条系统消息， 企业新增一条系统消息和待办事项. 修改项目状态，
   * 2、修改申请记录状态
   */
  @Override
  @Transactional
  public void changeLawyerAudit(Long id, ProjectChangeLawyerAuditEntity projectChangeLawyerAuditEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectChangeLawyerAuditEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.CHANGE_LAWYER_STAGE);
    Date date = new Date();
    projectChangeLawyerAuditEntity.setCreateTime(date);
    projectChangeLawyerAuditService.save(projectChangeLawyerAuditEntity);
    ProjectUserChangeLawyerEntity projectUserChangeLawyerEntity = projectUserChangeLawyerService.getById(projectChangeLawyerAuditEntity.getChangeLawyer());
    Assert.isEqual(0, projectUserChangeLawyerEntity.getStatus());
    if (Integer.valueOf(0).equals(projectChangeLawyerAuditEntity.getResult())) {
      if (project.getStartTime().getTime() > date.getTime()) {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_START_SERVICE);
      } else {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
      }
      if (Integer.valueOf(0).equals(projectUserChangeLawyerEntity.getCreator())) {
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.CHANGE_LAWYER_AUDIT_FAIL).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).createTime(date.getTime()).build());
      } else {
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.CHANGE_LAWYER_AUDIT_FAIL).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).createTime(date.getTime()).build());
      }
    } else {
      if (Integer.valueOf(0).equals(projectUserChangeLawyerEntity.getCreator())) {
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_COMPANY).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).createTime(date.getTime()).build());
        ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getNowLawyer()).projectName(project.getProjectName()).item(SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER).createTime(date).build();
        Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), projectChangeLawyerAuditEntity.getId(), date);
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_LAWYER).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).itemId(String.valueOf(itemId)).createTime(date.getTime()).build());

        updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_LAWYER_TO_DEAL_CHANGE_LAWYER);
      } else {
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_LAWYER).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).createTime(date.getTime()).build());
        ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER).createTime(date).build();
        Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, project.getNowLawyer(), projectChangeLawyerAuditEntity.getId(), date);
        projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_COMPANY).appendContent("审核意见：" + projectChangeLawyerAuditEntity.getAdvice()).itemId(String.valueOf(itemId)).createTime(date.getTime()).build());
        updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
        EndServiceDTO endServiceDTO = EndServiceDTO.builder().project(id).lawyer(project.getNowLawyer()).company(project.getCompany()).date(date).build();
        statisticalLawyerService.endService(endServiceDTO, date);
      }
    }
    projectUserChangeLawyerEntity.setStatus(1);
    projectUserChangeLawyerService.updateById(projectUserChangeLawyerEntity);
  }

  /**
   * 律师处理用户更换律师申请
   * 1、新增一条操作记录
   * 2、1️⃣同意申请：企业生成一条系统消息，企业生成一个待办事项， 修改项目状态
   * 2️⃣提出申诉：企业生成一条系统消息，修改项目状态
   * 3、律师完成待办事项
   */
  @Override
  @Transactional
  public void dealChangeLawyer(Long id, ProjectLawyerDealChangeLawyerVo projectLawyerDealChangeLawyerVo) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectLawyerDealChangeLawyerVo);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_LAWYER_TO_DEAL_CHANGE_LAWYER);
    Date date = new Date();
    ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity = ProjectLawyerDealChangeLawyerEntity.builder().changeLawyer(projectLawyerDealChangeLawyerVo.getChangeLawyer())
        .result(projectLawyerDealChangeLawyerVo.getResult()).reason(projectLawyerDealChangeLawyerVo.getReason())
        .createTime(date).build();
    projectLawyerDealChangeLawyerService.save(projectLawyerDealChangeLawyerEntity);
    if (Integer.valueOf(0).equals(projectLawyerDealChangeLawyerEntity.getResult())) {
      ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER_LAWYER_AGREE).createTime(date).build();
      Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), projectLawyerDealChangeLawyerEntity.getId(), date);
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.LAWYER_AGREE_CHANGE_LAWYER).appendContent("代办事项").itemId(String.valueOf(itemId)).createTime(date.getTime()).build());
      updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
      EndServiceDTO endServiceDTO = EndServiceDTO.builder().project(id).lawyer(project.getNowLawyer()).company(project.getCompany()).date(date).chargeStandard(serviceLevelService.getById(servicePlanService.getById(project.getPlan()).getServiceLevel()).getChargeStandard()).build();
      statisticalLawyerService.endService(endServiceDTO, date);
    } else {
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.LAWYER_COMPLAINT).appendContent("申诉理由：" + projectLawyerDealChangeLawyerVo.getReason()).createTime(date.getTime()).build());
      updateStatus(project, ProjectConstant.ProjectStatusEnum.COMPLAINT_STAGE);
    }

    projectUserTodoItemService.finishItemWithUser(id, SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER, date);
  }

  /**
   * 企业对服务方案提出异议
   * 1、新增一条异议记录记录
   * 2、补充用户需求信息
   * 3、修改项目状态
   * 4、企业完成待办事项
   */
  @Override
  @Transactional
  public void objection(Long id, ProjectCompanyObjectionEntity projectCompanyObjectionEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectCompanyObjectionEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_PAY);
    Date date = new Date();
    projectCompanyObjectionEntity.setCreateTime(date);
    projectCompanyObjectionService.save(projectCompanyObjectionEntity);
    ProjectCompanyDemandEntity projectCompanyDemandEntity = projectCompanyDemandService.getById(project.getDemand());
    projectCompanyDemandEntity.setObjection(projectCompanyObjectionEntity.getId());
    projectCompanyDemandService.updateById(projectCompanyDemandEntity);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_SUCCESS);
    projectUserTodoItemService.finishItemWithUser(id, SystemConstant.PAY_ITEM_KEY, date);
  }


  /**
   * 企业作出评价
   * 1、新增一条评价记录
   * 2、代理律师新增一条系统消息
   * 3、修改项目状态
   * 4、企业完成待办事项
   */
  @Override
  @Transactional
  public void evaluation(Long id, ProjectCompanyEvaluationEntity projectCompanyEvaluationEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectCompanyEvaluationEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING, ProjectConstant.ProjectStatusEnum.END_WAIT_TO_EVALUATION);
    Date date = new Date();
    projectCompanyEvaluationEntity.setProject(id);
    projectCompanyEvaluationEntity.setCreateTime(date);
    projectCompanyEvaluationService.save(projectCompanyEvaluationEntity);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.COMPANY_EVALUATION).appendContent("{分数: " + String.valueOf(projectCompanyEvaluationEntity.getScore()) + ", 评价内容: " + projectCompanyEvaluationEntity.getContent() + "}").createTime(date.getTime()).build());
    if (ProjectConstant.ProjectStatusEnum.END_WAIT_TO_EVALUATION.getCode() == project.getStatus()) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.END_HAVE_EVALUATION);
    }
    projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RENEW_PROJECT, date);
  }

  /**
   * 企业续期项目
   * 1、修改项目基本信息表的结束时间和费用
   * 2、新增一条分配服务记录
   * 3、代理律师新增一条系统消息
   * 4、删除就得项目到期的定时任务，新增新的项目到期的定时任务
   * 5、修改代办事项状态
   */
  @Override
  @Transactional
  public void renewal(Long id, ProjectPlanEntity projectPlanEntity) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
    Date date = new Date();
    project.setEndTime(projectPlanEntity.getEndTime());
    project.setCost(project.getCost().add(projectPlanEntity.getCost()));
    this.updateById(project);
    projectPlanEntity.setProject(id);
    projectPlanEntity.setCreateTime(date);
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    projectPlanService.save(projectPlanEntity);
    projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.RENEWAL).appendContent("服务时间: " + dft.format(project.getStartTime()) + "-" + dft.format(project.getEndTime())).createTime(date.getTime()).build());
    try {
      scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.END_PROJECT), String.valueOf(id)));
      this.endProject(project);
      this.remindReNew(project, date);
    } catch (SchedulerException e) {
      throw RunException.builder().code(ExceptionCode.DELETE_SCHEDULE_ERROR).build();
    }
  }

  /**
   * 归档项目
   * 1、新增一条归档记录
   * 2、新增一条律师服务记录
   * 2、修改项目状态
   */
  @Override
  @Transactional
  public void archive(Long id, ProjectArchiveEntity projectArchiveEntity) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.END_HAVE_EVALUATION);
    Date date = new Date();
    projectArchiveEntity.setProject(id);
    projectArchiveEntity.setCreateTime(date);
    projectArchiveService.save(projectArchiveEntity);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.HAVE_DOCUMENT);
  }

  /**
   * 系统处理申诉
   * 1、新增一条处理记录
   * 2、1️⃣驳回律师申诉申请：企业和律师都生成一条系统消息，企业生成一个待办事项， 修改项目状态
   * 2️⃣驳回企业更换律师申请：企业和律师都生成一条系统消息，修改项目状态
   */
  @Override
  @Transactional
  public void dealComplaint(Long id, ProjectComplaintEntity projectComplaintEntity) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.COMPLAINT_STAGE);
    Date date = new Date();
    projectComplaintEntity.setCreateTime(date);
    projectComplaintService.save(projectComplaintEntity);
    if (projectComplaintEntity.getResult() == 0) {
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.DEAL_COMPLAINT_REFUSE_LAWYER_TO_LAWYER).appendContent("处理意见：" + projectComplaintEntity.getAdvice()).createTime(date.getTime()).build());
      ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER_REFUSE_LAWYER).createTime(date).build();
      Long itemId = projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), projectComplaintEntity.getId(), date);
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.DEAL_COMPLAINT_REFUSE_LAWYER_TO_COMAPNY).appendContent("处理意见：" + projectComplaintEntity.getAdvice()).itemId(String.valueOf(itemId)).createTime(date.getTime()).build());
      updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
      EndServiceDTO endServiceDTO = EndServiceDTO.builder().project(id).lawyer(project.getNowLawyer()).company(project.getCompany()).date(date).build();
      statisticalLawyerService.endService(endServiceDTO, date);
    } else {
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_COM).content(SystemConstant.DEAL_COMPLAINT_REFUSE_COMPANY_TO_COMPANY).appendContent("处理意见：" + projectComplaintEntity.getAdvice()).createTime(date.getTime()).build());
      projectMessageService.save(ProjectMessageEntity.builder().project(id).receiver(SystemConstant.ROLE_LAW).content(SystemConstant.DEAL_COMPLAINT_REFUSE_COMPANY_TO_LAWYER).appendContent("处理意见：" + projectComplaintEntity.getAdvice()).createTime(date.getTime()).build());
      updateStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
    }

  }

  @Override
  public Long getLevel(Long id) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    ServicePlanEntity servicePlanEntity = servicePlanService.getById(project.getPlan());
    return serviceLevelService.getById(servicePlanEntity.getServiceLevel()).getLevel();
  }

  @Override
  public List<ProjectBaseEntity> history(String id) {
    return this.list(new QueryWrapper<ProjectBaseEntity>().eq("company", id));
  }

  @Override
  public List<ProjectBaseEntity> newList(String lawyerId) {
    List<ProjectBaseEntity> list = this.list(new QueryWrapper<ProjectBaseEntity>().eq("status", ProjectConstant.ProjectStatusEnum.WAIT_TO_UNDER_TAKE.getCode()));
    return list.stream().filter(item -> {
      return projectLawyerService.getLatestRecord(item.getId()).getLawyer().equals(lawyerId);
    }).collect(Collectors.toList());
  }

  @Override
  public List<ProjectBaseEntity> nowList(String lawyerId) {
    return this.list(new QueryWrapper<ProjectBaseEntity>().eq("now_lawyer", lawyerId).notIn("status", Arrays.asList(11, 15, 16)));
  }

  @Override
  public List<WorkRecordVo> endList(String lawyerId) {
    List<StatisticalLawyerEntity> statisticalLawyerEntities = statisticalLawyerService.list(new QueryWrapper<StatisticalLawyerEntity>().eq("lawyer", lawyerId));
    return statisticalLawyerEntities.stream().map(item -> {
      WorkRecordVo workRecordVo = new WorkRecordVo();
      BeanUtils.copyProperties(item, workRecordVo);
      return workRecordVo;
    }).collect(Collectors.toList());
  }

  //更新项目状态
  @Override
  public void updateStatus(ProjectBaseEntity project, ProjectConstant.ProjectStatusEnum status) {
    project.setStatus(status.getCode());
    this.updateById(project);
  }

  private void startProject(ProjectBaseEntity projectBaseEntity) {
    try {
      scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.START_PROJECT), String.valueOf(projectBaseEntity.getId())));
      AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(SystemConstant.START_PROJECT).projectId(projectBaseEntity.getId()).userId(projectBaseEntity.getCompany()).date(projectBaseEntity.getStartTime()).build();
      JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(SystemConstant.START_PROJECT), String.valueOf(projectBaseEntity.getId())).build();
      jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
      jobDetail.getJobDataMap().put("project", projectBaseEntity);
      Trigger startServing = TriggerBuilder.newTrigger().withIdentity(String.valueOf(SystemConstant.START_PROJECT), String.valueOf(projectBaseEntity.getId())).startAt(projectBaseEntity.getStartTime()).build();
      try {
        scheduler.scheduleJob(jobDetail, startServing);
        scheduler.start();
      } catch (SchedulerException e) {
        throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  private void remindReNew(ProjectBaseEntity projectBaseEntity, Date date) {
    try {
      scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.REMIND_RENEW), String.valueOf(projectBaseEntity.getId())));
      AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(SystemConstant.REMIND_RENEW).projectId(projectBaseEntity.getId()).userId(projectBaseEntity.getCompany()).date(date).build();
      JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(SystemConstant.REMIND_RENEW), String.valueOf(projectBaseEntity.getId())).build();
      jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
      jobDetail.getJobDataMap().put("project", projectBaseEntity);
      Trigger startServing = TriggerBuilder.newTrigger().withIdentity(String.valueOf(SystemConstant.REMIND_RENEW), String.valueOf(projectBaseEntity.getId())).startAt(new Date(projectBaseEntity.getEndTime().getTime() - SystemConstant.RENEW_GOV * SystemConstant.MS_OF_DAY)).build();
      try {
        scheduler.scheduleJob(jobDetail, startServing);
        scheduler.start();
      } catch (SchedulerException e) {
        throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  private void endProject(ProjectBaseEntity projectBaseEntity) {
    try {
      scheduler.deleteJob(new JobKey(String.valueOf(SystemConstant.END_PROJECT), String.valueOf(projectBaseEntity.getId())));
      AutoFinishTodoItemDTO finishTodoItemDTO = AutoFinishTodoItemDTO.builder().itemKey(SystemConstant.END_PROJECT).projectId(projectBaseEntity.getId()).userId(projectBaseEntity.getCompany()).otherId(projectBaseEntity.getNowLawyer()).date(projectBaseEntity.getEndTime()).build();
      JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(String.valueOf(SystemConstant.END_PROJECT), String.valueOf(projectBaseEntity.getId())).build();
      jobDetail.getJobDataMap().put("finishTodoItemDTO", finishTodoItemDTO);
      jobDetail.getJobDataMap().put("project", projectBaseEntity);
      jobDetail.getJobDataMap().put("chargeStandard", serviceLevelService.getById(servicePlanService.getById(projectBaseEntity.getPlan()).getServiceLevel()).getChargeStandard().doubleValue());
      Trigger startServing = TriggerBuilder.newTrigger().withIdentity(String.valueOf(SystemConstant.END_PROJECT), String.valueOf(projectBaseEntity.getId())).startAt(projectBaseEntity.getEndTime()).build();
      try {
        scheduler.scheduleJob(jobDetail, startServing);
        scheduler.start();
      } catch (SchedulerException e) {
        throw RunException.builder().code(ExceptionCode.CREATE_SCHEDULE_ERROR).build();
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

  }


}