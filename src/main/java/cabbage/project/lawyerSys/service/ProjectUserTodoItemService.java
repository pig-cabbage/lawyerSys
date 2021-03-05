package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import cabbage.project.lawyerSys.entity.ProjectUserTodoItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户待办事项记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface ProjectUserTodoItemService extends IService<ProjectUserTodoItemEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void addItem(ProjectUserTodoItemEntity projectUserTodoItemEntity, String theOtherId, Long eventId, Date date);

  void finishItemWithUser(Long id, Long itemKey, Date date);

  void finishItemWithSystem(Long id, Long itemKey, Date date);
}

