package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.constant.UserConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserCompanyAuthEntity;
import cabbage.project.lawyerSys.entity.UserCompanyEntity;
import cabbage.project.lawyerSys.vo.CompanyAuthVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 企业用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface UserCompanyService extends IService<UserCompanyEntity> {

    PageUtils queryPage(Map<String, Object> params);

  void add(UserCompanyAuthEntity userCompanyAuthEntity, Date date);

  UserCompanyEntity getByAccount(String id);
}

