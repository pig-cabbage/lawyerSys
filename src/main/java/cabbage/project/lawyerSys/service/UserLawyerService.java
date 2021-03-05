package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import cabbage.project.lawyerSys.vo.LawyerAuthVo;
import com.baomidou.mybatisplus.extension.service.IService;

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
}

