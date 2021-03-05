package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.StatisticalLawyerDao;
import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import cabbage.project.lawyerSys.service.StatisticalLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("statisticalLawyerService")
public class StatisticalLawyerServiceImpl extends ServiceImpl<StatisticalLawyerDao, StatisticalLawyerEntity> implements StatisticalLawyerService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<StatisticalLawyerEntity> page = this.page(
        new Query<StatisticalLawyerEntity>().getPage(params),
        new QueryWrapper<StatisticalLawyerEntity>()
    );

    return new PageUtils(page);
  }

}