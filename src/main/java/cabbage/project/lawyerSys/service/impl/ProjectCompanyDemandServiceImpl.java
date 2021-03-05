package cabbage.project.lawyerSys.service.impl;


import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectCompanyDemandDao;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandEntity;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.service.ProjectCompanyDemandService;
import cabbage.project.lawyerSys.service.SystemMessageService;
import cabbage.project.lawyerSys.valid.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service("projectCompanyDemandService")
public class ProjectCompanyDemandServiceImpl extends ServiceImpl<ProjectCompanyDemandDao, ProjectCompanyDemandEntity> implements ProjectCompanyDemandService {

  @Autowired
  private ProjectBaseService projectBaseService;
  @Autowired
  private SystemMessageService systemMessageService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ProjectCompanyDemandEntity> page = this.page(
        new Query<ProjectCompanyDemandEntity>().getPage(params),
        new QueryWrapper<ProjectCompanyDemandEntity>()
    );

    return new PageUtils(page);
  }

  /**
   * 新增一条咨询记录
   * 咨询服务项目基本信息新增一条记录
   * 系统消息生成一条消息
   */
  @Transactional
  @Override
  public void add(ProjectCompanyDemandEntity entity) {
    Date date = new Date();
    if (Integer.valueOf(0).equals(entity.getRecommendPlan())) {
      Assert.isNotNull(entity.getDemandPlan());
    } else {
      if (Integer.valueOf(1).equals(entity.getRecommendPlan())) {
        Assert.isNull(entity.getDemandPlan());
      } else {
        throw RunException.builder().code(ExceptionCode.WRONG_DATA_CODE).build();
      }
    }
    this.save(entity);
    ProjectBaseEntity project = ProjectBaseEntity.builder()
        .company(entity.getCompanyAccount())
        .demand(entity.getId())
        .createTime(date)
        .status(ProjectConstant.ProjectStatusEnum.REGISTER_SUCCESS.getCode())
        .projectName(String.valueOf(date.toString()))
        .build();
    projectBaseService.save(project);
    systemMessageService.addMessage(entity.getCompanyAccount(), SystemConstant.SystemMessageEnum.REGISTER_SUCCESS, String.valueOf(project.getId()), date);
  }

}