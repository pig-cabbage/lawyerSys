package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.ServiceLevelEntity;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 服务等级信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ServiceLevelService extends IService<ServiceLevelEntity> {

  PageUtils queryPage(Map<String, Object> params);
}

