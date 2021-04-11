package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.vo.*;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
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

//  /**
//   * 获取指定状态下的项目
//   */
//  @RequestMapping("/status/list")
//  public R getList(@RequestBody String status) {
//    List<Integer> integers = JSONObject.parseObject(status).getJSONArray("status").toJavaList(Integer.class);
//    List<ProjectBaseEntity> list =  projectBaseService.getByStatus(integers);
//    return R.ok().put("list", list);
//  }


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
  public R determineUnderTake(@PathVariable("id") Long id, @RequestBody ProjectLawyerCarryVo projectLawyerCarryVo) {
    projectBaseService.determineUnderTake(id, projectLawyerCarryVo);
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
  public R dealChangeLawyer(@PathVariable("id") Long id, @RequestBody ProjectLawyerDealChangeLawyerVo projectLawyerDealChangeLawyerVo) {
    projectBaseService.dealChangeLawyer(id, projectLawyerDealChangeLawyerVo);
    return R.ok();
  }

  /**
   * 系统处理申诉
   */
  @PostMapping("/{id}/dealComplaint")
  public R dealComplaint(@PathVariable("id") Long id, @RequestBody ProjectComplaintEntity projectComplaintEntity) {
    projectBaseService.dealComplaint(id, projectComplaintEntity);
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
   * 归档项目
   */
  @PostMapping("/{id}/archive")
  public R archive(@PathVariable("id") Long id, @RequestBody ProjectArchiveEntity projectArchiveEntity) {
    projectBaseService.archive(id, projectArchiveEntity);
    return R.ok();
  }

  /**
   * 获取项目的服务方案等级
   */
  @GetMapping("/{id}/level")
  public R getLevel(@PathVariable("id") Long id) {
    Long level = projectBaseService.getLevel(id);

    return R.ok().put("level", level);
  }

  /**
   * 获取特定企业用户的历史咨询项目列表
   */
  @RequestMapping("/company/{id}/list")
  public R history(@PathVariable("id") String id) {
    List<ProjectBaseEntity> list = projectBaseService.history(id);

    return R.ok().put("list", list);
  }

  /**
   * 获取特定律师用户的新分配项目
   */
  @RequestMapping("/{lawyerId}/newList")
  public R newList(@PathVariable("lawyerId") String lawyerId) {
    List<ProjectBaseEntity> list = projectBaseService.newList(lawyerId);

    return R.ok().put("list", list);
  }

  /**
   * 获取特定律师用户的在办项目
   */
  @RequestMapping("/{lawyerId}/nowList")
  public R nowList(@PathVariable("lawyerId") String lawyerId) {
    List<ProjectBaseEntity> list = projectBaseService.nowList(lawyerId);

    return R.ok().put("list", list);
  }

  /**
   * 获取特定律师用户的已结束项目
   */
  @RequestMapping("/{lawyerId}/endList")
  public R endList(@PathVariable("lawyerId") String lawyerId) {
    List<WorkRecordVo> list = projectBaseService.endList(lawyerId);

    return R.ok().put("list", list);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, String> params) {
    List<ProjectBaseEntity> list = projectBaseService.query(params);

    return R.ok().put("list", list);
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
