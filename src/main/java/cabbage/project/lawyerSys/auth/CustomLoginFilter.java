package cabbage.project.lawyerSys.auth;


import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.form.SysLoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {

    try {
      InputStream is = request.getInputStream();
      SysLoginForm form = objectMapper.readValue(is, SysLoginForm.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(form, form.getJsCode());
      this.setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw RunException.builder().code(ExceptionCode.UNKNOW_EXCEPTION).build();
    }

  }

}
