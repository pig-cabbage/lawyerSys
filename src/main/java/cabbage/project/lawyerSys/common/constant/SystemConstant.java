package cabbage.project.lawyerSys.common.constant;

//系统常量
public class SystemConstant {

  //用户身份代码
  public static final Integer ROLE_COM = 1;
  public static final Integer ROLE_LAW = 2;

  //Redis相关
  public static final String WEBSOCKET_KEY_PREFIX = "SOCKET_";

  //待办事项主键
  public static final Long PAY_ITEM_KEY = 1L;
  public static final Long CHOOSE_LAWYER_KEY = 2L;
  public static final Long LAWYER_DETERMINE_UNDER_TAKE = 3L;
  public static final Long DETERMINE_AGREE_CHANGE_LAWYER = 4L;
  public static final Long RE_CHOOSE_LAWYER = 5L;
  public static final Long RE_CHOOSE_LAWYER_LAWYER_AGREE = 6L;
  public static final Long RE_CHOOSE_LAWYER_REFUSE_LAWYER = 7L;
  public static final Long RENEW_PROJECT = 8L;
  public static final Long ADD_CHAT_RECORD = 9L;
  public static final Long DELETE_CHAT_RECORD = 10L;
  public static final Long START_PROJECT = 11L;
  public static final Long END_PROJECT = 12L;
  public static final Long RE_CHOOSE_LAWYER_LAWYER_REFUSE = 13L;
  public static final Long DEAL_PAST_DUE = 14L;

  //律师同意更换律师到服务结束的缓冲期
  public static final Long CHANGE_LAWYER_GOV = 3L;

  //提醒用户续费项目定时任务Id
  public static final Long REMIND_RENEW = 99L;
  //在项目到期前多久执行提醒的定时任务
  public static final Long RENEW_GOV = 7L;

  //处理续费事项的期限

  //旧律师结束服务到新律师开始服务的时间间隔 默认是1天
  public static final Long OLD_TO_NEW_LAWYER = 1L;

  public static final Integer MAX_CHAR_RECORD_REDIS = 30;

  public static final Long MONTH_DAY = 30L;
  public static final Long MS_OF_DAY = 86400000L;

  //系统消息常量
  public static final String PROCESS_CER = "系统已完成对您发起的身份认证申请的审核, 店家查看审核结果。";
  public static final String COMPANY_AUTH_APPLY = "发送认证申请成功， 请耐心等待系统处理";
  public static final String LAWYER_AUTH_APPLY = "发送认证申请成功， 请耐心等待系统处理";
  public static final String REGISTER_SUCCESS = "您发起的咨询已成功被系统登记， 请等待系统审核。";
  public static final String AUDIT_SUCCESS = "您发起的咨询已审核通过， 系统会尽快根据您的需求确定服务方案";
  public static final String AUDIT_FAIL = "您发起的咨询审核不通过， 请查看审核意见";
  public static final String WAIT_TO_PAY = "系统已成功为您分配好服务方案， 请前往待办事项中支付费用";
  public static final String PAY_REMIND = "请尽快完成支付， 否则系统将终止此次交易！";
  public static final String PAY_INFO = "支付成功，请前往待办事项指定服务律师";
  public static final String WAIT_TO_DIS_LAWYER = "系统将尽快为您分配律师";
  public static final String WAIT_TO_UNDER_TAKE = "系统已为您分配好律师， 请等待律师决定是否承接项目";
  public static final String LAWYER_DETERMINE_UNDER_TAKE_MES = "系统为您分配一个新的项目，请尽快决定是否承接项目";
  public static final String UNDER_TAKE_REMIND = "请尽快决定是否承接项目， 否则系统将默认您拒绝承接项目";
  public static final String REFUSE_UNDER_TAKE = "系统为您分配律师拒绝承接项目， 请前往待办事项重新选择律师";
  public static final String ACCEPT_UNDER_TAKE = "系统为您分配的律师已接受委托， 律师将按照指定的开始日期开始服务";
  public static final String CHANGE_LAWYER_AUDIT_FAIL = "您发起的更换律师申请审核不通过， 请点击查看审核意见";
  public static final String CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_LAWYER = "你负责的项目的企业方申请更换律师, 请进行处理";
  public static final String CHANGE_LAWYER_AUDIT_SUCCESS_COMPANY_TO_COMPANY = "您发起的更换律师申请已审核通过， 请等待律师操作";
  public static final String CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_LAWYER = "您发起的更换律师申请已审核通过，系统已通知企业方, 在系统为企业重新分配律师之前，请继续为企业提供服务";
  public static final String CHANGE_LAWYER_AUDIT_SUCCESS_LAWYER_TO_COMPANY = "您发起的咨询项目的律师方申请终止服务， 系统已审核通过， 请前往待办事项重新选择律师";
  public static final String LAWYER_AGREE_CHANGE_LAWYER = "律师已经同意您的更换律师申请， 请尽快前往待办事项重新选择律师";
  public static final String LAWYER_COMPLAINT = "律师拒绝了您的更换律师申请并提出申诉， 系统会尽快进行处理";
  public static final String COMPANY_EVALUATION = "企业已对您的服务作出了评价，点击查看";
  public static final String RENEWAL = "您负责的项目的企业方续期项目成功， 服务时间会延长";
  public static final String DEAL_COMPLAINT_REFUSE_LAWYER_TO_COMAPNY = "系统驳回律师的申诉请求，请尽快前往待办事项重新选择律师";
  public static final String DEAL_COMPLAINT_REFUSE_LAWYER_TO_LAWYER = "系统驳回律师的申诉请求，三天后您对企业的服务将终止";
  public static final String DEAL_COMPLAINT_REFUSE_COMPANY_TO_COMPANY = "系统驳回企业的更换律师请求，律师会继续为您提供服务";
  public static final String DEAL_COMPLAINT_REFUSE_COMPANY_TO_LAWYER = "系统驳回企业的更换律师请求, 请继续为企业提供服务";
  public static final String PAY_PAST_DUE = "您错过了支付期限，请重新发起咨询服务请求";
  public static final String CHOOSE_LAWYER_PAST_DUE = "您错过了选择律师操作期限， 系统会为您推荐律师";
  public static final String DETERMINE_UNDER_TAKE_PAST_DUE_TO_LAWYER = "您错过了决定是否承接项目的期限, 系统自动执行拒绝操作";
  public static final String DETERMINE_UNDER_TAKE_PAST_DUE_TO_COMPANY = "代理律师用户没有在规定时间内作出选择，系统默认律师用户拒绝承接项目";
  public static final String DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_LAWYER = "您没有在规定时间内决定是否同意企业用户的更换律师申请，系统默认执行同意操作";
  public static final String DETERMINE_AGREE_CHANGE_LAWYER_PAST_DUE_TO_COMPANY = "律师没有在规定时间内处理更换律师申请， 系统默认执行同意操作, 请重新选择律师";
  public static final String LAWYER_START_SERVICE_TO_LAWYER = "您负责的项目开始了，您现在可以与企业交流";
  public static final String LAWYER_START_SERVICE_TO_COMPANY = "您发起的项目的律师已开始服务，您现在可以与律师交流";
  public static final String END_SERVICE_TO_LAWYER = "您对项目的服务已结束， 感谢您的付出！";
  public static final String END_SERVICE_TO_COMPANY = "律师对您项目的服务已终止，请等待新的律师继续为您提供服务";
  public static final String START_PROJECT_TO_COMPANY = "您申请的咨询项目已开始";
  public static final String END_PROJECT_TO_COMPANY = "您申请的项目已到期, 欢迎继续订购我们的服务。";
  public static final String END_PROJECT_TO_LAWYER = "您负责的项目已到期，感谢您的付出。";
  public static final String REMIND_RENEW_PROJECT = "项目即将到期，如果您对我们的服务感到满意，可以省去流程直接续费项目。";
  public static final String RENEW_PROJECT_PAST_DUE_COMPANY = "你没有在规定的时间内决定是否续费项目， 系统默认执行拒绝操作并读项目做出评价。";
  public static final String RENEW_PROJECT_PAST_DUE_LAWYER = "用户已对您的服务做出评价。";


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

//  public enum SystemMessageEnum {

//    private final String brief;
//    private final String detail;
//    private final String keyWord;
//    private final String url;
//
//    SystemMessageEnum(String brief, String detail, String keyWord, String url) {
//      this.brief = brief;
//      this.detail = detail;
//      this.keyWord = keyWord;
//      this.url = url;
//    }
//
//    public String getBrief() {
//      return brief;
//    }
//
//    public String getDetail() {
//      return detail;
//    }
//
//    public String getKeyWord() {
//      return keyWord;
//    }
//
//    public String getUrl() {
//      return url;
//    }
//  }
}
