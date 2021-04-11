package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.constant.UserConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserAccountDao;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service("userAccountService")
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccountEntity> implements UserAccountService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<UserAccountEntity> page = this.page(
        new Query<UserAccountEntity>().getPage(params),
        new QueryWrapper<UserAccountEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public void addMessage(String accountId) {
    Assert.isNotNull(accountId);
    this.baseMapper.addMessage(accountId);
  }


  @Override
  @Transactional
  public void updateStatus(String accountId, UserConstant.CertificationStatusEnum certificationStatusEnum) {
    Assert.isNotNull(accountId);
    UserAccountEntity accountEntity = this.getById(accountId);
    Assert.isNotEqual(accountEntity.getCertificationStatus(), certificationStatusEnum.getCode());
    accountEntity.setCertificationStatus(certificationStatusEnum.getCode());
    this.updateById(accountEntity);
  }

  @Override
  public UserAccountEntity addAccount(String openid, Integer role) {
    UserAccountEntity accountEntity = new UserAccountEntity();
    accountEntity.setId(openid);
    accountEntity.setRole(role);
    accountEntity.setCertificationStatus(0);
    accountEntity.setCreateTime(new Date());
    accountEntity.setUnReadMessage(0);
    this.save(accountEntity);
    return accountEntity;
  }

}