package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectCompanyPayDao;
import cabbage.project.lawyerSys.entity.ProjectCompanyPayEntity;
import cabbage.project.lawyerSys.service.ProjectCompanyPayService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("projectCompanyPayService")
public class ProjectCompanyPayServiceImpl extends ServiceImpl<ProjectCompanyPayDao, ProjectCompanyPayEntity> implements ProjectCompanyPayService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectCompanyPayEntity> page = this.page(
        new Query<ProjectCompanyPayEntity>().getPage(params),
        new QueryWrapper<ProjectCompanyPayEntity>()
    );

    return new PageUtils(page);
  }

}