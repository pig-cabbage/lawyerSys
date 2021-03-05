package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ConstantTodoItemEntity;
import cabbage.project.lawyerSys.service.ConstantTodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 待办事项常量表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("lawyersys/constanttodoitem")
public class ConstantTodoItemController {
  @Autowired
  private ConstantTodoItemService constantTodoItemService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = constantTodoItemService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ConstantTodoItemEntity constantTodoItem = constantTodoItemService.getById(id);

    return R.ok().put("constantTodoItem", constantTodoItem);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ConstantTodoItemEntity constantTodoItem) {
    constantTodoItemService.save(constantTodoItem);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ConstantTodoItemEntity constantTodoItem) {
    constantTodoItemService.updateById(constantTodoItem);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    constantTodoItemService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
