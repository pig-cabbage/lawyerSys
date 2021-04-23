package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import cabbage.project.lawyerSys.service.StatisticalLawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 律师服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/lawyerStatistical")
public class StatisticalLawyerController {
  @Autowired
  private StatisticalLawyerService statisticalLawyerService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = statisticalLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    StatisticalLawyerEntity statisticalLawyer = statisticalLawyerService.getById(id);

    return R.ok().put("statisticalLawyer", statisticalLawyer);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody StatisticalLawyerEntity statisticalLawyer) {
    statisticalLawyerService.save(statisticalLawyer);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody StatisticalLawyerEntity statisticalLawyer) {
    statisticalLawyerService.updateById(statisticalLawyer);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    statisticalLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
