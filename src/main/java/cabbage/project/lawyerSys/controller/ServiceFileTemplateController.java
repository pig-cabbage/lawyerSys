package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ServiceFileTemplateEntity;
import cabbage.project.lawyerSys.service.ServiceFileTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 文件模板信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/service/file/template")
public class ServiceFileTemplateController {
  @Autowired
  private ServiceFileTemplateService serviceFileTemplateService;

  /**
   * 获取特定服务方案的文件模板列表
   */
  @RequestMapping("/{id}/list")
  public R getList(@PathVariable("id") Long id) {
    List<ServiceFileTemplateEntity> result = serviceFileTemplateService.getList(id);

    return R.ok().put("list", result);
  }


  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ServiceFileTemplateEntity serviceFileTemplate) {
    serviceFileTemplateService.save(serviceFileTemplate);
    return R.ok().put("entity", serviceFileTemplate);
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
