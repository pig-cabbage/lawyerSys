package cabbage.project.lawyerSys.vo;

import lombok.Data;

/**
 * account	是	string	律师姓名
 * name	是	string	律师姓名
 * phoneNumber	是	string	联系方式
 * sex	是	integer	性别（0表示男性，1表示女性）
 * degree	是	integer	学位（0表示学士，1表示硕士，2表示博士）
 * workTime	是	integer	从业时长
 * lawyerLicense	是	string	律师执照（url地址）
 * positiveIdCard	是	string	身份证正面（url地址）
 * negativeIdCard	是	string	身份证反面（url地址）
 */
@Data
public class LawyerAuthVo {
  private String account;
  private String name;
  private String phoneNumber;
  private Integer sex;
  private Integer degree;
  private Integer workTime;
  private String lawyerLicense;
  private String positiveIdCard;
  private String negativeIdCard;
}
