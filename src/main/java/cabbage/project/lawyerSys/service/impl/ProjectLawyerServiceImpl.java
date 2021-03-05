package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerService;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("projectLawyerService")
public class ProjectLawyerServiceImpl extends ServiceImpl<ProjectLawyerDao, ProjectLawyerEntity> implements ProjectLawyerService {

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

}