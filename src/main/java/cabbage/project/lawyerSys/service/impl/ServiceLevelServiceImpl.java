package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ServiceLevelDao;
import cabbage.project.lawyerSys.entity.ServiceLevelEntity;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("serviceLevelService")
public class ServiceLevelServiceImpl extends ServiceImpl<ServiceLevelDao, ServiceLevelEntity> implements ServiceLevelService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ServiceLevelEntity> page = this.page(
        new Query<ServiceLevelEntity>().getPage(params),
        new QueryWrapper<ServiceLevelEntity>()
    );

    return new PageUtils(page);
  }

}