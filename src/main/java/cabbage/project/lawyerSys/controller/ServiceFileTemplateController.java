package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ServiceFileTemplateEntity;
import cabbage.project.lawyerSys.service.ServiceFileTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 文件模板信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("lawyersys/servicefiletemplate")
public class ServiceFileTemplateController {
  @Autowired
  private ServiceFileTemplateService serviceFileTemplateService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = serviceFileTemplateService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ServiceFileTemplateEntity serviceFileTemplate = serviceFileTemplateService.getById(id);

    return R.ok().put("serviceFileTemplate", serviceFileTemplate);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ServiceFileTemplateEntity serviceFileTemplate) {
    serviceFileTemplateService.save(serviceFileTemplate);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ServiceFileTemplateEntity serviceFileTemplate) {
    serviceFileTemplateService.updateById(serviceFileTemplate);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    serviceFileTemplateService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
