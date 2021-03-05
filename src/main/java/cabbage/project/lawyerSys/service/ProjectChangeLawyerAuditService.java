package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectChangeLawyerAuditService extends IService<ProjectChangeLawyerAuditEntity> {

  PageUtils queryPage(Map<String, Object> params);
}

