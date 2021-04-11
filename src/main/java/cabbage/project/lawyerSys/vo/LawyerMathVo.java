package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LawyerMathVo {

  private String name;
  private String account;
  private Long totalNumber;
  private Long totalDate;
  private BigDecimal totalCost;
}
