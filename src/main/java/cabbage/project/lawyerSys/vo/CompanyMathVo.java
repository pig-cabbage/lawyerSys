package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompanyMathVo {

  private String accountId;
  private String name;
  private Long number;
  private Long date;
  private BigDecimal cost;
}
