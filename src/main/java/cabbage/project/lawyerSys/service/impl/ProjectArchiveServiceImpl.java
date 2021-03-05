package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectArchiveDao;
import cabbage.project.lawyerSys.entity.ProjectArchiveEntity;
import cabbage.project.lawyerSys.service.ProjectArchiveService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectArchiveService")
public class ProjectArchiveServiceImpl extends ServiceImpl<ProjectArchiveDao, ProjectArchiveEntity> implements ProjectArchiveService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectArchiveEntity> page = this.page(
        new Query<ProjectArchiveEntity>().getPage(params),
        new QueryWrapper<ProjectArchiveEntity>()
    );

    return new PageUtils(page);
  }

}