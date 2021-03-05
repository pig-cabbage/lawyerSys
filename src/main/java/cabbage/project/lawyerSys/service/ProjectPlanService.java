package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 项目分配服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectPlanService extends IService<ProjectPlanEntity> {

  PageUtils queryPage(Map<String, Object> params);

  ProjectPlanEntity getByProjectIdLatest(Long id);
}

