package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectChatDao;
import cabbage.project.lawyerSys.dao.ProjectChatRecordDao;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import cabbage.project.lawyerSys.service.ProjectChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("projectChatRecordService")
public class ProjectChatRecordServiceimpl extends ServiceImpl<ProjectChatRecordDao, ProjectChatRecordEntity> implements ProjectChatRecordService {

  @Override
  public PageUtils queryPage(String sessionId, Map<String, Object> params) {
    IPage<ProjectChatRecordEntity> page = this.page(new Query<ProjectChatRecordEntity>().getPage(params),
        new QueryWrapper<ProjectChatRecordEntity>().eq("session_id", sessionId).orderByDesc("create_time"));
    return new PageUtils(page);
  }

}
