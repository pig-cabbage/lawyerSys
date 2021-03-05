package cabbage.project.lawyerSys.common.constant;

//系统常量
public class SystemConstant {

  //待办事项主键
  public static final Long PAY_ITEM_KEY = 1L;
  public static final Long CHOOSE_LAWYER_KEY = 2L;
  public static final Long LAWYER_DETERMINE_UNDER_TAKE = 3L;
  public static final Long DETERMINE_AGREE_CHANGE_LAWYER = 4L;
  public static final Long RE_CHOOSE_LAWYER = 5L;
  public static final Long RE_CHOOSE_LAWYER_LAWYER_AGREE = 6L;


  //认证结果消息
  public enum CertificationMessageEnum {
    NOT(0, "认证成功"), WAIT(1, "待认证"),
    SUCCESS(2, "认证成功"), FAIL(3, "认证失败"),
    ERROR(4, "有异常");

    private final int code;
    private final String msg;

    CertificationMessageEnum(int code, String msg) {
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

  public enum SystemMessageEnum {
    COMPANY_AUTH_APPLY("企业认证申请", "发送认证申请成功， 请耐心等待系统处理", "申请详情", "api/company/auth/info/"),
    LAWYER_AUTH_APPLY("律师认证申请", "发送认证申请成功， 请耐心等待系统处理", "申请详情", "api/lawyer/auth/info/"),
    PROCESS_CER("处理认证申请", "系统已完成对您发起的身份认证申请的审核。", "审核结果", "/api/process/user/auth/info/"),
    REGISTER_SUCCESS("登记成功", "您发起的咨询已成功被系统登记， 请等待系统审核。", "项目详情", "/api/project/info/"),
    AUDIT_SUCCESS("审核通过", "您发起的咨询已审核通过， 系统会尽快根据您的需求确定服务方案", "审核意见", "/api/project/system/audit/info/"),
    AUDIT_FAIL("审核不通过", "您发起的咨询审核不通过， 请查看系统审核意见", "审核意见", "/api/project/system/audit/info/"),
    WAIT_TO_PAY("待支付", "系统已成功为您分配好服务方案， 请前往待办事项中支付费用", "服务方案", "/api/project/system/plan/info/"),
    PAY_INFO("支付记录", "支付成功，请前往待办事项指定服务律师", "交易记录", "/api/project/pay/info/"),
    WAIT_TO_DIS_LAWYER("待系统分配律师", "系统将尽快为您分配律师", "选择律师信息", "/api/project/demandLawyer/info/"),
    PAY_REMIND("再次提醒用户支付", "请尽快完成支付， 否则系统将终止此次交易！", "", ""),
    WAIT_TO_UNDER_TAKE("等待律师承接项目", "系统已为您分配好律师， 请等待律师决定是否承接项目", "分配信息", "/api/project/distributeLawyer/info/"),
    LAWYER_DETERMINE_UNDER_TAKE("决定是否承接项目", "系统为您分配一个新的项目，请尽快决定是否承接项目", "查看详情", "api/project/info/"),
    UNDER_TAKE_REMIND("再次提醒律师承接项目", "请尽快决定是否承接项目， 否则系统将默认您拒绝承接项目", "", ""),
    REFUSE_UNDER_TAKE("拒绝承接项目", "系统为您分配律师拒绝承接项目， 请前往待办事项重新选择律师", "拒绝理由", "api/project/lawyerCarry/info/"),
    ACCEPT_UNDER_TAKE("接受委托", "系统为您分配的律师已接受委托， 律师将按照指定的开始日期开始服务", "律师信息", "api/user/lawyer/info/"),
    CHANGE_LAWYER_APPLY("申请更换律师", "您负责的项目的企业方申请更换律师， 请做出处理", "查看详情", "api/project/changeLawyer/info/"),
    CHANGE_LAWYER_AUDIT_FAIL("更换律师申请审核不通过", "您发起的更换律师申请审核不通过， 请点击查看审核意见", "审核意见", "api/project/changeLawyerAudit/info/"),
    CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_LAWYER("企业更换律师申请审核通过（给律师方消息）", "你负责的项目的企业方申请更换律师, 请进行处理", "查看详情", "api/project/changeLawyerAudit/info/"),
    CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_COMPANY("企业更换律师申请审核通过（给企业方消息）", "您发起的更换律师申请已审核通过， 请等待律师操作", "审核意见", "api/project/changeLawyerAudit/info/"),
    CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_LAWYER("律师更换律师申请审核通过（给律师方消息）", "您发起的更换律师申请已审核通过，系统已通知企业方, 在系统为企业重新分配律师之前，请继续为企业提供服务", "查看详情", "api/project/changeLawyerAudit/info/"),
    CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_COMPANY("律师更换律师申请审核通过（给企业方消息）", "您发起的咨询项目的律师方申请终止服务， 系统已审核通过， 请前往待办事项重新选择律师", "查看详情", "api/project/changeLawyerAudit/info/"),
    LAWYER_AGREE_CHANGE_LAWYER("律师同意更换律师申请", "律师已经同意您的更换律师申请， 请尽快前往待办事项重新选择律师", "", ""),
    LAWYER_COMPLAINT("律师提出申诉", "律师拒绝了您的更换律师申请并提出申诉， 系统会尽快进行处理", "查看详情", "api/project/lawyerComplaint/info/"),
    COMPANY_EVALUATION("企业作出评价", "企业已对您的服务作出了评价，点击查看", "查看详情", "api/project/evaluation/info/"),
    RENEWAL("企业续期", "您负责的项目的企业方续期项目成功， 服务时间会延长", "查看详情", "/api/project/system/plan/info/"),
    PAY_PAST_DUE("支付过期", "您错过了支付期限，请重新发起咨询服务请求", "", ""),
    CHOOSE_LAWYER_PAST_DUE("选择律师过期", "您错过了选择律师操作期限， 系统会为您推荐律师", "", ""),
    DETERMINE_UNDER_TAKE_PAST_DUE_TO_LAWYER("决定是否承接项目过期", "您错过了决定是否承接项目的期限, 系统自动执行拒绝操作", "项目详情", "/api/project/info"),
    DETERMINE_UNDER_TAKE_PAST_DUE_TO_COMPANY("决定是否承接项目过期", "代理律师用户没有在规定时间内作出选择，系统默认律师用户拒绝承接项目", "律师详情", "api/user/lawyer/info/"),
    DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_LAWYER("处理企业用户更换律师申请过期", "您没有在规定时间内决定是否同意企业用户的更换律师申请，系统默认执行同意操作", "申请详情", "api/project/changeLawyerAudit/info/");

    private final String brief;
    private final String detail;
    private final String keyWord;
    private final String url;

    SystemMessageEnum(String brief, String detail, String keyWord, String url) {
      this.brief = brief;
      this.detail = detail;
      this.keyWord = keyWord;
      this.url = url;
    }

    public String getBrief() {
      return brief;
    }

    public String getDetail() {
      return detail;
    }

    public String getKeyWord() {
      return keyWord;
    }

    public String getUrl() {
      return url;
    }
  }
}
