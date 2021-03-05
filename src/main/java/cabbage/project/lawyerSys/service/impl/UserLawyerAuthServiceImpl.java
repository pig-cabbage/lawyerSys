package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserLawyerAuthDao;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.service.UserLawyerAuthService;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service("userLawyerAuthService")
public class UserLawyerAuthServiceImpl extends ServiceImpl<UserLawyerAuthDao, UserLawyerAuthEntity> implements UserLawyerAuthService {

  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private SystemMessageService systemMessageService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<UserLawyerAuthEntity> page = this.page(
        new Query<UserLawyerAuthEntity>().getPage(params),
        new QueryWrapper<UserLawyerAuthEntity>()
    );

    return new PageUtils(page);
  }

  /**
   * @param lawyerAuthVo 企业申请认证
   *                     1、生成一条认证记录
   *                     2、修改企业用户认证状态
   *                     3、生成一条系统消息
   */
  @Override
  @Transactional
  public void auth(LawyerAuthVo lawyerAuthVo) {
    UserAccountEntity userAccountEntity = userAccountService.getById(lawyerAuthVo.getAccount());
    Assert.isNotNull(userAccountEntity);
    if (Integer.valueOf(0).equals(userAccountEntity.getCertificationStatus())) {
      Date date = new Date();
      UserLawyerAuthEntity userLawyerAuthEntity = new UserLawyerAuthEntity();
      BeanUtils.copyProperties(lawyerAuthVo, userLawyerAuthEntity);
      userLawyerAuthEntity.setCreateTime(date);
      this.save(userLawyerAuthEntity);
      userAccountEntity.setCertificationStatus(1);
      userAccountService.updateById(userAccountEntity);
      systemMessageService.addMessage(userAccountEntity.getId(), SystemConstant.SystemMessageEnum.LAWYER_AUTH_APPLY, String.valueOf(userLawyerAuthEntity.getId()), date);
    } else {
      throw RunException.builder().code(ExceptionCode.USER_COMPANY_STATUS_ERROR).build();
    }
  }

}