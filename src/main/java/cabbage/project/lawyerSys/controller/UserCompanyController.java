package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserCompanyEntity;
import cabbage.project.lawyerSys.service.UserCompanyService;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 企业用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/user/company")
public class UserCompanyController {
    @Autowired
    private UserCompanyService userCompanyService;

  /**
   * 根据account获取企业用户信息
   */
  @RequestMapping("/info/account/{id}")
  public R info(@PathVariable("id") String id) {
    UserCompanyEntity userCompany = userCompanyService.getByAccount(id);

    return R.ok().put("userCompany", userCompany);
  }


    /**
     * 企业检索
     * 127.0.0.1：8080/api/user/company/search
     */
    @RequestMapping("/search")
    public R search(@RequestParam Map<String, Object> params) {
        PageUtils page = userCompanyService.queryPage(params);

        return R.ok().put("page", page);
    }


}
