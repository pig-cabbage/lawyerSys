package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectCompanyPayEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业支付记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/project/pay")
public class ProjectCompanyPayController {
  @Autowired
  private ProjectCompanyPayService projectCompanyPayService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectCompanyPayService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectCompanyPayEntity projectCompanyPay = projectCompanyPayService.getById(id);

    return R.ok().put("entity", projectCompanyPay);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectCompanyPayEntity projectCompanyPay) {
    projectCompanyPayService.save(projectCompanyPay);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectCompanyPayEntity projectCompanyPay) {
    projectCompanyPayService.updateById(projectCompanyPay);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectCompanyPayService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
