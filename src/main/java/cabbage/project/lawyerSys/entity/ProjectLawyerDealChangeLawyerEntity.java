package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 律师处理更换律师申请记录表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@Builder
@TableName("project_lawyer_deal_change_lawyer")
public class ProjectLawyerDealChangeLawyerEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @TableId
  private Long id;
  /**
   *
   */
  private Long changeLawyer;
  /**
   *
   */
  private Integer result;
  /**
   *
   */
  private Date createTime;
  /**
   *
   */
  private String reason;

}
