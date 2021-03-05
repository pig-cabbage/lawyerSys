package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectLawyerDealChangeLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectLawyerDealChangeLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerDealChangeLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectLawyerComplaintService")
public class ProjectLawyerDealChangeLawyerServiceImpl extends ServiceImpl<ProjectLawyerDealChangeLawyerDao, ProjectLawyerDealChangeLawyerEntity> implements ProjectLawyerDealChangeLawyerService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectLawyerDealChangeLawyerEntity> page = this.page(
        new Query<ProjectLawyerDealChangeLawyerEntity>().getPage(params),
        new QueryWrapper<ProjectLawyerDealChangeLawyerEntity>()
    );

    return new PageUtils(page);
  }

}