package cabbage.project.lawyerSys.dto;

import lombok.Data;

@Data
public class WeixinAuthDTO {
  private String openid;
  private String session_key;
  private String unionid;
  private Integer errcode;
  private String errmsg;
}
