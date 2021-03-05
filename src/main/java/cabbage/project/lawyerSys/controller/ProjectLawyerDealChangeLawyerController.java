package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectLawyerDealChangeLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerDealChangeLawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 律师申诉记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("lawyersys/projectlawyercomplaint")
public class ProjectLawyerDealChangeLawyerController {
  @Autowired
  private ProjectLawyerDealChangeLawyerService projectLawyerDealChangeLawyerService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectLawyerDealChangeLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectLawyerDealChangeLawyerEntity projectLawyerComplaint = projectLawyerDealChangeLawyerService.getById(id);

    return R.ok().put("projectLawyerComplaint", projectLawyerComplaint);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectLawyerDealChangeLawyerEntity projectLawyerComplaint) {
    projectLawyerDealChangeLawyerService.save(projectLawyerComplaint);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectLawyerDealChangeLawyerEntity projectLawyerComplaint) {
    projectLawyerDealChangeLawyerService.updateById(projectLawyerComplaint);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectLawyerDealChangeLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
