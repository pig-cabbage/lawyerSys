package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectFileEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 项目文件信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectFileService extends IService<ProjectFileEntity> {

  PageUtils queryPage(Map<String, Object> params);

  List<ProjectFileEntity> search(Map<String, Object> params);
}

