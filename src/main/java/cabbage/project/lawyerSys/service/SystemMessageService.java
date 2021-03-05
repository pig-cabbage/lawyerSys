package cabbage.project.lawyerSys.service;

import cabbage.project.lawyerSys.common.constant.SystemConstant;
import cabbage.project.lawyerSys.common.utils.PageUtils;
import cabbage.project.lawyerSys.entity.SystemMessageEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * 系统消息记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-07 20:32:44
 */
public interface SystemMessageService extends IService<SystemMessageEntity> {

  PageUtils queryPage(Map<String, Object> params);

  void addMessage(String accountId, SystemConstant.SystemMessageEnum systemMessage, String EventId, Date date);

  void addMessageWithoutEventId(String accountId, SystemConstant.SystemMessageEnum systemMessage, Date date);
}

