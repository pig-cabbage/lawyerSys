package cabbage.project.lawyerSys.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class EndServiceDTO {

  private Long project;
  private String lawyer;
  private String company;
  private Date date;
  private BigDecimal chargeStandard;
}
