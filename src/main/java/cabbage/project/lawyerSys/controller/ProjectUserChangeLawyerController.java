package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectUserChangeLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectUserChangeLawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 用户更换律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/project/changeLawyer")
public class ProjectUserChangeLawyerController {
  @Autowired
  private ProjectUserChangeLawyerService projectUserChangeLawyerService;

  /**
   * 获取最近的发起方为企业的已审核通过的更换律师申请
   */
  @RequestMapping("/{projectId}/latestInfo")
  public R getLatestInfo(@PathVariable("projectId") Long projectId) {
    ProjectUserChangeLawyerEntity projectUserChangeLawyer = projectUserChangeLawyerService.getLatestInfo(projectId);

    return R.ok().put("projectUserChangeLawyer", projectUserChangeLawyer);
  }

  /**
   * 获取项目的未处理更换律师申请
   */
  @RequestMapping("/{projectId}/info")
  public R getInfo(@PathVariable("projectId") Long projectId) {
    ProjectUserChangeLawyerEntity projectUserChangeLawyer = projectUserChangeLawyerService.getInfo(projectId);

    return R.ok().put("projectUserChangeLawyer", projectUserChangeLawyer);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectUserChangeLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectUserChangeLawyerEntity projectUserChangeLawyer = projectUserChangeLawyerService.getById(id);

    return R.ok().put("projectUserChangeLawyer", projectUserChangeLawyer);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectUserChangeLawyerEntity projectUserChangeLawyer) {
    projectUserChangeLawyerService.save(projectUserChangeLawyer);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectUserChangeLawyerEntity projectUserChangeLawyer) {
    projectUserChangeLawyerService.updateById(projectUserChangeLawyer);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectUserChangeLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
