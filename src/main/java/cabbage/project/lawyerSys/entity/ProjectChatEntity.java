package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("project_chat")
public class ProjectChatEntity {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String company;
  /**
   *
   */
  private String lawyer;
  /**
   *
   */
  private Long project;
  /**
   *
   */
  private String companyName;
  /**
   *
   */
  private String lawyerName;
}
