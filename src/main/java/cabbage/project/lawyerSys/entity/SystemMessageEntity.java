package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("system_message")
public class SystemMessageEntity {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String receiver;
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
  private String itemId;
  /**
   *
   */
  private Long createTime;

}
