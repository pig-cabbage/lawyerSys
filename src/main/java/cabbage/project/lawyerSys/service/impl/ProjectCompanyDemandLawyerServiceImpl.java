package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectCompanyDemandLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyDemandLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectCompanyDemandLawyerService")
public class ProjectCompanyDemandLawyerServiceImpl extends ServiceImpl<ProjectCompanyDemandLawyerDao, ProjectCompanyDemandLawyerEntity> implements ProjectCompanyDemandLawyerService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectCompanyDemandLawyerEntity> page = this.page(
        new Query<ProjectCompanyDemandLawyerEntity>().getPage(params),
        new QueryWrapper<ProjectCompanyDemandLawyerEntity>()
    );

    return new PageUtils(page);
  }

}