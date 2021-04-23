package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@TableName("project_message")
public class ProjectMessageEntity {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long project;
  /**
   *
   */
  private Integer receiver;
  /**
   *
   */
  private String content;
  /**
   *
   */
  private String appendContent;
  /**
   *
   */
  private Long createTime;
  /**
   *
   */
  private String itemId;
}
