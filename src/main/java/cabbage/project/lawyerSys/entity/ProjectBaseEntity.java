package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@Builder
@TableName("project_base")
public class ProjectBaseEntity implements Serializable {
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
  private Long demand;
  /**
   *
   */
  private Date createTime;
  /**
   *
   */
  private Integer status;
  /**
   *
   */
  private String nowLawyer;
  /**
   *
   */
  private Long plan;
  /**
   *
   */
  private Date startTime;
  /**
   *
   */
  private Date endTime;
  /**
   *
   */
  private BigDecimal cost;
  /**
   *
   */
  private String projectName;

}
