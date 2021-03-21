package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.StatisticalLawyerDao;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.service.ServicePlanService;
import cabbage.project.lawyerSys.service.StatisticalLawyerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@Service("statisticalLawyerService")
public class StatisticalLawyerServiceImpl extends ServiceImpl<StatisticalLawyerDao, StatisticalLawyerEntity> implements StatisticalLawyerService {

  @Autowired
  private ServicePlanService servicePlanService;
  @Autowired
  private ServiceLevelService serviceLevelService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<StatisticalLawyerEntity> page = this.page(
        new Query<StatisticalLawyerEntity>().getPage(params),
        new QueryWrapper<StatisticalLawyerEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public void endService(ProjectBaseEntity projectBaseEntity, String nowLawyer, Date date) {
    QueryWrapper<StatisticalLawyerEntity> wrapper = new QueryWrapper<StatisticalLawyerEntity>().eq("project", projectBaseEntity.getId());
    wrapper.and(new Consumer<QueryWrapper<StatisticalLawyerEntity>>() {
      @Override
      public void accept(QueryWrapper<StatisticalLawyerEntity> statisticalLawyerEntityQueryWrapper) {
        statisticalLawyerEntityQueryWrapper.eq("lawyer", nowLawyer);
      }
    });
    StatisticalLawyerEntity statisticalLawyerEntity = this.list(wrapper).get(0);
    Date newDate = new Date(date.getTime() + SystemConstant.CHANGE_LAWYER_GOV * 24 * 60 * 60 * 1000);
    statisticalLawyerEntity.setEndTime(new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()));
    statisticalLawyerEntity.setCost((statisticalLawyerEntity.getEndTime().getTime() - statisticalLawyerEntity.getStartTime().getTime()) / (30 * 24 * 60 * 60 * 1000) * serviceLevelService.getById(servicePlanService.getById(projectBaseEntity.getPlan()).getServiceLevel()).getChargeStandard().doubleValue());
    this.updateById(statisticalLawyerEntity);
  }

  @Override
  public void endServiceFinal(ProjectBaseEntity projectBaseEntity, String nowLawyer) {
    QueryWrapper<StatisticalLawyerEntity> wrapper = new QueryWrapper<StatisticalLawyerEntity>().eq("project", projectBaseEntity.getId());
    wrapper.and(new Consumer<QueryWrapper<StatisticalLawyerEntity>>() {
      @Override
      public void accept(QueryWrapper<StatisticalLawyerEntity> statisticalLawyerEntityQueryWrapper) {
        statisticalLawyerEntityQueryWrapper.eq("lawyer", nowLawyer);
      }
    });
    StatisticalLawyerEntity statisticalLawyerEntity = this.list(wrapper).get(0);
    statisticalLawyerEntity.setEndTime(projectBaseEntity.getEndTime());
    statisticalLawyerEntity.setCost((statisticalLawyerEntity.getEndTime().getTime() - statisticalLawyerEntity.getStartTime().getTime()) / (30 * 24 * 60 * 60 * 1000) * serviceLevelService.getById(servicePlanService.getById(projectBaseEntity.getPlan()).getServiceLevel()).getChargeStandard().doubleValue());
    this.updateById(statisticalLawyerEntity);
  }

}