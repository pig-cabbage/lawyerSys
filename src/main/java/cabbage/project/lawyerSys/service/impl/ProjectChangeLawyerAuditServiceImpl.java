package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectChangeLawyerAuditDao;
import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.service.ProjectChangeLawyerAuditService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectChangeLawyerAuditService")
public class ProjectChangeLawyerAuditServiceImpl extends ServiceImpl<ProjectChangeLawyerAuditDao, ProjectChangeLawyerAuditEntity> implements ProjectChangeLawyerAuditService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectChangeLawyerAuditEntity> page = this.page(
        new Query<ProjectChangeLawyerAuditEntity>().getPage(params),
        new QueryWrapper<ProjectChangeLawyerAuditEntity>()
    );

    return new PageUtils(page);
  }

}