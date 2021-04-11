package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.vo.ChatVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectChatService extends IService<ProjectChatEntity> {
  List<ChatVo> getChat(String account, Integer role);

}
