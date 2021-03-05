package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectComplaintDao;
import cabbage.project.lawyerSys.entity.ProjectComplaintEntity;
import cabbage.project.lawyerSys.service.ProjectComplaintService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectComplaintService")
public class ProjectComplaintServiceImpl extends ServiceImpl<ProjectComplaintDao, ProjectComplaintEntity> implements ProjectComplaintService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectComplaintEntity> page = this.page(
        new Query<ProjectComplaintEntity>().getPage(params),
        new QueryWrapper<ProjectComplaintEntity>()
    );

    return new PageUtils(page);
  }

}