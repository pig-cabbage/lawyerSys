package cabbage.project.lawyerSys.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WorkRecordVo {
  private Long company;
  private Date startTime;
  private Date endTime;
  private Long plan;
  private BigDecimal cost;
}
