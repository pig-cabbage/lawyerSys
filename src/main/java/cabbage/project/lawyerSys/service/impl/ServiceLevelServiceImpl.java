package cabbage.project.lawyerSys.service.impl;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.common.utils.Query;
import cabbage.project.lawyerSys.dao.ServiceLevelDao;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.ServiceLevelEntity;
import cabbage.project.lawyerSys.entity.ServicePlanEntity;
import cabbage.project.lawyerSys.service.ProjectBaseService;
import cabbage.project.lawyerSys.service.ServiceLevelService;
import cabbage.project.lawyerSys.service.ServicePlanService;
import cabbage.project.lawyerSys.vo.ServiceLevelMathVo;
import cabbage.project.lawyerSys.vo.ServiceMathVo;
import cabbage.project.lawyerSys.vo.ServicePlanMathVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service("serviceLevelService")
public class ServiceLevelServiceImpl extends ServiceImpl<ServiceLevelDao, ServiceLevelEntity> implements ServiceLevelService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<ServiceLevelEntity> page = this.page(
        new Query<ServiceLevelEntity>().getPage(params),
        new QueryWrapper<>()
    );

    return new PageUtils(page);
  }

}