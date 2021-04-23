package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemMessageVo {
  private String content;
  private Long createTime;
}
