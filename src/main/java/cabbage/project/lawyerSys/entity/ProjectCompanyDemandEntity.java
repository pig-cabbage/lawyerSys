package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 企业咨询记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@Builder
@TableName("project_company_demand")
public class ProjectCompanyDemandEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  @ListValue(vals = {0, 1})
  private Integer recommendPlan;
  /**
   *
   */
  private Long demandPlan;
  /**
   *
   */
  private String content;
  /**
   *
   */
  private String companyAccount;
  /**
   *
   */
  private Long objection;

}
