package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectLawyerDealChangeLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectLawyerDealChangeLawyerEntity;
import cabbage.project.lawyerSys.entity.ProjectUserChangeLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerDealChangeLawyerService;
import cabbage.project.lawyerSys.service.ProjectUserChangeLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectLawyerComplaintService")
public class ProjectLawyerDealChangeLawyerServiceImpl extends ServiceImpl<ProjectLawyerDealChangeLawyerDao, ProjectLawyerDealChangeLawyerEntity> implements ProjectLawyerDealChangeLawyerService {

  @Autowired
  private ProjectUserChangeLawyerService projectUserChangeLawyerService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectLawyerDealChangeLawyerEntity> page = this.page(
        new Query<ProjectLawyerDealChangeLawyerEntity>().getPage(params),
        new QueryWrapper<ProjectLawyerDealChangeLawyerEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public ProjectLawyerDealChangeLawyerEntity getInfo(Long projectId) {
    ProjectUserChangeLawyerEntity projectUserChangeLawyerEntity = projectUserChangeLawyerService.getInfo(projectId);
    return this.getOne(new QueryWrapper<ProjectLawyerDealChangeLawyerEntity>().eq("change_lawyer", projectUserChangeLawyerEntity.getId()));
  }

}