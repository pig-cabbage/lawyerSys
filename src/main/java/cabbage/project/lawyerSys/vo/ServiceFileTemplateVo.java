package cabbage.project.lawyerSys.vo;

import lombok.Data;

/**
 * name	string	文件名
 * url	string	链接
 * type	boolean	文件类型（0表示文件夹，1表示文件）
 * suffix	string	后缀
 * parent	long	父文件
 */
@Data
public class ServiceFileTemplateVo {
  private Long uid;
  private String name;
  private String url;
  private Integer type;
  private String suffix;
  private Long parent;
}
