package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectPlanDao;
import cabbage.project.lawyerSys.entity.ProjectLawyerCarryEntity;
import cabbage.project.lawyerSys.entity.ProjectPlanEntity;
import cabbage.project.lawyerSys.service.ProjectPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectPlanService")
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanDao, ProjectPlanEntity> implements ProjectPlanService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectPlanEntity> page = this.page(
        new Query<ProjectPlanEntity>().getPage(params),
        new QueryWrapper<ProjectPlanEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public ProjectPlanEntity getByProjectIdLatest(Long id) {
    return this.baseMapper.getProjectLatest(id);
  }

}