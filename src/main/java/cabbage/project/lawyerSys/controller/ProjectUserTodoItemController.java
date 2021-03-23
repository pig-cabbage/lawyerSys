package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectUserTodoItemEntity;
import cabbage.project.lawyerSys.service.ProjectUserTodoItemService;
import cabbage.project.lawyerSys.vo.TodoItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 用户待办事项记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/todoItem")
public class ProjectUserTodoItemController {
  @Autowired
  private ProjectUserTodoItemService projectUserTodoItemService;

  /**
   * 获取特定用户的待办事项
   */
  @RequestMapping("/list/{userId}")
  public R list(@PathVariable("userId") String userId) {
    List<TodoItemVo> list = projectUserTodoItemService.getList(userId);

    return R.ok().put("list", list);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectUserTodoItemService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectUserTodoItemEntity projectUserTodoItem = projectUserTodoItemService.getById(id);

    return R.ok().put("projectUserTodoItem", projectUserTodoItem);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectUserTodoItemEntity projectUserTodoItem) {
    projectUserTodoItemService.save(projectUserTodoItem);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectUserTodoItemEntity projectUserTodoItem) {
    projectUserTodoItemService.updateById(projectUserTodoItem);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectUserTodoItemService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
