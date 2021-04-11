package cabbage.project.lawyerSys.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StartServiceDTO {
  private String lawyer;
  private String lawyerName;
  private String company;
  private String companyName;
  private Long project;
  private Long plan;
  private Date startTime;
}
