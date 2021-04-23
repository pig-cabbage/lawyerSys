package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.dao.ProjectMessageDao;
import cabbage.project.lawyerSys.dao.SystemMessageDao;
import cabbage.project.lawyerSys.entity.ProjectMessageEntity;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.ProjectMessageService;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("systemMessageService")
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageDao, SystemMessageEntity> implements SystemMessageService {

  @Autowired
  private UserAccountService userAccountService;

  @Override
  public boolean save(SystemMessageEntity entity) {
    if (SqlHelper.retBool(this.getBaseMapper().insert(entity))) {
      userAccountService.addMessage(entity.getReceiver());
      return true;
    }
    return false;
  }
}
