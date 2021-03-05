package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.UserAccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户帐号信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {

  void addMessage(@Param("accountId") String accountId);

  void insertWithId(@Param("entity") UserAccountEntity entity);
}
