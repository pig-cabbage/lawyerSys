package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectCompanyEvaluationEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业评价记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/companyEvaluate")
public class ProjectCompanyEvaluationController {
  @Autowired
  private ProjectCompanyEvaluationService projectCompanyEvaluationService;

  /**
   * 获取某个特定项目对应的用户评价
   */
  @RequestMapping("/{projectId}/info")
  public R getInfo(@PathVariable("projectId") Long projectId) {
    ProjectCompanyEvaluationEntity projectCompanyEvaluation = projectCompanyEvaluationService.getInfo(projectId);

    return R.ok().put("projectCompanyEvaluation", projectCompanyEvaluation);
  }


  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectCompanyEvaluationService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectCompanyEvaluationEntity projectCompanyEvaluation = projectCompanyEvaluationService.getById(id);

    return R.ok().put("projectCompanyEvaluation", projectCompanyEvaluation);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectCompanyEvaluationEntity projectCompanyEvaluation) {
    projectCompanyEvaluationService.save(projectCompanyEvaluation);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectCompanyEvaluationEntity projectCompanyEvaluation) {
    projectCompanyEvaluationService.updateById(projectCompanyEvaluation);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectCompanyEvaluationService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
