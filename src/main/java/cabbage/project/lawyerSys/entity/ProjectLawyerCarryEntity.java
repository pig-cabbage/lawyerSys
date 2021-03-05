package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 律师承接项目记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@Builder
@TableName("project_lawyer_carry")
public class ProjectLawyerCarryEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long lawyer;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer carry;
  /**
   *
   */
  private String reason;
  /**
   *
   */
  private Date createTime;

}
