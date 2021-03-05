package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.UserConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserCompanyDao;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserCompanyEntity;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.service.UserCompanyAuthService;
import cabbage.project.lawyerSys.service.UserCompanyService;
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


@Service("userCompanyService")
public class UserCompanyServiceImpl extends ServiceImpl<UserCompanyDao, UserCompanyEntity> implements UserCompanyService {

  @Autowired
  private UserCompanyAuthService userCompanyAuthService;
  @Autowired
  private UserAccountService userAccountService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    QueryWrapper<UserCompanyEntity> wrapper = new QueryWrapper<>();
    Assert.isNotBlank((String) params.get("key"), key -> {

    });
    IPage<UserCompanyEntity> page = this.page(
        new Query<UserCompanyEntity>().getPage(params),
        wrapper
    );
    return new PageUtils(page);
  }


}