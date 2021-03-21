package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.ServiceFileTemplateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文件模板信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface ServiceFileTemplateDao extends BaseMapper<ServiceFileTemplateEntity> {

  void setPlan(@Param("userInfo") String userInfo, @Param("planId") Long planId);
}
