package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectComplaintEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 项目申诉处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectComplaintService extends IService<ProjectComplaintEntity> {

  PageUtils queryPage(Map<String, Object> params);
}

