package cabbage.project.lawyerSys.common.constant;

public class ProjectConstant {
  public enum ProjectStatusEnum {
    REGISTER_FAIL(-1, "登记失败"), REGISTER_SUCCESS(0, "登记成功，待审核"),
    AUDIT_SUCCESS(1, "审核成功，待分配服务"), AUDIT_FAIL(2, "审核不通过"),
    WAIT_TO_PAY(3, "待支付"), PAY_OVER_DUE(4, "支付过期"),
    WAIT_TO_CHOOSE_LAWYER(5, "待选择律师"), WAIT_TO_DIS_LAWYER(6, "待系统分配律师"),
    WAIT_TO_UNDER_TAKE(7, "待律师承接"), WAIT_TO_START_SERVICE(8, "待开始服务"),
    SERVICING(9, "正在服务"), COMPLAINT_STAGE(10, "待处理申诉"),
    END_WAIT_TO_EVALUATION(11, "服务结束, 待评价"), CHANGE_LAWYER_STAGE(12, "待审核更换律师申请"),
    RE_CHOOSE_LAWYER(13, "待企业重新选择律师"), WAIT_LAWYER_TO_DEAL_CHANGE_LAWYER(14, "待处理更换律师申请"),
    END_HAVE_EVALUATION(15, "服务结束， 已评价"), HAVE_DOCUMENT(16, "已归档"),
    PAY_PAST_DUE(17, "支付过期");

    private final int code;
    private final String msg;

    ProjectStatusEnum(int code, String msg) {
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
