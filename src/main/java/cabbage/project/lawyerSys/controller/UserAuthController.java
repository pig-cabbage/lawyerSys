package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserAuthEntity;
import cabbage.project.lawyerSys.service.UserAuthService;
import cabbage.project.lawyerSys.vo.AuthProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 认证申请处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("/api/process/user")
public class UserAuthController {
  @Autowired
  private UserAuthService userAuthService;

  /**
   * 管理员处理企业认证申请
   */
  @PostMapping("/company/{id}/auth")
  public R authCompany(@PathVariable("id") Long id, @RequestBody AuthProcessVo authProcessVo) {
    userAuthService.processCompany(id, authProcessVo);
    return R.ok();
  }

  /**
   * 管理员处理律师认证申请
   */
  @PostMapping("/lawyer/{id}/auth")
  public R authLawyer(@PathVariable("id") Long id, @RequestBody AuthProcessVo authProcessVo) {
    userAuthService.processLawyer(id, authProcessVo);
    return R.ok();
  }





  /**
   * 信息
   */
  @RequestMapping("/auth/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    UserAuthEntity userAuth = userAuthService.getById(id);
    return R.ok().put("entity", userAuth);
  }


}
