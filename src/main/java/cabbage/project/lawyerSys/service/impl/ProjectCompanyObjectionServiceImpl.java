package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectCompanyObjectionDao;
import cabbage.project.lawyerSys.entity.ProjectCompanyObjectionEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyObjectionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectCompanyObjectionService")
public class ProjectCompanyObjectionServiceImpl extends ServiceImpl<ProjectCompanyObjectionDao, ProjectCompanyObjectionEntity> implements ProjectCompanyObjectionService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectCompanyObjectionEntity> page = this.page(
        new Query<ProjectCompanyObjectionEntity>().getPage(params),
        new QueryWrapper<ProjectCompanyObjectionEntity>()
    );

    return new PageUtils(page);
  }

}