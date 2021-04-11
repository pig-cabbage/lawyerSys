package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("project_chat_record")
public class ProjectChatRecordEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long sessionId;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer sender;
  /**
   *
   */
  private Integer type;
  /**
   *
   */
  private String content;
  /**
   *
   */
  private Date createTime;
}
