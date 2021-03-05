package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyDemandLawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业选择律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/project/demandLawyer")
public class ProjectCompanyDemandLawyerController {
  @Autowired
  private ProjectCompanyDemandLawyerService projectCompanyDemandLawyerService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectCompanyDemandLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyer = projectCompanyDemandLawyerService.getById(id);

    return R.ok().put("projectCompanyDemandLawyer", projectCompanyDemandLawyer);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyer) {
    projectCompanyDemandLawyerService.save(projectCompanyDemandLawyer);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyer) {
    projectCompanyDemandLawyerService.updateById(projectCompanyDemandLawyer);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectCompanyDemandLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
