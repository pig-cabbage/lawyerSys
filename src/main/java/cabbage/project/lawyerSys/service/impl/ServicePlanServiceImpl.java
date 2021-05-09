package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ServicePlanDao;
import cabbage.project.lawyerSys.entity.*;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.service.ServiceFileTemplateService;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.service.ServicePlanService;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.ServiceFileTemplateVo;
import cabbage.project.lawyerSys.vo.ServicePlanDetailVo;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import cabbage.project.lawyerSys.vo.ServicePlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("servicePlanService")
public class ServicePlanServiceImpl extends ServiceImpl<ServicePlanDao, ServicePlanEntity> implements ServicePlanService {

  @Autowired
  private ServiceFileTemplateService serviceFileTemplateService;
  @Autowired
  private ServiceLevelService serviceLevelService;
  @Autowired
  private ProjectBaseService projectBaseService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ServicePlanEntity> page = this.page(
        new Query<ServicePlanEntity>().getPage(params),
        new QueryWrapper<ServicePlanEntity>()
    );

    return new PageUtils(page);
  }

  @Transactional
  @Override
  public void savePlan(ServicePlanVo servicePlanVo) {
    Assert.isNotNull(servicePlanVo);
    System.out.println(servicePlanVo.getServiceLevel());
    ServicePlanEntity servicePlanEntity = new ServicePlanEntity();
    BeanUtils.copyProperties(servicePlanVo, servicePlanEntity);
    System.out.println(servicePlanEntity.getServiceLevel());
    servicePlanEntity.setCreateTime(new Date());
    servicePlanEntity.setModifyTime(new Date());
    this.save(servicePlanEntity);
    serviceFileTemplateService.setPlan(servicePlanVo.getUserInfo(), servicePlanEntity.getId());
  }

  @Override
  public void fileUpload(Long id, ServiceFileTemplateVo serviceFileTemplateVo) {
    ServiceFileTemplateEntity serviceFileTemplateEntity = new ServiceFileTemplateEntity();
    BeanUtils.copyProperties(serviceFileTemplateVo, serviceFileTemplateEntity);
    serviceFileTemplateEntity.setPlan(id);
    serviceFileTemplateService.save(serviceFileTemplateEntity);
  }

  @Override
  public BigDecimal calculateCost(Long id, Date startDate, Date endDate) {
    ServicePlanEntity servicePlanEntity = this.getById(id);
    ServiceLevelEntity serviceLevelEntity = serviceLevelService.getById(servicePlanEntity.getServiceLevel());
    Assert.isNotNull(serviceLevelEntity);
    Calendar start = Calendar.getInstance();
    start.setTime(startDate);
    Calendar end = Calendar.getInstance();
    end.setTime(endDate);
    int months = 12 * (end.get(Calendar.YEAR) - start.get(Calendar.YEAR))
        + (end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
    return serviceLevelEntity.getChargeStandard().multiply(new BigDecimal(months));
  }

  @Override
  public List<ServicePlanEntity> search(Long level, Map<String, Object> params) {
    QueryWrapper<ServicePlanEntity> wrapper = new QueryWrapper<>();
    wrapper.eq("service_level", level);
    Assert.isNotNull(params.get("status"), s -> {
      wrapper.eq("status", s);
    });
    Assert.isNotBlank((String) params.get("key"), k -> {
      wrapper.like("name", k);
    });
    if ("0".equals(params.get("sort"))) {
      wrapper.orderByDesc("create_time");
    } else {
      wrapper.orderByAsc("create_time");
    }
    return this.list(wrapper);
  }


  /**
   * @param id 1、删除实体
   *           2、删除实体对应的文件模板
   */
  @Override
  public void deletePlan(Long id) {
    this.removeById(id);
    serviceFileTemplateService.remove(new QueryWrapper<ServiceFileTemplateEntity>().eq("plan", id));
  }

  @Override
  public ServicePlanDetailVo detail(Long id) {
    ServicePlanEntity planEntity = this.getById(id);
    ServiceLevelEntity levelEntity = serviceLevelService.getById(planEntity.getServiceLevel());
    return ServicePlanDetailVo.builder()
        .id(id)
        .name(planEntity.getName())
        .level(levelEntity.getLevel())
        .chargeStandard(levelEntity.getChargeStandard())
        .content(planEntity.getContent()).build();
  }

  public void savePlanRecursive(List<ServiceFileTemplateVo> fileVos, Long planId, List<ServiceFileTemplateVo> allList, Long parentId) {
    fileVos.forEach(item -> {
      ServiceFileTemplateEntity serviceFileTemplateEntity = new ServiceFileTemplateEntity();
      BeanUtils.copyProperties(item, serviceFileTemplateEntity);
      serviceFileTemplateEntity.setPlan(planId);
      serviceFileTemplateEntity.setParent(parentId);
      serviceFileTemplateService.save(serviceFileTemplateEntity);
      if (Boolean.FALSE.equals(item.getType())) {
        List<ServiceFileTemplateVo> fileVos1 = allList.stream().filter(entity -> item.getUid().equals(entity.getParent())).collect(Collectors.toList());
        Assert.isNotEmpty(fileVos1, fileVos2 -> {
          savePlanRecursive((List<ServiceFileTemplateVo>) fileVos2, planId, allList, serviceFileTemplateEntity.getId());
        });
      }
    });
  }


}