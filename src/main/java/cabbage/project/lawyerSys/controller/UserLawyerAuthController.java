package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.service.UserLawyerAuthService;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 律师认证申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/user/lawyer/auth")
public class UserLawyerAuthController {
    @Autowired
    private UserLawyerAuthService userLawyerAuthService;

    /**
     * 律师认证申请
     * 127.0.0.1：8080/api/user/company/auth/apply
     */
    @PostMapping("/apply")
    public R apply(@RequestBody LawyerAuthVo lawyerAuthVo) {
        userLawyerAuthService.auth(lawyerAuthVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userLawyerAuthService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        UserLawyerAuthEntity userLawyerAuth = userLawyerAuthService.getById(id);

        return R.ok().put("userLawyerAuth", userLawyerAuth);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserLawyerAuthEntity userLawyerAuth) {
        userLawyerAuthService.save(userLawyerAuth);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserLawyerAuthEntity userLawyerAuth) {
        userLawyerAuthService.updateById(userLawyerAuth);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        userLawyerAuthService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
