package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.dao.ProjectChangeLawyerAuditDao;
import cabbage.project.lawyerSys.dao.ProjectChatDao;
import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.service.ProjectChatService;
import cabbage.project.lawyerSys.vo.ChatVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("projectChatService")
public class ProjectChatServiceimpl extends ServiceImpl<ProjectChatDao, ProjectChatEntity> implements ProjectChatService {
  @Override
  public List<ChatVo> getChat(String account, Integer role) {
    if (0 == role) {
      return this.list(new QueryWrapper<ProjectChatEntity>().eq("company", account)).stream()
          .map(item -> {
            return ChatVo.builder().id(item.getId()).man(item.getLawyer()).project(item.getProject()).name(item.getLawyerName()).build();
          }).collect(Collectors.toList());
    } else {
      return this.list(new QueryWrapper<ProjectChatEntity>().eq("lawyer", account)).stream()
          .map(item -> {
            return ChatVo.builder().id(item.getId()).man(item.getCompany()).project(item.getProject()).name(item.getCompanyName()).build();
          }).collect(Collectors.toList());
    }
  }
}
