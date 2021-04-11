package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.dao.ProjectChatDao;
import cabbage.project.lawyerSys.dao.ProjectChatRecordDao;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.service.ProjectChatRecordService;
import cabbage.project.lawyerSys.service.ProjectChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("projectChatRecordService")
public class ProjectChatRecordServiceimpl extends ServiceImpl<ProjectChatRecordDao, ProjectChatRecordEntity> implements ProjectChatRecordService {
}
