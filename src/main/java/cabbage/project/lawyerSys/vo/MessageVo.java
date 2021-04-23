package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MessageVo {

  private Long sessionId;
  private Integer sender;
  private String content;
  private Long createTime;
}
