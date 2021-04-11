package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ServiceMathVo {
  private Long totalNumber;
  private Long totalDate;
  private BigDecimal totalCost;
  private List<ServiceLevelMathVo> list;

}
