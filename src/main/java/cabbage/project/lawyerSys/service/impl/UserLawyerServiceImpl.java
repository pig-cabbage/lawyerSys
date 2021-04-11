package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.UserLawyerDao;
import cabbage.project.lawyerSys.entity.UserCompanyEntity;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import cabbage.project.lawyerSys.service.UserLawyerAuthService;
import cabbage.project.lawyerSys.service.UserLawyerService;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("userLawyerService")
public class UserLawyerServiceImpl extends ServiceImpl<UserLawyerDao, UserLawyerEntity> implements UserLawyerService {

  @Autowired
  private UserLawyerAuthService userLawyerAuthService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    QueryWrapper<UserLawyerEntity> wrapper = new QueryWrapper<>();
    Assert.isNotNull(params.get("level"), level -> wrapper.ge("highest_level", level).le("lowest_level", level));
    Assert.isNotBlank((String) params.get("key"), key -> {
      wrapper.like("name", key);
    });
    IPage<UserLawyerEntity> page = this.page(
        new Query<UserLawyerEntity>().getPage(params),
        wrapper
    );
    return new PageUtils(page);
  }

  @Override
  public void add(UserLawyerAuthEntity userLawyerAuthEntity, Date date, Integer lowestLevel, Integer highestLevel) {
    UserLawyerEntity userLawyerEntity = new UserLawyerEntity();
    BeanUtils.copyProperties(userLawyerAuthEntity, userLawyerEntity);
    userLawyerEntity.setCertificationTime(date);
    userLawyerEntity.setLowestLevel(lowestLevel);
    userLawyerEntity.setHighestLevel(highestLevel);
    userLawyerEntity.setId(null);
    UserLawyerEntity account = this.getOne(new QueryWrapper<UserLawyerEntity>().eq("account", userLawyerAuthEntity.getAccount()));
    if (account == null) {
      this.save(userLawyerEntity);
    } else {
      userLawyerEntity.setId(account.getId());
      BeanUtils.copyProperties(userLawyerEntity, account);
      this.updateById(account);
    }

  }

  @Override
  public UserLawyerEntity getByAccount(String id) {
    return this.getOne(new QueryWrapper<UserLawyerEntity>().eq("account", id));
  }


}