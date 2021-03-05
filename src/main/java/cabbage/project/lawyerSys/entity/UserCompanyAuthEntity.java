package cabbage.project.lawyerSys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 企业认证申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("user_company_auth")
public class UserCompanyAuthEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private String account;
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
  private String organizationCode;
  /**
   *
   */
  private String address;
  /**
   *
   */
  private String businessLicense;
  /**
   *
   */
  private Integer status;
  /**
   *
   */
  private Date createTime;
  /**
   *
   */
  private Date processTime;
}
