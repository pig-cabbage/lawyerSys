package cabbage.project.lawyerSys.dto;

import cabbage.project.lawyerSys.common.valid.ListValue;
import lombok.Data;

import java.util.Date;

@Data
public class ChatRecordDTO {
  /**
   *
   */
  private String man;
  /**
   *
   */
  private Long sessionId;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer sender;
  /**
   *
   */
  private Integer type;
  /**
   *
   */
  private String content;
  /**
   *
   */
  private Long createTime;
}
