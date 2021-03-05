package cabbage.project.lawyerSys.common.exception;

import lombok.Builder;

import java.util.Map;

public class RunException extends RuntimeException {
  private final ExceptionCode code;
  private final Map<String, Object> data;

  @Builder
  public RunException(String message, Throwable cause, ExceptionCode code, Map<String, Object> data) {
    super(message, cause);
    this.data = data;
    this.code = code != null ? code : ExceptionCode.UNKNOW_EXCEPTION;
  }

  public ExceptionCode getCode() {
    return code;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public static class RunExceptionBuilder {
    public RunExceptionBuilder cause(Throwable cause) {
      this.cause = cause;
      if (this.message == null) {
        this.message = cause.getMessage();
      }
      if ((this.code == null || this.code == ExceptionCode.UNKNOW_EXCEPTION) && cause instanceof RunException) {
        this.code = ((RunException) cause).getCode();
      }
      return this;
    }
  }
}
