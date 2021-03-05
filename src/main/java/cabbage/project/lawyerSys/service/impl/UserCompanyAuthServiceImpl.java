package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserCompanyAuthDao;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserCompanyEntity;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.service.UserCompanyAuthService;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service("userCompanyAuthService")
public class UserCompanyAuthServiceImpl extends ServiceImpl<UserCompanyAuthDao, UserCompanyAuthEntity> implements UserCompanyAuthService {

  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private SystemMessageService systemMessageService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<UserCompanyAuthEntity> page = this.page(
        new Query<UserCompanyAuthEntity>().getPage(params),
        new QueryWrapper<UserCompanyAuthEntity>()
    );

    return new PageUtils(page);
  }

  /**
   * @param companyAuthVo 企业申请认证
   *                      1、生成一条认证记录
   *                      2、修改企业用户认证状态
   *                      3、生成一条系统消息
   */
  @Override
  @Transactional
  public void auth(CompanyAuthVo companyAuthVo) {
    UserAccountEntity userAccountEntity = userAccountService.getById(companyAuthVo.getAccount());
    Assert.isNotNull(userAccountEntity);
    if (Integer.valueOf(0).equals(userAccountEntity.getCertificationStatus())) {
      Date date = new Date();
      UserCompanyAuthEntity userCompanyAuthEntity = new UserCompanyAuthEntity();
      BeanUtils.copyProperties(companyAuthVo, userCompanyAuthEntity);
      userCompanyAuthEntity.setCreateTime(date);
      this.save(userCompanyAuthEntity);
      userAccountEntity.setCertificationStatus(1);
      userAccountService.updateById(userAccountEntity);
      systemMessageService.addMessage(userAccountEntity.getId(), SystemConstant.SystemMessageEnum.COMPANY_AUTH_APPLY, String.valueOf(userCompanyAuthEntity.getId()), date);
    } else {
      throw RunException.builder().code(ExceptionCode.USER_COMPANY_STATUS_ERROR).build();
    }
  }

}