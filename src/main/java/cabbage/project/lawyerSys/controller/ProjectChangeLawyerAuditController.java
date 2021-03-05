package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.service.ProjectChangeLawyerAuditService;
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
@RequestMapping("api/project/changeLawyerAudit")
public class ProjectChangeLawyerAuditController {
  @Autowired
  private ProjectChangeLawyerAuditService projectChangeLawyerAuditService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectChangeLawyerAuditService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectChangeLawyerAuditEntity projectChangeLawyerAudit = projectChangeLawyerAuditService.getById(id);

    return R.ok().put("projectChangeLawyerAudit", projectChangeLawyerAudit);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectChangeLawyerAuditEntity projectChangeLawyerAudit) {
    projectChangeLawyerAuditService.save(projectChangeLawyerAudit);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectChangeLawyerAuditEntity projectChangeLawyerAudit) {
    projectChangeLawyerAuditService.updateById(projectChangeLawyerAudit);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectChangeLawyerAuditService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
