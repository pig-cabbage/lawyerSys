package cabbage.project.lawyerSys.service;


import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ProjectChatRecordService extends IService<ProjectChatRecordEntity> {
  PageUtils queryPage(String sessionId, Map<String, Object> params);
}
