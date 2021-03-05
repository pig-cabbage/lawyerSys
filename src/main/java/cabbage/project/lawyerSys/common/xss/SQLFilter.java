/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cabbage.project.lawyerSys.common.xss;

import cabbage.project.lawyerSys.common.exception.RunException;
import org.apache.commons.lang.StringUtils;

/**
 * SQL过滤
 *
 * @author Mark sunlightcs@gmail.com
 */
public class SQLFilter {

  /**
   * SQL注入过滤
   *
   * @param str 待验证的字符串
   */
  public static String sqlInject(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    //去掉'|"|;|\字符
    str = StringUtils.replace(str, "'", "");
    str = StringUtils.replace(str, "\"", "");
    str = StringUtils.replace(str, ";", "");
    str = StringUtils.replace(str, "\\", "");

    //转换成小写
    str = str.toLowerCase();

    //非法字符
    String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

    //判断是否包含非法字符
    for (String keyword : keywords) {
      if (str.indexOf(keyword) != -1) {
        throw RunException.builder().message("包含非法字符").build();
      }
    }

    return str;
  }
}
