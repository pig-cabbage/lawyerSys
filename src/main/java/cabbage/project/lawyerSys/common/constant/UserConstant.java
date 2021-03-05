package cabbage.project.lawyerSys.common.constant;

public class UserConstant {
  //用户认证状态
  public enum CertificationStatusEnum {
    NOT(0, "未认证"), WAIT(1, "待认证"),
    SUCCESS(2, "认证成功"), FAIL(3, "认证失败"),
    ERROR(4, "有异常");

    private final int code;
    private final String msg;

    CertificationStatusEnum(int code, String msg) {
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

  //管理员处理认证结果
  public enum CertificationResultEnum {
    FAIL(0, "认证不通过"), SUCCESS(1, "认证通过"),
    ERROR(2, "有异常");

    private int code;
    private String msg;

    CertificationResultEnum(int code, String msg) {
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
}
