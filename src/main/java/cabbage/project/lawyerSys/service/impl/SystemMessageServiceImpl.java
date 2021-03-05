package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.SystemMessageDao;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.service.UserAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("systemMessageService")
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageDao, SystemMessageEntity> implements SystemMessageService {

  @Autowired
  private UserAccountService userAccountService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<SystemMessageEntity> page = this.page(
        new Query<SystemMessageEntity>().getPage(params),
        new QueryWrapper<SystemMessageEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public void addMessage(String accountId, SystemConstant.SystemMessageEnum systemMessage, String eventId, Date date) {
    SystemMessageEntity message = SystemMessageEntity.builder()
        .account(accountId)
        .keyword(systemMessage.getKeyWord())
        .content(systemMessage.getDetail())
        .url(systemMessage.getUrl() + eventId)
        .createTime(date).build();
    this.save(message);
    userAccountService.addMessage(accountId);
  }

  @Override
  public void addMessageWithoutEventId(String accountId, SystemConstant.SystemMessageEnum systemMessage, Date date) {
    SystemMessageEntity message = SystemMessageEntity.builder()
        .account(accountId)
        .content(systemMessage.getDetail())
        .createTime(date).build();
    this.save(message);
    userAccountService.addMessage(accountId);
  }


}