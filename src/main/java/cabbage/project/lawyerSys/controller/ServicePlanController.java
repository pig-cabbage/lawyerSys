package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ServicePlanEntity;
import cabbage.project.lawyerSys.service.ServicePlanService;
import cabbage.project.lawyerSys.vo.ServiceFileTemplateVo;
import cabbage.project.lawyerSys.vo.ServicePlanDetailVo;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import cabbage.project.lawyerSys.vo.ServicePlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 服务方案信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/service/plan")
public class ServicePlanController {
  @Autowired
  private ServicePlanService servicePlanService;

  /**
   * 注册新服务方案
   * 127.0.0.1:8080/api/service/plan/add
   */
  @RequestMapping("/add")
  public R add(@RequestBody ServicePlanVo servicePlanVo) {
    servicePlanService.savePlan(servicePlanVo);
    return R.ok();
  }

  /**
   * 上传文件模板
   * 127.0.0.1:8080/api/service/plan/{id}/fileUpload
   */
  @RequestMapping("/{id}/fileUpload")
  public R fileUpload(@PathVariable("id") Long id, @RequestBody ServiceFileTemplateVo serviceFileTemplateVo) {
    servicePlanService.fileUpload(id, serviceFileTemplateVo);
    return R.ok();
  }

  /**
   * 列表
   */
  @RequestMapping("/{level}/search")
  public R search(@PathVariable("level") Long level, @RequestParam Map<String, Object> params) {
    List<ServicePlanEntity> entityList = servicePlanService.search(level, params);
    return R.ok().put("list", entityList);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = servicePlanService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息,连同服务等级一起返回
   */
  @RequestMapping("/detail/{id}")
  public R info(@PathVariable("id") Long id) {
    ServicePlanDetailVo servicePlanDetailVo = servicePlanService.detail(id);

    return R.ok().put("servicePlanDetail", servicePlanDetailVo);
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ServicePlanEntity servicePlan) {
    servicePlan.setModifyTime(new Date());
    servicePlanService.updateById(servicePlan);

    return R.ok();
  }

  /**
   * 删除
   */
  @DeleteMapping("/delete")
  public R delete(@RequestParam("id") Long id) {
    servicePlanService.deletePlan(id);
    return R.ok();
  }

}
