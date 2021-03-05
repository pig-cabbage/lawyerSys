/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cabbage.project.lawyerSys.common.utils;

import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
  private static final long serialVersionUID = 1L;

  public <T> T getData(TypeReference<T> typeReference) throws IOException {
    Object data = get("data");
    String jsonString = JSON.toJSONString(data);
    return JSON.parseObject(jsonString, typeReference);
  }

  public R setData(Object data) {
    put("data", data);
    return this;
  }

  public R() {
    put("code", 0);
    put("msg", "success");
  }

  public static R error(ExceptionCode code) {
    R r = new R();
    r.put("code", code.getCode());
    r.put("msg", code.getMsg());
    return r;
  }

  public static R error(RunException exception) {
    R r = new R();
    r.put("code", exception.getCode().getCode());
    r.put("msg", exception.getCode().getMsg());
    if (exception.getData() != null && exception.getData().size() > 0)
      r.putAll(exception.getData());
    return r;
  }

  public static R ok(String msg) {
    R r = new R();
    r.put("msg", msg);
    return r;
  }

  public static R ok(Map<String, Object> map) {
    R r = new R();
    r.putAll(map);
    return r;
  }

  public static R ok() {
    return new R();
  }

  public R put(String key, Object value) {
    super.put(key, value);
    return this;
  }

  public Integer getCode() {
    return (Integer) this.get("code");
  }
}
