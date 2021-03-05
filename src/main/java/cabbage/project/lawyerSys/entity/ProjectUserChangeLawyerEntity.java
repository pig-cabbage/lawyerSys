package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户更换律师记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("project_user_change_lawyer")
public class ProjectUserChangeLawyerEntity implements Serializable {
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
  private String lawyer;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer creator;
  /**
   *
   */
  private String reason;
  /**
   *
   */
  private Date createTime;
  /**
   *
   */
  private Integer status;

}
