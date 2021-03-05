package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectLawyerCarryDao;
import cabbage.project.lawyerSys.entity.ProjectLawyerCarryEntity;
import cabbage.project.lawyerSys.service.ProjectLawyerCarryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectLawyerCarryService")
public class ProjectLawyerCarryServiceImpl extends ServiceImpl<ProjectLawyerCarryDao, ProjectLawyerCarryEntity> implements ProjectLawyerCarryService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectLawyerCarryEntity> page = this.page(
        new Query<ProjectLawyerCarryEntity>().getPage(params),
        new QueryWrapper<ProjectLawyerCarryEntity>()
    );

    return new PageUtils(page);
  }

}