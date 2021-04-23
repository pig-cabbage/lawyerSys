package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ProjectChangeLawyerAuditDao;
import cabbage.project.lawyerSys.dao.ProjectChatDao;
import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import cabbage.project.lawyerSys.entity.ProjectChatRecordEntity;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandLawyerEntity;
import cabbage.project.lawyerSys.service.ProjectChatService;
import cabbage.project.lawyerSys.vo.ChatVo;
import cabbage.project.lawyerSys.vo.MessageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("projectChatService")
public class ProjectChatServiceimpl extends ServiceImpl<ProjectChatDao, ProjectChatEntity> implements ProjectChatService {
  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public List<ChatVo> getChat(String account, Integer role) {
    if (0 == role) {
      return this.list(new QueryWrapper<ProjectChatEntity>().eq("company", account)).stream()
          .map(item -> {
            ChatVo.ChatVoBuilder chatVoBuilder = ChatVo.builder().id(item.getId()).man(item.getLawyer()).project(item.getProject()).name(item.getLawyerName());
            if (redisTemplate.hasKey(String.valueOf(item.getId()))) {
              ProjectChatRecordEntity record = (ProjectChatRecordEntity) redisTemplate.boundListOps(String.valueOf(item.getId())).index(redisTemplate.boundListOps(String.valueOf(item.getId())).size() - 1);
              chatVoBuilder.newestMessage(MessageVo.builder().content(role.equals(record.getSender()) ? "你：" + record.getContent() : record.getContent()).createTime(record.getCreateTime().getTime()).sender(record.getSender()).build());
            }
            return chatVoBuilder.build();
          }).collect(Collectors.toList());
    } else {
      return this.list(new QueryWrapper<ProjectChatEntity>().eq("lawyer", account)).stream()
          .map(item -> {
            ChatVo.ChatVoBuilder chatVoBuilder = ChatVo.builder().id(item.getId()).man(item.getCompany()).project(item.getProject()).name(item.getLawyerName());
            if (redisTemplate.hasKey(String.valueOf(item.getId()))) {
              ProjectChatRecordEntity record = (ProjectChatRecordEntity) redisTemplate.boundListOps(String.valueOf(item.getId())).index(redisTemplate.boundListOps(String.valueOf(item.getId())).size() - 1);
              chatVoBuilder.newestMessage(MessageVo.builder().content(role.equals(record.getSender()) ? "你：" + record.getContent() : record.getContent()).createTime(record.getCreateTime().getTime()).sender(record.getSender()).build());
            }
            return chatVoBuilder.build();
          }).collect(Collectors.toList());
    }
  }


}
