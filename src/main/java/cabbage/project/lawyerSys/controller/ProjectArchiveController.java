package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectArchiveEntity;
import cabbage.project.lawyerSys.service.ProjectArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/archive")
public class ProjectArchiveController {
  @Autowired
  private ProjectArchiveService projectArchiveService;

  /**
   * 信息
   */
  @RequestMapping("/{projectId}/info")
  public R getByProject(@PathVariable("projectId") Long projectId) {
    ProjectArchiveEntity projectArchive = projectArchiveService.getByProject(projectId);

    return R.ok().put("projectArchive", projectArchive);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectArchiveService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectArchiveEntity projectArchive = projectArchiveService.getById(id);

    return R.ok().put("projectArchive", projectArchive);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectArchiveEntity projectArchive) {
    projectArchiveService.save(projectArchive);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectArchiveEntity projectArchive) {
    projectArchiveService.updateById(projectArchive);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectArchiveService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
