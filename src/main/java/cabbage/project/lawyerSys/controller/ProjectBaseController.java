package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.vo.ChooseLawyerVo;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import cabbage.project.lawyerSys.vo.ProjectPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;


/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/project")
public class ProjectBaseController {
  @Autowired
  private ProjectBaseService projectBaseService;

  /**
   * 审核项目
   */
  @PostMapping("/{id}/system/audit")
  public R audit(@PathVariable("id") Long id, @RequestBody ProjectAuditEntity audit) {
    projectBaseService.audit(id, audit);
    return R.ok();
  }

  /**
   * 分配服务方案
   */
  @PostMapping("/{id}/system/distributePlan")
  public R distributePlan(@PathVariable("id") Long id, @RequestBody ProjectPlanVo projectPlan) throws ParseException {
    projectBaseService.distributePlan(id, projectPlan);
    return R.ok();
  }

  /**
   * 提醒律师支付
   */
  @GetMapping("/{id}/remindPay")
  public R remindPay(@PathVariable("id") Long id) {
    projectBaseService.remindPay(id);
    return R.ok();
  }

  /**
   * 支付费用
   */
  @GetMapping("/{id}/pay")
  public R pay(@PathVariable("id") Long id) {
    projectBaseService.pay(id);
    return R.ok();
  }

  /**
   * 选择律师
   */
  @PostMapping("/{id}/chooseLawyer")
  public R chooseLawyer(@PathVariable("id") Long id, @RequestBody ChooseLawyerVo chooseLawyerVo) {
    projectBaseService.chooseLawyer(id, chooseLawyerVo);
    return R.ok();
  }

  /**
   * 分配律师
   */
  @PostMapping("/{id}/distributeLawyer")
  public R distributeLawyer(@PathVariable("id") Long id, @RequestBody DistributeLawyerVo distributeLawyerVo) {
    projectBaseService.distributeLawyer(id, distributeLawyerVo);
    return R.ok();
  }

  /**
   * 提醒律师决定是否承接项目
   */
  @GetMapping("/{id}/remindUnderTake")
  public R remindUnderTake(@PathVariable("id") Long id, @RequestParam("distributeRecordId") Long distributeRecordId) {
    projectBaseService.remindUnderTake(id, distributeRecordId);
    return R.ok();
  }

  /**
   * 律师用户决定是否承接项目
   */
  @PostMapping("/{id}/determineUnderTake")
  public R determineUnderTake(@PathVariable("id") Long id, @RequestBody ProjectLawyerCarryEntity projectLawyerCarryEntity) {
    projectBaseService.determineUnderTake(id, projectLawyerCarryEntity);
    return R.ok();
  }

  /**
   * 用户更换律师申请
   */
  @PostMapping("/{id}/changeLawyer")
  public R changeLawyer(@PathVariable("id") Long id, @Validated @RequestBody ProjectUserChangeLawyerEntity projectUserChangeLawyerEntity) {
    projectBaseService.changeLawyer(id, projectUserChangeLawyerEntity);
    return R.ok();
  }

  /**
   * 系统审核用户更换律师申请
   */
  @PostMapping("/{id}/changeLawyerAudit")
  public R changeLawyerAudit(@PathVariable("id") Long id, @RequestBody ProjectChangeLawyerAuditEntity projectChangeLawyerAuditEntity) {
    projectBaseService.changeLawyerAudit(id, projectChangeLawyerAuditEntity);
    return R.ok();
  }

  /**
   * 律师处理企业更换律师申请
   */
  @PostMapping("/{id}/dealChangeLawyer")
  public R dealChangeLawyer(@PathVariable("id") Long id, @RequestBody ProjectLawyerDealChangeLawyerEntity projectLawyerDealChangeLawyerEntity) {
    projectBaseService.dealChangeLawyer(id, projectLawyerDealChangeLawyerEntity);
    return R.ok();
  }

  /**
   * 企业对服务方案异议
   */
  @PostMapping("/{id}/objection")
  public R objection(@PathVariable("id") Long id, @RequestBody ProjectCompanyObjectionEntity projectCompanyObjectionEntity) {
    projectBaseService.objection(id, projectCompanyObjectionEntity);
    return R.ok();
  }

  /**
   * 企业续期项目
   */
  @PostMapping("/{id}/renewal")
  public R renewal(@PathVariable("id") Long id, @RequestBody ProjectPlanEntity projectPlanEntity) {
    projectBaseService.renewal(id, projectPlanEntity);
    return R.ok();
  }

  /**
   * 企业评价项目
   */
  @PostMapping("/{id}/evaluation")
  public R evaluation(@PathVariable("id") Long id, @RequestBody ProjectCompanyEvaluationEntity projectCompanyEvaluationEntity) {
    projectBaseService.evaluation(id, projectCompanyEvaluationEntity);
    return R.ok();
  }


  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectBaseService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectBaseEntity projectBase = projectBaseService.getById(id);

    return R.ok().put("projectBase", projectBase);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectBaseEntity projectBase) {
    projectBaseService.save(projectBase);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectBaseEntity projectBase) {
    projectBaseService.updateById(projectBase);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectBaseService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
