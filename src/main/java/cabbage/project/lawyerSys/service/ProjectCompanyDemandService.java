package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectCompanyDemandEntity;
import cabbage.project.lawyerSys.vo.ProjectDemandVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 企业咨询记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectCompanyDemandService extends IService<ProjectCompanyDemandEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void add(ProjectDemandVo vo);
}

