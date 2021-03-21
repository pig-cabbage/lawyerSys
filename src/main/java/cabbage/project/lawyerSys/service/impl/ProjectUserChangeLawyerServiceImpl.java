package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectUserChangeLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectUserChangeLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectUserChangeLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectUserChangeLawyerService")
public class ProjectUserChangeLawyerServiceImpl extends ServiceImpl<ProjectUserChangeLawyerDao, ProjectUserChangeLawyerEntity> implements ProjectUserChangeLawyerService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectUserChangeLawyerEntity> page = this.page(
        new Query<ProjectUserChangeLawyerEntity>().getPage(params),
        new QueryWrapper<ProjectUserChangeLawyerEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public ProjectUserChangeLawyerEntity getInfo(Long projectId) {
    return this.baseMapper.getByProjectLatest(projectId);
  }

}