package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectAuditDao;
import cabbage.project.lawyerSys.entity.ProjectAuditEntity;
import cabbage.project.lawyerSys.service.ProjectAuditService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectAuditService")
public class ProjectAuditServiceImpl extends ServiceImpl<ProjectAuditDao, ProjectAuditEntity> implements ProjectAuditService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectAuditEntity> page = this.page(
        new Query<ProjectAuditEntity>().getPage(params),
        new QueryWrapper<ProjectAuditEntity>()
    );

    return new PageUtils(page);
  }

}