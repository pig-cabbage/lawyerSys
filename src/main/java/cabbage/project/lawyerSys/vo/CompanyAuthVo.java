package cabbage.project.lawyerSys.vo;

import lombok.Data;

/**
 * |account |是  |string |  帐号id   |
 * |name |是  |string | 企业名称    |
 * |phoneNumber     |是  |string | 联系方式    |
 * |organizationCode |是  |string |机构代码   |
 * |address |是  |string |企业地址   |
 * |businessLicense |是  |string | 营业执照(url地址)    |
 */
@Data
public class CompanyAuthVo {
  private String account;
  private String name;
  private String phoneNumber;
  private String organizationCode;
  private String address;
  private String businessLicense;
}
