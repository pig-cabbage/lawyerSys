package cabbage.project.lawyerSys.vo;

import cabbage.project.lawyerSys.common.valid.ListValue;
import lombok.Data;

@Data
public class ProjectLawyerCarryVo {

  private Long lawyer;
  @ListValue(vals = {0, 1})
  private Integer carry;
  private String reason;
}
