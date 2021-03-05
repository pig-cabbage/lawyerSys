package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.UserAuthEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 认证申请处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface UserAuthDao extends BaseMapper<UserAuthEntity> {

}
