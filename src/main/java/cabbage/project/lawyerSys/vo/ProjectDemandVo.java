package cabbage.project.lawyerSys.vo;

import cabbage.project.lawyerSys.common.valid.ListValue;
import lombok.Data;

@Data
public class ProjectDemandVo {
  @ListValue(vals = {0, 1})
  private Integer recommendPlan;
  private Long demandPlan;
  private String content;
  private String companyAccount;
  private String projectName;
}
