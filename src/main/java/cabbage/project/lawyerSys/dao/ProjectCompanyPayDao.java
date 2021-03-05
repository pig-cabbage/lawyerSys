package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.ProjectCompanyPayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业支付记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface ProjectCompanyPayDao extends BaseMapper<ProjectCompanyPayEntity> {

}
