package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.ProjectPlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目分配服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface ProjectPlanDao extends BaseMapper<ProjectPlanEntity> {

  ProjectPlanEntity getProjectLatest(@Param("projectId") Long projectId);
}
