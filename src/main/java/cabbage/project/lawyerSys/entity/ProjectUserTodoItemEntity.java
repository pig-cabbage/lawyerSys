package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 用户待办事项记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@Builder
@TableName("project_user_todo_item")
public class ProjectUserTodoItemEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String user;
  /**
   *
   */
  private Long project;
  /**
   *
   */
  private String projectName;
  /**
   *
   */
  private Long item;
  /**
   *
   */
  private Date createTime;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer status;
  /**
   *
   */
  private Date finishTime;
  /**
   *
   */
  private Date latestTime;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer type;

}
