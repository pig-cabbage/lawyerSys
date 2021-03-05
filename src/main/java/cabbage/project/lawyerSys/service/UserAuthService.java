package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserAuthEntity;
import cabbage.project.lawyerSys.vo.AuthProcessVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 认证申请处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserAuthService extends IService<UserAuthEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void processCompany(Long id, AuthProcessVo authProcessVo);

  void processLawyer(Long id, AuthProcessVo authProcessVo);
}

