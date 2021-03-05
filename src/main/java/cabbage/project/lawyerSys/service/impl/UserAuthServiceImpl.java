package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.constant.UserConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserAuthDao;
import cabbage.project.lawyerSys.entity.UserAuthEntity;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.service.*;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.AuthProcessVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service("userAuthService")
public class UserAuthServiceImpl extends ServiceImpl<UserAuthDao, UserAuthEntity> implements UserAuthService {

  @Autowired
  private UserCompanyAuthService userCompanyAuthService;
  @Autowired
  private UserLawyerAuthService userLawyerAuthService;
  @Autowired
  private UserAccountService userAccountService;
  @Autowired
  private UserCompanyService userCompanyService;
  @Autowired
  private SystemMessageService systemMessageService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<UserAuthEntity> page = this.page(
        new Query<UserAuthEntity>().getPage(params),
        new QueryWrapper<UserAuthEntity>()
    );

    return new PageUtils(page);
  }

  /**
   * 1、数据库增加一条处理记录
   * 2、修改申请处理状态
   * 3、修改用户认证状态
   * 3、系统消息新增一条消息
   */
  @Transactional
  @Override
  public void processCompany(Long id, AuthProcessVo authProcessVo) {
    Date date = new Date();
    Assert.isNotNull(id);
    UserCompanyAuthEntity userCompanyAuthEntity = userCompanyAuthService.getById(id);
    Assert.isNotNull(authProcessVo.getResult());
    UserAuthEntity userAuthEntity = new UserAuthEntity();
    BeanUtils.copyProperties(authProcessVo, userAuthEntity);
    userAuthEntity.setRole(0);
    userAuthEntity.setCreateTime(date);
    userAuthEntity.setApplyId(id);
    this.save(userAuthEntity);
    if (Integer.valueOf(1).equals(authProcessVo.getResult())) {
      userAccountService.updateStatus(userCompanyAuthEntity.getAccount(), UserConstant.CertificationStatusEnum.SUCCESS);
    } else {
      if (Integer.valueOf(0).equals(authProcessVo.getResult())) {
        userAccountService.updateStatus(userCompanyAuthEntity.getAccount(), UserConstant.CertificationStatusEnum.FAIL);
      } else {
        throw RunException.builder().code(ExceptionCode.PROCESS_CER_STATUS_ERROR).build();
      }
    }
    userCompanyAuthEntity.setStatus(1);
    userCompanyAuthEntity.setProcessTime(date);
    userCompanyAuthService.updateById(userCompanyAuthEntity);
    systemMessageService.addMessage(userCompanyAuthEntity.getAccount(), SystemConstant.SystemMessageEnum.PROCESS_CER, String.valueOf(userAuthEntity.getId()), date);
  }

  @Transactional
  @Override
  public void processLawyer(Long id, AuthProcessVo authProcessVo) {
    Date date = new Date();
    Assert.isNotNull(id);
    UserLawyerAuthEntity userLawyerAuthEntity = userLawyerAuthService.getById(id);
    Assert.isNotNull(authProcessVo.getResult());
    UserAuthEntity userAuthEntity = new UserAuthEntity();
    BeanUtils.copyProperties(authProcessVo, userAuthEntity);
    userAuthEntity.setRole(1);
    userAuthEntity.setCreateTime(date);
    userAuthEntity.setApplyId(id);
    this.save(userAuthEntity);
    if (Integer.valueOf(1).equals(authProcessVo.getResult())) {
      userAccountService.updateStatus(userLawyerAuthEntity.getAccount(), UserConstant.CertificationStatusEnum.SUCCESS);
    } else {
      if (Integer.valueOf(0).equals(authProcessVo.getResult())) {
        userAccountService.updateStatus(userLawyerAuthEntity.getAccount(), UserConstant.CertificationStatusEnum.FAIL);
      } else {
        throw RunException.builder().code(ExceptionCode.PROCESS_CER_STATUS_ERROR).build();
      }
    }
    userLawyerAuthEntity.setStatus(1);
    userLawyerAuthEntity.setProcessTime(date);
    userLawyerAuthService.updateById(userLawyerAuthEntity);
    systemMessageService.addMessage(userLawyerAuthEntity.getAccount(), SystemConstant.SystemMessageEnum.PROCESS_CER, String.valueOf(userAuthEntity.getId()), date);
  }

}