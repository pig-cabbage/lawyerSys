package cabbage.project.lawyerSys.auth;


import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.dto.WeixinAuthDTO;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.form.SysLoginForm;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.valid.Assert;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthenProvider implements AuthenticationProvider {

  @Value("${weixin.url}")
  private String url;
  @Value("${weixin.app-id}")
  private String appId;
  @Value("${weixin.app-secret}")
  private String appSecret;

  @Autowired
  private UserAccountService userAccountService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    SysLoginForm form = (SysLoginForm) authentication.getPrincipal();
    Assert.isNotNull(form.getJsCode());
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet httpGet = new HttpGet(url + "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + form.getJsCode() + "&grant_type=authorization_code");
    RequestConfig requestConfig = RequestConfig.custom()
        .setSocketTimeout(2000) //服务器响应超时时间
        .setConnectTimeout(2000) //连接服务器超时时间
        .build();
    httpGet.setConfig(requestConfig);
    try {
      CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
      HttpEntity entity = httpResponse.getEntity();
      String s = EntityUtils.toString(entity);//转换成字符串
      WeixinAuthDTO weixinAuthDTO = JSONObject.parseObject(s, WeixinAuthDTO.class);
      UserAccountEntity account = userAccountService.getById(weixinAuthDTO.getOpenid());
      if (account != null) {
        if (!form.getRole().equals(account.getRole())) {
          throw new AuthenticationServiceException("登录身份不匹配", RunException.builder().code(ExceptionCode.LOGIN_ROLE_WRONG).build());
        }
        return new UsernamePasswordAuthenticationToken(account, weixinAuthDTO, Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(account.getRole()))));
      } else {
        return new UsernamePasswordAuthenticationToken(userAccountService.addAccount(weixinAuthDTO.getOpenid(), form.getRole()), weixinAuthDTO, null);
      }
    } catch (IOException e) {
      throw new AuthenticationServiceException("请求微信数据失败", RunException.builder().code(ExceptionCode.GET_WEIXIN_AUTH_FAIL).build());
    }
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }
}
