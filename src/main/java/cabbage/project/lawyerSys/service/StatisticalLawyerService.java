package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 律师服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface StatisticalLawyerService extends IService<StatisticalLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void endService(ProjectBaseEntity projectBaseEntity, String nowLawyer, Date date);

  void endServiceFinal(ProjectBaseEntity projectBaseEntity, String nowLawyer);
}

