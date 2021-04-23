package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 项目分配律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/distributeLawyer")
public class ProjectLawyerController {
  @Autowired
  private ProjectLawyerService projectLawyerService;

  /**
   * 获取特定项目的最近一条分配记录
   */
  @RequestMapping("/{id}/latestRecord")
  public R getLatestRecord(@PathVariable("id") Long id) {
    ProjectLawyerEntity entity = projectLawyerService.getLatestRecord(id);

    return R.ok().put("entity", entity);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectLawyerEntity projectLawyer = projectLawyerService.getById(id);

    return R.ok().put("entity", projectLawyer);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectLawyerEntity projectLawyer) {
    projectLawyerService.save(projectLawyer);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectLawyerEntity projectLawyer) {
    projectLawyerService.updateById(projectLawyer);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
