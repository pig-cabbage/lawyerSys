package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.service.UserCompanyAuthService;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业认证申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/user/company/auth")
public class UserCompanyAuthController {
  @Autowired
  private UserCompanyAuthService userCompanyAuthService;

  /**
   * 企业认证申请
   * 127.0.0.1：8080/api/user/company/auth/apply
   */
  @PostMapping("/apply")
  public R apply(@RequestBody CompanyAuthVo companyAuthVo) {
    userCompanyAuthService.auth(companyAuthVo);
    return R.ok();
  }

  /**
   * 获取特定账户最近一条申请记录
   */
  @RequestMapping("/{account}/latestRecord")
  public R getLatest(@PathVariable("account") String account) {
    UserCompanyAuthEntity entity = userCompanyAuthService.getLatest(account);

    return R.ok().put("entity", entity);
  }

  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = userCompanyAuthService.queryPage(params);

    return R.ok().put("page", page);
  }




}
