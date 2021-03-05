package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 项目分配服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("project_plan")
public class ProjectPlanEntity implements Serializable {
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
  private Date createTime;
  /**
   *
   */
  private BigDecimal cost;

}
