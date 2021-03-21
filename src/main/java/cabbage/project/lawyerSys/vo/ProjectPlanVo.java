package cabbage.project.lawyerSys.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectPlanVo {
  private Long plan;
  private Date startTime;
  private Date endTime;
}
