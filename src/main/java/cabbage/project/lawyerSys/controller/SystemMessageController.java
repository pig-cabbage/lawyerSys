package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 系统消息记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("lawyersys/systemmessage")
public class SystemMessageController {
  @Autowired
  private SystemMessageService systemMessageService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = systemMessageService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    SystemMessageEntity systemMessage = systemMessageService.getById(id);

    return R.ok().put("systemMessage", systemMessage);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody SystemMessageEntity systemMessage) {
    systemMessageService.save(systemMessage);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody SystemMessageEntity systemMessage) {
    systemMessageService.updateById(systemMessage);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    systemMessageService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
