package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 认证申请处理记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("user_auth")
public class UserAuthEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long applyId;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer role;
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
