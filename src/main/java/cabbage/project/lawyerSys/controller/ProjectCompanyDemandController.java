package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业咨询记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/project/company/demand")
public class ProjectCompanyDemandController {
  @Autowired
  private ProjectCompanyDemandService projectCompanyDemandService;

  /**
   * 新建咨询
   */
  @PostMapping("/add")
  public R add(@RequestBody ProjectCompanyDemandEntity entity) {
    projectCompanyDemandService.add(entity);
    return R.ok();
  }


  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectCompanyDemandService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectCompanyDemandEntity projectCompanyDemand = projectCompanyDemandService.getById(id);

    return R.ok().put("projectCompanyDemand", projectCompanyDemand);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectCompanyDemandEntity projectCompanyDemand) {
    projectCompanyDemandService.save(projectCompanyDemand);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectCompanyDemandEntity projectCompanyDemand) {
    projectCompanyDemandService.updateById(projectCompanyDemand);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectCompanyDemandService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
