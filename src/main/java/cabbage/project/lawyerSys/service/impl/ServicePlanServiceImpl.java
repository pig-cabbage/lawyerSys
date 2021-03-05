package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ServicePlanDao;
import cabbage.project.lawyerSys.entity.ServiceFileTemplateEntity;
import cabbage.project.lawyerSys.entity.ServiceLevelEntity;
import cabbage.project.lawyerSys.entity.ServicePlanEntity;
import cabbage.project.lawyerSys.service.ServiceFileTemplateService;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.service.ServicePlanService;
import cabbage.project.lawyerSys.valid.Assert;
import cabbage.project.lawyerSys.vo.ServiceFileTemplateVo;
import cabbage.project.lawyerSys.vo.ServicePlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    ServicePlanEntity servicePlanEntity = new ServicePlanEntity();
    BeanUtils.copyProperties(servicePlanVo, servicePlanEntity);
    servicePlanEntity.setCreateTime(new Date());
    servicePlanEntity.setModifyTime(new Date());
    this.save(servicePlanEntity);
    Assert.isNotEmpty(servicePlanVo.getFileList(), list -> {
      list.stream().filter(entity -> entity.getParent().equals(-1L)).forEach(item -> {
        ServiceFileTemplateEntity serviceFileTemplateEntity = new ServiceFileTemplateEntity();
        BeanUtils.copyProperties(item, serviceFileTemplateEntity);
        serviceFileTemplateEntity.setPlan(servicePlanEntity.getId());
        serviceFileTemplateService.save(serviceFileTemplateEntity);
        if (Boolean.FALSE.equals(item.getType())) {
          List<ServiceFileTemplateVo> fileVos = list.stream().filter(entity1 -> entity1.getParent().equals(item.getUid())).collect(Collectors.toList());
          Assert.isNotEmpty(fileVos, fileVos1 -> {
            savePlanRecursive((List<ServiceFileTemplateVo>) fileVos1, servicePlanEntity.getId(), servicePlanVo.getFileList(), serviceFileTemplateEntity.getId());
          });
        }
      });
    });
  }

  @Override
  public void fileUpload(Long id, ServiceFileTemplateVo serviceFileTemplateVo) {
    ServiceFileTemplateEntity serviceFileTemplateEntity = new ServiceFileTemplateEntity();
    BeanUtils.copyProperties(serviceFileTemplateVo, serviceFileTemplateEntity);
    serviceFileTemplateEntity.setPlan(id);
    serviceFileTemplateService.save(serviceFileTemplateEntity);
  }

  @Override
  public BigDecimal calculateCost(Long id, Long months) {
    ServicePlanEntity servicePlanEntity = this.getById(id);
    ServiceLevelEntity serviceLevelEntity = serviceLevelService.getById(servicePlanEntity.getServiceLevel());
    Assert.isNotNull(serviceLevelEntity);
    return serviceLevelEntity.getChargeStandard().multiply(new BigDecimal(months));
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