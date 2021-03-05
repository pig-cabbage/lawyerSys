package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.constant.UserConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户帐号信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserAccountService extends IService<UserAccountEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void addMessage(String accountId);

  UserAccountEntity addAccount(String openid, Integer role);

  void updateStatus(String accountId, UserConstant.CertificationStatusEnum certificationStatusEnum);
}

