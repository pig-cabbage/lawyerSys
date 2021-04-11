package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 律师服务记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("statistical_lawyer")
public class StatisticalLawyerEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String lawyer;
  /**
   *
   */
  private String lawyerName;
  /**
   *
   */
  private String company;
  /**
   *
   */
  private String companyName;
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
  private Long project;
  /**
   *
   */
  private Double cost;
  /**
   *
   */
  private Long plan;

}
