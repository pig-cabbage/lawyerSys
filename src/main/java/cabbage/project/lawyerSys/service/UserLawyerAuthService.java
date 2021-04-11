package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserLawyerAuthEntity;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 律师认证申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserLawyerAuthService extends IService<UserLawyerAuthEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void auth(LawyerAuthVo lawyerAuthVo);

  UserLawyerAuthEntity getLatest(String account);
}

