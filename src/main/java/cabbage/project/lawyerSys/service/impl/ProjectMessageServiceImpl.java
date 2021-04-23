package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.dao.ProjectMessageDao;
import cabbage.project.lawyerSys.dao.ProjectPlanDao;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.ProjectMessageEntity;
import cabbage.project.lawyerSys.entity.ProjectPlanEntity;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.service.ProjectMessageService;
import cabbage.project.lawyerSys.service.ProjectPlanService;
import cabbage.project.lawyerSys.service.SystemMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("projectMessageService")
public class ProjectMessageServiceImpl extends ServiceImpl<ProjectMessageDao, ProjectMessageEntity> implements ProjectMessageService {
  @Autowired
  private SystemMessageService systemMessageService;
  @Autowired
  private ProjectBaseService projectBaseService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    return null;
  }


  @Override
  public boolean save(ProjectMessageEntity entity) {

    ProjectBaseEntity baseEntity = projectBaseService.getById(entity.getProject());
    return SqlHelper.retBool(this.getBaseMapper().insert(entity))
        && systemMessageService.save(SystemMessageEntity.builder()
        .itemId(String.valueOf(entity.getProject()))
        .content(entity.getReceiver() == 1 ? "您申请的项目：" + baseEntity.getProjectName() + " 有一条新消息" : "您负责的项目：" + baseEntity.getProjectName() + " 有一条新消息")
        .receiver(entity.getReceiver() == 1 ? baseEntity.getCompany() : baseEntity.getNowLawyer())
        .createTime(entity.getCreateTime()).build());
  }


}
