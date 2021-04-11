package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectFileEntity;
import cabbage.project.lawyerSys.service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 项目文件信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/project/file")
public class ProjectFileController {
  @Autowired
  private ProjectFileService projectFileService;


  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    List<ProjectFileEntity> list = projectFileService.search(params);

    return R.ok().put("list", list);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    ProjectFileEntity projectFile = projectFileService.getById(id);

    return R.ok().put("projectFile", projectFile);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody ProjectFileEntity projectFile) {
    projectFileService.save(projectFile);
    return R.ok().put("entity", projectFile);
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody ProjectFileEntity projectFile) {
    projectFileService.updateById(projectFile);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    projectFileService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
