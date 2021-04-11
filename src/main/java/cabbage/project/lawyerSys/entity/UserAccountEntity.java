package cabbage.project.lawyerSys.entity;

import cabbage.project.lawyerSys.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 用户帐号信息表
 *
 * @author Weizhong
 * @email 2089319261@qq.com
 * @date 2021-02-09 02:07:42
 */
@Data
@TableName("user_account")
public class UserAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private String id;
	/**
	 *
	 */
	@ListValue(vals = {0, 1, 2})
	private Integer certificationStatus;
	/**
	 *
	 */
	private Integer role;
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private Integer unReadMessage;

}
