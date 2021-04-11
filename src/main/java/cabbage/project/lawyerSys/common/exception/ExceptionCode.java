package cabbage.project.lawyerSys.common.exception;

public enum ExceptionCode {
  UNKNOW_EXCEPTION(10000, "系统未知异常"),
  DATA_NULL(10001, "数据不能为空"),
  DATA_MUST_NULL(10002, "数据必须为空"),
  STRING_BLANK(10003, "字符串不能为空"),
  GET_POLICY_EXCEPTION(10004, "获取认证失败"),
  DATA_NOT_EXIST(10005, "数据不存在"),
  WRONG_DATA_CODE(10006, "错误数码"),
  WRONG_NUMBER(10007, "数量错误"),
  VALID_EXCEPTION(10008, "数据格式错误"),
  WRONG_DATA(10009, "数据错误"),
  CREATE_SCHEDULE_ERROR(10010, "创建定时任务错误"),
  DELETE_SCHEDULE_ERROR(10011, "删除定时任务失败"),
  AUTH_FAIL(10012, "认证失败"),
  ACCESS_DENY(10013, "没有权限"),
  GET_WEIXIN_AUTH_FAIL(10014, "获取微信登录状态失败"),
  LOGIN_ROLE_WRONG(10015, "登录身份不匹配"),
  DATE_TRANS_WRONG(10016, "日期转化错误"),
  USER_COMPANY_STATUS_ERROR(20001, "企业用户状态错误"),
  USER_LAWYER_STATUS_ERROR(20002, "律师用户状态错误"),
  PROCESS_CER_STATUS_ERROR(20003, "处理用户认证申请状态码错误"),
  WRONG_PROJECT_STATUS(30001, "项目状态错误"),
  USER_OFFLINE(40001, "用户不存在或不在线");


  private int code;
  private String msg;

  ExceptionCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
