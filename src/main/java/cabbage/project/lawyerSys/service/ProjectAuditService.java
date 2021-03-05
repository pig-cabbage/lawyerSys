package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectAuditEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 项目审核记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectAuditService extends IService<ProjectAuditEntity> {

  PageUtils queryPage(Map<String, Object> params);
}

