package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectBaseDao;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.ChooseLawyerVo;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import cabbage.project.lawyerSys.vo.ProjectPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.Scheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service("projectBaseService")
public class ProjectBaseServiceImpl extends ServiceImpl<ProjectBaseDao, ProjectBaseEntity> implements ProjectBaseService {

  @Autowired
  private Scheduler scheduler;
  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ProjectAuditService projectAuditService;
  @Autowired
  private ProjectPlanService projectPlanService;
  @Autowired
  private ProjectUserTodoItemService projectUserTodoItemService;
  @Autowired
  private SystemMessageService systemMessageService;
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

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectBaseEntity> page = this.page(
        new Query<ProjectBaseEntity>().getPage(params),
        new QueryWrapper<ProjectBaseEntity>()
    );

    return new PageUtils(page);
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
    if (Integer.valueOf(1).equals(audit.getResult())) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_SUCCESS);
      systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.AUDIT_SUCCESS, String.valueOf(audit.getId()), date);
    } else {
      if (Integer.valueOf(0).equals(audit.getResult())) {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.AUDIT_FAIL);
        systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.AUDIT_FAIL, String.valueOf(audit.getId()), date);
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
    projectPlanEntity.setCost(servicePlanService.calculateCost(projectPlanEntity.getPlan(), (projectPlanEntity.getEndTime().getTime() - projectPlanEntity.getStartTime().getTime()) / 30 * 24 * 60 * 60 * 1000));
    projectPlanService.save(projectPlanEntity);
    updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_PAY);
    systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.WAIT_TO_PAY, String.valueOf(projectPlanEntity.getId()), date);
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.PAY_ITEM_KEY).createTime(date).build();
    projectUserTodoItemService.addItem(userTodoItemEntity, null, projectPlanEntity.getId(), date);
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
    systemMessageService.addMessageWithoutEventId(project.getCompany(), SystemConstant.SystemMessageEnum.PAY_REMIND, date);
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
    systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.PAY_INFO, String.valueOf(projectCompanyPayEntity.getId()), date);
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.CHOOSE_LAWYER_KEY).createTime(date).build();
    projectUserTodoItemService.addItem(userTodoItemEntity, null, projectCompanyPayEntity.getId(), date);
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
    updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_DIS_LAWYER);
    systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.WAIT_TO_DIS_LAWYER, String.valueOf(projectCompanyDemandLawyerEntity.getId()), date);
    if (ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER.getCode() == project.getStatus()) {
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.CHOOSE_LAWYER_KEY, date);
    } else {
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RE_CHOOSE_LAWYER, date);
      projectUserTodoItemService.finishItemWithUser(id, SystemConstant.RE_CHOOSE_LAWYER_LAWYER_AGREE, date);
    }
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
    systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.WAIT_TO_UNDER_TAKE, String.valueOf(entityId), date);
    systemMessageService.addMessage(distributeLawyerVo.getLawyerId(), SystemConstant.SystemMessageEnum.LAWYER_DETERMINE_UNDER_TAKE, String.valueOf(id), date);
    ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(distributeLawyerVo.getLawyerId()).projectName(project.getProjectName()).item(SystemConstant.LAWYER_DETERMINE_UNDER_TAKE).createTime(date).build();
    projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), entityId, date);
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
    systemMessageService.addMessageWithoutEventId(projectLawyerEntity.getLawyer(), SystemConstant.SystemMessageEnum.UNDER_TAKE_REMIND, new Date());
  }

  /**
   * 律师用户决定是否承接项目
   * 1、新增一条操作记录
   * 2、1️⃣若律师承接项目，则修改项目状态，修改项目信息
   * 2️⃣若律师拒绝承接项目，则修改项目状态
   * 3、新增一条系统消息
   * 4、修改企业待办事项状态
   */
  @Override
  @Transactional
  public void determineUnderTake(Long id, ProjectLawyerCarryEntity projectLawyerCarryEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectLawyerCarryEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Date date = new Date();
    projectLawyerCarryEntity.setCreateTime(date);
    projectLawyerCarryService.save(projectLawyerCarryEntity);
    if (Integer.valueOf(0).equals(projectLawyerCarryEntity.getCarry())) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_CHOOSE_LAWYER);
      systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.REFUSE_UNDER_TAKE, String.valueOf(projectLawyerCarryEntity.getId()), date);
    } else {
      ProjectLawyerEntity projectLawyerEntity = projectLawyerService.getById(projectLawyerCarryEntity.getLawyer());
      project.setNowLawyer(projectLawyerEntity.getLawyer());
      project.setStatus(ProjectConstant.ProjectStatusEnum.WAIT_TO_START_SERVICE.getCode());
      this.updateById(project);
      systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.ACCEPT_UNDER_TAKE, projectLawyerEntity.getLawyer(), date);
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
   * 2️⃣审核通过：（1）发起方为企业：律师新增一条系统消息和待办事项， 企业新增一条系统消息
   * (2) 发起方为律师:律师新增一条系统消息， 企业新增一条系统消息和待办事项. 修改项目状态
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
      if (project.getStartTime().getTime() < date.getTime()) {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_TO_START_SERVICE);
      } else {
        updateStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
      }
      if (Integer.valueOf(0).equals(projectUserChangeLawyerEntity.getCreator())) {
        systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_FAIL, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
      } else {
        systemMessageService.addMessage(project.getNowLawyer(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_FAIL, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
      }
    } else {
      if (Integer.valueOf(0).equals(projectUserChangeLawyerEntity.getCreator())) {
        systemMessageService.addMessage(project.getNowLawyer(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_LAWYER, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
        systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_COMPANY, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
        ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getNowLawyer()).projectName(project.getProjectName()).item(SystemConstant.DETERMINE_AGREE_CHANGE_LAWYER).createTime(date).build();
        projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), projectChangeLawyerAuditEntity.getId(), date);
        updateStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_LAWYER_TO_DEAL_CHANGE_LAWYER);
      } else {
        systemMessageService.addMessage(project.getNowLawyer(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_LAWYER, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
        systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_COMPANY, String.valueOf(projectChangeLawyerAuditEntity.getId()), date);
        ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER).createTime(date).build();
        projectUserTodoItemService.addItem(userTodoItemEntity, project.getNowLawyer(), projectChangeLawyerAuditEntity.getId(), date);
        updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
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
  public void dealChangeLawyer(Long id, ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity) {
    Assert.isNotNull(id);
    Assert.isNotNull(projectLawyerDealChangeLawyerEntity);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.WAIT_LAWYER_TO_DEAL_CHANGE_LAWYER);
    Date date = new Date();
    projectLawyerDealChangeLawyerEntity.setCreateTime(date);
    projectLawyerDealChangeLawyerService.save(projectLawyerDealChangeLawyerEntity);
    if (Integer.valueOf(0).equals(projectLawyerDealChangeLawyerEntity.getResult())) {
      systemMessageService.addMessageWithoutEventId(project.getCompany(), SystemConstant.SystemMessageEnum.LAWYER_AGREE_CHANGE_LAWYER, date);
      ProjectUserTodoItemEntity userTodoItemEntity = ProjectUserTodoItemEntity.builder().project(id).user(project.getCompany()).projectName(project.getProjectName()).item(SystemConstant.RE_CHOOSE_LAWYER_LAWYER_AGREE).createTime(date).build();
      projectUserTodoItemService.addItem(userTodoItemEntity, project.getCompany(), projectLawyerDealChangeLawyerEntity.getId(), date);
      updateStatus(project, ProjectConstant.ProjectStatusEnum.RE_CHOOSE_LAWYER);
    } else {
      systemMessageService.addMessage(project.getCompany(), SystemConstant.SystemMessageEnum.LAWYER_COMPLAINT, String.valueOf(projectLawyerDealChangeLawyerEntity.getId()), date);
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
    systemMessageService.addMessage(project.getNowLawyer(), SystemConstant.SystemMessageEnum.COMPANY_EVALUATION, String.valueOf(projectCompanyEvaluationEntity.getId()), date);
    if (ProjectConstant.ProjectStatusEnum.END_WAIT_TO_EVALUATION.getCode() == project.getStatus()) {
      updateStatus(project, ProjectConstant.ProjectStatusEnum.END_HAVE_EVALUATION);
    }
  }

  /**
   * 企业续期项目
   * 1、修改项目基本信息表的结束时间和费用
   * 2、新增一条分配服务记录
   * 3、代理律师新增一条系统消息
   */
  @Override
  @Transactional
  public void renewal(Long id, ProjectPlanEntity projectPlanEntity) {
    Assert.isNotNull(id);
    ProjectBaseEntity project = this.getById(id);
    Assert.isNotNull(project);
    Assert.isNotNull(project);
    Assert.isTrueStatus(project, ProjectConstant.ProjectStatusEnum.SERVICING);
    Date date = new Date();
    project.setEndTime(projectPlanEntity.getEndTime());
    project.setCost(project.getCost().add(projectPlanEntity.getCost()));
    this.updateById(project);
    projectPlanEntity.setProject(id);
    projectPlanEntity.setCreateTime(date);
    projectPlanService.save(projectPlanEntity);
    systemMessageService.addMessage(project.getNowLawyer(), SystemConstant.SystemMessageEnum.RENEWAL, String.valueOf(projectPlanEntity.getId()), date);
  }


  //更新项目状态
  @Override
  public void updateStatus(ProjectBaseEntity project, ProjectConstant.ProjectStatusEnum status) {
    project.setStatus(status.getCode());
    this.updateById(project);
  }


}