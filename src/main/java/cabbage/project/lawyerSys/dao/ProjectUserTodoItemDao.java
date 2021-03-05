package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.ProjectUserTodoItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户待办事项记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface ProjectUserTodoItemDao extends BaseMapper<ProjectUserTodoItemEntity> {

  ProjectUserTodoItemEntity getItem(@Param("projectId") Long projectId, @Param("item") Long item);
}
