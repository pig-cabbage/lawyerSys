package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectUserChangeLawyerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户更换律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectUserChangeLawyerService extends IService<ProjectUserChangeLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  ProjectUserChangeLawyerEntity getInfo(Long projectId);
}

