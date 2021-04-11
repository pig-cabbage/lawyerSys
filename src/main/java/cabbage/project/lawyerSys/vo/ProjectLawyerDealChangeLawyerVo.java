package cabbage.project.lawyerSys.vo;

import cabbage.project.lawyerSys.common.valid.ListValue;
import lombok.Data;

@Data
public class ProjectLawyerDealChangeLawyerVo {
  private Long changeLawyer;
  @ListValue(vals = {0, 1})
  private Integer result;
  private String reason;
}
