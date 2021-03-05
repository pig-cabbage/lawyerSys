package cabbage.project.lawyerSys.exception;

import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.R;
import cabbage.project.lawyerSys.valid.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(value = "cabbage.project.lawyerSys.controller")
public class LawyerSysExceptionControllerAdvice {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public R headleVaildException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    Map<String, String> map = new HashMap<>();
    bindingResult.getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
    return R.error(ExceptionCode.VALID_EXCEPTION).put("data", map);
  }

  @ExceptionHandler(value = RunException.class)
  public R handleException(RunException e) {
    R r = R.error(e.getCode());
    Assert.isNotBlank(e.getMessage(), message ->
        {
          r.put("msg", message);
          r.remove("code");
        }
    );
    Assert.isNotNull(e.getCause(), cause -> r.put("cause", cause));
    return r;
  }
}
