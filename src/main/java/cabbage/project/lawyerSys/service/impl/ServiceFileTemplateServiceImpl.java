package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ServiceFileTemplateDao;
import cabbage.project.lawyerSys.entity.ServiceFileTemplateEntity;
import cabbage.project.lawyerSys.service.ServiceFileTemplateService;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("serviceFileTemplateService")
public class ServiceFileTemplateServiceImpl extends ServiceImpl<ServiceFileTemplateDao, ServiceFileTemplateEntity> implements ServiceFileTemplateService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ServiceFileTemplateEntity> page = this.page(
        new Query<ServiceFileTemplateEntity>().getPage(params),
        new QueryWrapper<ServiceFileTemplateEntity>()
    );

    return new PageUtils(page);
  }

  @Override
  public List<ServiceFileTemplateEntity> getList(Long id) {
    Assert.isNotNull(id);
    return this.list(new QueryWrapper<ServiceFileTemplateEntity>().eq("plan", id));
  }

  @Override
  public void setPlan(String userInfo, Long planId) {
    this.baseMapper.setPlan(userInfo, planId);
  }

}