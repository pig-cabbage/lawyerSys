package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 律师用户信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("user_lawyer")
public class UserLawyerEntity implements Serializable {
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
  private String phoneNumber;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer sex;
  /**
   *
   */
  @ListValue(vals = {0, 1, 2, 3})
  private Integer degree;
  /**
   *
   */
  private Integer workTime;
  /**
   *
   */
  private String account;
  /**
   *
   */
  private Integer lowestLevel;
  /**
   *
   */
  private Integer highestLevel;
  /**
   *
   */
  private String lawyerLicense;
  /**
   *
   */
  private Date certificationTime;

}
