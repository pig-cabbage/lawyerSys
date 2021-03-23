package cabbage.project.lawyerSys.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class TodoItemVo {
  private Long item;
  private String projectName;
  private Long project;
  private String itemName;
  private String itemContent;
  private Date createTime;
  private Date latestTime;
}
