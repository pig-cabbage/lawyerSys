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
     * 企业检索
     * 127.0.0.1：8080/api/user/company/search
     */
    @RequestMapping("/search")
    public R search(@RequestParam Map<String, Object> params) {
        PageUtils page = userCompanyService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userCompanyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        UserCompanyEntity userCompany = userCompanyService.getById(id);

        return R.ok().put("userCompany", userCompany);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserCompanyEntity userCompany) {
        userCompanyService.save(userCompany);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserCompanyEntity userCompany) {
        userCompanyService.updateById(userCompany);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        userCompanyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
