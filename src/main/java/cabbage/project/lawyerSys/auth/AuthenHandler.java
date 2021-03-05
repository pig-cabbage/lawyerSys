package cabbage.project.lawyerSys.auth;


import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.common.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自定义的拦截处理器
 *
 * @author weizli
 * @email 2089319261@qq.com
 * @date 2020-11-30 10:44:09
 */
@Component
public class AuthenHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, AccessDeniedHandler, LogoutSuccessHandler {

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * 认证失败
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    httpServletResponse.setContentType("application/json;charset=UTF-8");
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.error((RunException) e.getCause())));
  }

  /**
   * 认证成功
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    httpServletResponse.setContentType("application/json;charset=UTF-8");
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.ok()
        .put("data", objectMapper.writeValueAsString(authentication))));
  }


  /**
   * 没有权限
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(R.error(RunException.builder().code(ExceptionCode.ACCESS_DENY).build())));
  }

  /**
   * 登出成功
   */
  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(R.ok()
        .put("data", "退出成功")));
  }
}
