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
    return R.ok().put("list", list).put("count", userAccountService.getById(account).getUnReadMessage());
  }

}
