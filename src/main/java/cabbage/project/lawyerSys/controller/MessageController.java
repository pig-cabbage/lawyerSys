package cabbage.project.lawyerSys.controller;

import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.entity.ProjectMessageEntity;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.service.ProjectMessageService;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.service.UserAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {
  @Autowired
  private SystemMessageService systemMessageService;
  @Autowired
  private ProjectMessageService projectMessageService;
  @Autowired
  private UserAccountService userAccountService;

  @RequestMapping("/system/user/{id}")
  public R getSysById(@PathVariable("id") String id) {
    userAccountService.update(new UpdateWrapper<UserAccountEntity>().eq("id", id).set("un_read_message", 0));
    return R.ok().put("list", systemMessageService.list(new QueryWrapper<SystemMessageEntity>().eq("receiver", id).orderByDesc("create_time")));
  }

  @RequestMapping("/project/{projectId}/role/{role}")
  public R getProById(@PathVariable("projectId") Long projectId, @PathVariable("role") Integer role) {
    return R.ok().put("list", projectMessageService.list(new QueryWrapper<ProjectMessageEntity>().eq("project", projectId).eq("receiver", role).orderByDesc("create_time")));
  }

}
