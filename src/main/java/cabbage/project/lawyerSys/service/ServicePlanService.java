package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ServicePlanEntity;
import cabbage.project.lawyerSys.vo.ServiceFileTemplateVo;
import cabbage.project.lawyerSys.vo.ServicePlanDetailVo;
import cabbage.project.lawyerSys.vo.ServicePlanVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务方案信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ServicePlanService extends IService<ServicePlanEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void savePlan(ServicePlanVo servicePlanVo);

  void fileUpload(Long id, ServiceFileTemplateVo serviceFileTemplateVo);

  BigDecimal calculateCost(Long id, Long months);

  List<ServicePlanEntity> search(Long level, Map<String, Object> params);

  void deletePlan(Long id);

  ServicePlanDetailVo detail(Long id);
}

