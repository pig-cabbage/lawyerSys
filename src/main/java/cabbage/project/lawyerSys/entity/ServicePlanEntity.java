package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 服务方案信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("service_plan")
public class ServicePlanEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long serviceLevel;
  /**
   *
   */
  private String name;
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
  private String content;
  /**
   *
   */
  private Date modifyTime;

}
