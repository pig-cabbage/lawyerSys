package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserLawyerDao;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import cabbage.project.lawyerSys.service.UserLawyerAuthService;
import cabbage.project.lawyerSys.service.UserLawyerService;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userLawyerService")
public class UserLawyerServiceImpl extends ServiceImpl<UserLawyerDao, UserLawyerEntity> implements UserLawyerService {

  @Autowired
  private UserLawyerAuthService userLawyerAuthService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    QueryWrapper<UserLawyerEntity> wrapper = new QueryWrapper<>();
    Assert.isNotBlank((String) params.get("key"), key -> {

    });
    IPage<UserLawyerEntity> page = this.page(
        new Query<UserLawyerEntity>().getPage(params),
        wrapper
    );
    return new PageUtils(page);
  }


}