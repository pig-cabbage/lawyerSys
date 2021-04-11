package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ServiceLevelEntity;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 服务等级信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/service/level")
public class ServiceLevelController {
  @Autowired
  private ServiceLevelService serviceLevelService;

  /**
   * 新建服务等级
   * 127.0.0.1:8080/api/service/level/add
   */
  @RequestMapping("/add")
  public R add(@RequestBody ServiceLevelEntity serviceLevel) {
    serviceLevel.setCreatetime(new Date());
    serviceLevelService.save(serviceLevel);
    return R.ok();
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = serviceLevelService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ServiceLevelEntity serviceLevel = serviceLevelService.getById(id);

    return R.ok().put("serviceLevel", serviceLevel);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ServiceLevelEntity serviceLevel) {
    serviceLevelService.save(serviceLevel);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ServiceLevelEntity serviceLevel) {
    serviceLevelService.updateById(serviceLevel);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    serviceLevelService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
