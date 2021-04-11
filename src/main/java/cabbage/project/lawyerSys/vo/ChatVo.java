package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatVo {
  private Long id;
  private String man;
  private String name;
  private Long project;
}
