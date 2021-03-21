package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicePlanDetailVo {
  private Long id;
  private String name;
  private Long level;
  private BigDecimal chargeStandard;
  private String content;
}
