package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectComplaintEntity;
import cabbage.project.lawyerSys.service.ProjectComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 项目申诉处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/complaint")
public class ProjectComplaintController {
  @Autowired
  private ProjectComplaintService projectComplaintService;

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = projectComplaintService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectComplaintEntity projectComplaint = projectComplaintService.getById(id);

    return R.ok().put("entity", projectComplaint);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectComplaintEntity projectComplaint) {
    projectComplaintService.save(projectComplaint);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectComplaintEntity projectComplaint) {
    projectComplaintService.updateById(projectComplaint);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectComplaintService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
