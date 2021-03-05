package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectCompanyEvaluationDao;
import cabbage.project.lawyerSys.entity.ProjectCompanyEvaluationEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyEvaluationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectCompanyEvaluationService")
public class ProjectCompanyEvaluationServiceImpl extends ServiceImpl<ProjectCompanyEvaluationDao, ProjectCompanyEvaluationEntity> implements ProjectCompanyEvaluationService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectCompanyEvaluationEntity> page = this.page(
        new Query<ProjectCompanyEvaluationEntity>().getPage(params),
        new QueryWrapper<ProjectCompanyEvaluationEntity>()
    );

    return new PageUtils(page);
  }

}