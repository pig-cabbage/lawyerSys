package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandLawyerEntity;
import cabbage.project.lawyerSys.entity.ProjectLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyDemandLawyerService;
import cabbage.project.lawyerSys.service.ProjectLawyerService;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("projectLawyerService")
public class ProjectLawyerServiceImpl extends ServiceImpl<ProjectLawyerDao, ProjectLawyerEntity> implements ProjectLawyerService {

  @Autowired
  private ProjectCompanyDemandLawyerService projectCompanyDemandLawyerService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectLawyerEntity> page = this.page(
        new Query<ProjectLawyerEntity>().getPage(params),
        new QueryWrapper<ProjectLawyerEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public Long addRecord(DistributeLawyerVo distributeLawyerVo, Date date) {
    ProjectLawyerEntity projectLawyerEntity = ProjectLawyerEntity.builder()
        .lawyer(distributeLawyerVo.getLawyerId())
        .demandLawyer(distributeLawyerVo.getDemandLawyerRecordId())
        .createTime(date).build();
    this.save(projectLawyerEntity);
    return projectLawyerEntity.getId();
  }

  @Override
  public ProjectLawyerEntity getLatestRecord(Long id) {
    ProjectCompanyDemandLawyerEntity projectCompanyDemandLawyerEntity = projectCompanyDemandLawyerService.getByProjectIdLatest(id);
    return this.getOne(new QueryWrapper<ProjectLawyerEntity>().eq("demand_lawyer", projectCompanyDemandLawyerEntity.getId()));
  }

}