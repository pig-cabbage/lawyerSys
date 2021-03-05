package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.UserLawyerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 律师用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface UserLawyerDao extends BaseMapper<UserLawyerEntity> {

}
