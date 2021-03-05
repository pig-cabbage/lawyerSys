package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.ServicePlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务方案信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface ServicePlanDao extends BaseMapper<ServicePlanEntity> {

}
