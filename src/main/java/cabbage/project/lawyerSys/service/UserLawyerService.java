package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 律师用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserLawyerService extends IService<UserLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void add(UserLawyerAuthEntity userLawyerAuthEntity, Date date, Integer lowestLevel, Integer highestLevel);

  UserLawyerEntity getByAccount(String id);
}

