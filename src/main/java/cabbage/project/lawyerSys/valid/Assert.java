/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cabbage.project.lawyerSys.valid;


import cabbage.project.lawyerSys.common.constant.ProjectConstant;
import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.entity.ProjectBaseEntity;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 数据校验
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class Assert {

//  //断言：账号为空
//  public static void accountIsBlank(String str) {
//    if (StringUtils.isBlank(str)) {
//      throw new AuthenticationServiceException("账号不能为空", RunException.builder().code(ExceptionCode.ACCOUNT_BLANK).build());
//    }
//  }

  //断言：项目状态错误
  public static void isTrueStatus(ProjectBaseEntity entity, ProjectConstant.ProjectStatusEnum... statuses) {
    boolean result = false;
    for (ProjectConstant.ProjectStatusEnum status : statuses) {
      if (status.getCode() == entity.getStatus()) {
        result = true;
        break;
      }
    }
    if (!result) {
      throw RunException.builder().code(ExceptionCode.WRONG_PROJECT_STATUS).build();
    }
  }

  public static void isEqual(Object val1, Object val2) {
    if (!val1.equals(val2)) {
      throw RunException.builder().code(ExceptionCode.WRONG_DATA).build();
    }
  }

  public static void isNotEqual(Object val1, Object val2) {
    if (val1.equals(val2)) {
      throw RunException.builder().code(ExceptionCode.WRONG_DATA).build();
    }
  }

  public static void isBlank(String str) {
    if (StringUtils.isBlank(str)) {
      throw RunException.builder().code(ExceptionCode.STRING_BLANK).build();
    }
  }


  public static void isNotNull(Object object) {
    if (object == null) {
      throw RunException.builder().code(ExceptionCode.DATA_NULL).build();
    }
  }

  public static void isNull(Object object) {
    if (object != null) {
      throw RunException.builder().code(ExceptionCode.DATA_MUST_NULL).build();
    }
  }

  public static void isEmpty(Collection<?> list) {
    if (list == null || list.size() == 0) {
      throw RunException.builder().code(ExceptionCode.DATA_NULL).build();
    }
  }

  public static <T> void isNotNull(T object, Consumer<? super T> action) {
    if (object != null) {
      action.accept(object);
    }
  }

  public static void isNotBlank(String s, Consumer<String> action) {
    if (!StringUtils.isBlank(s)) {
      action.accept(s);
    }
  }

  public static void isBlank(String s, Consumer<String> action) {
    if (StringUtils.isBlank(s)) {
      action.accept(s);
    }
  }


  public static <T> void isNotEmpty(Collection<T> collection, Consumer<Collection<T>> action) {
    if (!(collection == null || collection.size() <= 0)) {
      action.accept(collection);
    }
  }

  public static void isTrue(Boolean b, Consumer<Boolean> action) {
    if (b != null && b) {
      action.accept(b);
    }
  }


}
