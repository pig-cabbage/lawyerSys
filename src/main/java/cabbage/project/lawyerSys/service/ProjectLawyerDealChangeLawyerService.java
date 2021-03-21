package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectLawyerDealChangeLawyerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 律师申诉记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectLawyerDealChangeLawyerService extends IService<ProjectLawyerDealChangeLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  ProjectLawyerDealChangeLawyerEntity getInfo(Long projectId);
}

