package cabbage.project.lawyerSys.dao;

import cabbage.project.lawyerSys.entity.StatisticalLawyerEntity;
import cabbage.project.lawyerSys.vo.LawyerMathDetailVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 律师服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
@Mapper
public interface StatisticalLawyerDao extends BaseMapper<StatisticalLawyerEntity> {

  StatisticalLawyerEntity getLatestRecord(@Param("projectId") Long project);

  List<LawyerMathDetailVo> getDate(@Param("account") String account, Date startDate, Date endState);
}
