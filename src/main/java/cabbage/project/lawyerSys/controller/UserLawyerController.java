package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import cabbage.project.lawyerSys.service.UserLawyerService;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 律师用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/user/lawyer")
public class UserLawyerController {
  @Autowired
  private UserLawyerService userLawyerService;


  /**
   * 企业检索
   * 127.0.0.1：8080/api/user/lawyer/search
   */
  @RequestMapping("/search")
  public R search(@RequestParam Map<String, Object> params) {
    PageUtils page = userLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = userLawyerService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    UserLawyerEntity userLawyer = userLawyerService.getById(id);

    return R.ok().put("userLawyer", userLawyer);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody UserLawyerEntity userLawyer) {
    userLawyerService.save(userLawyer);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody UserLawyerEntity userLawyer) {
    userLawyerService.updateById(userLawyer);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    userLawyerService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
