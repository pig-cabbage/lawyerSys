package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectFileDao;
import cabbage.project.lawyerSys.entity.ProjectFileEntity;
import cabbage.project.lawyerSys.service.ProjectFileService;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("projectFileService")
public class ProjectFileServiceImpl extends ServiceImpl<ProjectFileDao, ProjectFileEntity> implements ProjectFileService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectFileEntity> page = this.page(
        new Query<ProjectFileEntity>().getPage(params),
        new QueryWrapper<ProjectFileEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public List<ProjectFileEntity> search(Map<String, Object> params) {
    QueryWrapper<ProjectFileEntity> wrapper = new QueryWrapper<>();
    Assert.isNotNull(params.get("projectId"), id -> {
      wrapper.eq("project", id);
    });
    Assert.isNotNull(params.get("key"), key -> {
      wrapper.like("fileName", key);
    });
    return this.list(wrapper);
  }

}