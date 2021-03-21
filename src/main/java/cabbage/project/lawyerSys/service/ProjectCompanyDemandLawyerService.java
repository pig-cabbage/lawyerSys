package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandLawyerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 企业选择律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectCompanyDemandLawyerService extends IService<ProjectCompanyDemandLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  ProjectCompanyDemandLawyerEntity getByProjectIdLatest(Long projectId);
}

