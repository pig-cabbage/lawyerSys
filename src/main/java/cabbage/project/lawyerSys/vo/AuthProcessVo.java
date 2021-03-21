package cabbage.project.lawyerSys.vo;

import lombok.Data;

/**
 *
 * result	是	integer	处理结果（0表示不通过，1表示通过）
 * advice	是	string	处理意见
 * lowestLevel 否 integer 最低可服务等级
 * highestLevel 否 integer 最高可服务等级
 *
 *
 */
@Data
public class AuthProcessVo {
  private Integer result;
  private String advice;
  private Integer lowestLevel;
  private Integer highestLevel;
}
