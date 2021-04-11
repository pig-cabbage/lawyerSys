package cabbage.project.lawyerSys.dao;


import cabbage.project.lawyerSys.entity.ProjectChangeLawyerAuditEntity;
import cabbage.project.lawyerSys.entity.ProjectChatEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectChatDao extends BaseMapper<ProjectChatEntity> {

}

