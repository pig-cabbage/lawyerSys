package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.vo.ChooseLawyerVo;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import cabbage.project.lawyerSys.vo.ProjectPlanVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Map;

/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectBaseService extends IService<ProjectBaseEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void audit(Long id, ProjectAuditEntity audit);

  void distributePlan(Long id, ProjectPlanVo projectPlan) throws ParseException;

  void pay(Long id);

  void chooseLawyer(Long id, ChooseLawyerVo chooseLawyerVo);

  void remindPay(Long id);

  void distributeLawyer(Long id, DistributeLawyerVo distributeLawyerVo);

  void remindUnderTake(Long id, Long distributeRecordId);

  void determineUnderTake(Long id, ProjectLawyerCarryEntity projectLawyerCarryEntity);

  void changeLawyer(Long id, ProjectUserChangeLawyerEntity projectUserChangeLawyerEntity);

  void changeLawyerAudit(Long id, ProjectChangeLawyerAuditEntity projectChangeLawyerAuditEntity);

  void dealChangeLawyer(Long id, ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity);

  void objection(Long id, ProjectCompanyObjectionEntity projectCompanyObjectionEntity);

  void evaluation(Long id, ProjectCompanyEvaluationEntity projectCompanyEvaluationEntity);

  void renewal(Long id, ProjectPlanEntity projectPlanEntity);

  void updateStatus(ProjectBaseEntity project, ProjectConstant.ProjectStatusEnum status);
}

