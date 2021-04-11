package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 企业认证申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserCompanyAuthService extends IService<UserCompanyAuthEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void auth(CompanyAuthVo companyAuthVo);

  UserCompanyAuthEntity getLatest(String account);
}

