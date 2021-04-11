package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.service.ProjectChatService;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 用户帐号信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@RestController
@RequestMapping("api/user/account")
public class UserAccountController {
  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private ProjectChatService projectChatService;

  /**
   * 获取企业用户用户的当前联系人
   */
  @RequestMapping("/{account}/chat")
  public R getChatCompany(@PathVariable("account") String account, @RequestParam("role") Integer role) {
    List<ChatVo> list = projectChatService.getChat(account, role);

    return R.ok().put("list", list);
  }


  /**
   * 列表
   */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = userAccountService.queryPage(params);

    return R.ok().put("page", page);
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    UserAccountEntity userAccount = userAccountService.getById(id);

    return R.ok().put("userAccount", userAccount);
  }

  /**
   * 保存
   */
  @RequestMapping("/save")
  public R save(@RequestBody UserAccountEntity userAccount) {
    userAccountService.save(userAccount);

    return R.ok();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  public R update(@RequestBody UserAccountEntity userAccount) {
    userAccountService.updateById(userAccount);

    return R.ok();
  }

  /**
   * 删除
   */
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {
    userAccountService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }

}
