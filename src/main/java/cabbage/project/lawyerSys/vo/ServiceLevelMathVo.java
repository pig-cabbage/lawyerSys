package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ServiceLevelMathVo {
  private Long levelId;
  private String levelName;
  private Long number;
  private Long date;
  private BigDecimal cost;
  private List<ServicePlanMathVo> list;
}
