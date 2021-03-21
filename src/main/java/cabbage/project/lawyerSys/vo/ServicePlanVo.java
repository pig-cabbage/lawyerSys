package cabbage.project.lawyerSys.vo;

import lombok.Data;

import java.util.List;

/**
 * serviceLevel	是	long	服务等级
 * name	是	string	服务方案名称
 * content	是	string	服务摘要
 * fileList	是	list<serviceFile>	文件模板
 */
@Data
public class ServicePlanVo {
  private Long serviceLevel;
  private String name;
  private String content;
  private String userInfo;
}
