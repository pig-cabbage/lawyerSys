package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 待办事项常量表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:41
 */
@Data
@TableName("constant_todo_item")
public class ConstantTodoItemEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String name;
  /**
   *
   */
  private String content;
  /**
   *
   */
  private Integer defaultValue;
  /**
   *
   */
  private Integer duration;

}
