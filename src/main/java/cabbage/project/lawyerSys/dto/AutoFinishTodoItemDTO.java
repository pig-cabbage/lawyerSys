package cabbage.project.lawyerSys.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Long projectId = (Long)jobExecutionContext.getMergedJobDataMap().get("projectId");
 * Long userId = (Long)jobExecutionContext.getMergedJobDataMap().get("userId");
 * Long otherId = (Long)jobExecutionContext.getMergedJobDataMap().get("otherId");
 * Date date = (Date)jobExecutionContext.getMergedJobDataMap().get("date");
 */
@Data
@Builder
public class AutoFinishTodoItemDTO {
  private Long itemKey;
  private Long projectId;
  private String userId;
  private String otherId;
  private String userName;
  private String otherName;
  private Date date;
  private Integer defaultValue;
}
