package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("project_change_lawyer_audit")
public class ProjectChangeLawyerAuditEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long changeLawyer;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer result;
  /**
   *
   */
  private String advice;
  /**
   *
   */
  private Date createTime;

}
