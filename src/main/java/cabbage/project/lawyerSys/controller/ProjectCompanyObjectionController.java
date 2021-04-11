package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectCompanyObjectionEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyObjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业异议记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/objection")
public class ProjectCompanyObjectionController {
  @Autowired
  private ProjectCompanyObjectionService projectCompanyObjectionService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectCompanyObjectionService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectCompanyObjectionEntity projectCompanyObjection = projectCompanyObjectionService.getById(id);

    return R.ok().put("projectCompanyObjection", projectCompanyObjection);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectCompanyObjectionEntity projectCompanyObjection) {
    projectCompanyObjectionService.save(projectCompanyObjection);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectCompanyObjectionEntity projectCompanyObjection) {
    projectCompanyObjectionService.updateById(projectCompanyObjection);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectCompanyObjectionService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
