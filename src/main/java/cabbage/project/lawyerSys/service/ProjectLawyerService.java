package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectLawyerEntity;
import cabbage.project.lawyerSys.vo.DistributeLawyerVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 项目分配律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectLawyerService extends IService<ProjectLawyerEntity> {

  PageUtils queryPage(Map<String, Object> params);

  Long addRecord(DistributeLawyerVo distributeLawyerVo, Date date);

  ProjectLawyerEntity getLatestRecord(Long id);
}

