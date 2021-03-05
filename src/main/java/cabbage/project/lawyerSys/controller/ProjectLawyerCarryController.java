package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectLawyerCarryEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerCarryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 律师承接项目记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("lawyersys/projectlawyercarry")
public class ProjectLawyerCarryController {
  @Autowired
  private ProjectLawyerCarryService projectLawyerCarryService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectLawyerCarryService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectLawyerCarryEntity projectLawyerCarry = projectLawyerCarryService.getById(id);

    return R.ok().put("projectLawyerCarry", projectLawyerCarry);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectLawyerCarryEntity projectLawyerCarry) {
    projectLawyerCarryService.save(projectLawyerCarry);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectLawyerCarryEntity projectLawyerCarry) {
    projectLawyerCarryService.updateById(projectLawyerCarry);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectLawyerCarryService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
