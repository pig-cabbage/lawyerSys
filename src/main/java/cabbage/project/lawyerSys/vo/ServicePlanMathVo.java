package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicePlanMathVo {
  private Long planId;
  private String planName;
  private Long number;
  private Long date;
  private BigDecimal cost;
}
