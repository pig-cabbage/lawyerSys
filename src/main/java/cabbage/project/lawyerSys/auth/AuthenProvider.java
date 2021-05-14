package cabbage.project.lawyerSys.auth;


import cabbage.project.lawyerSys.common.exception.ExceptionCode;
import cabbage.project.lawyerSys.common.exception.RunException;
import cabbage.project.lawyerSys.dto.WeixinAuthDTO;
import cabbage.project.lawyerSys.entity.UserAccountEntity;
import cabbage.project.lawyerSys.form.SysLoginForm;
import cabbage.project.lawyerSys.service.UserAccountService;
import cabbage.project.lawyerSys.service.UserCompanyService;
import cabbage.project.lawyerSys.service.UserLawyerService;
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
  @Autowired
  private UserLawyerService userLawyerService;
  @Autowired
  private UserCompanyService userCompanyService;


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
      Assert.isNotNull(weixinAuthDTO.getErrcode(), errCode -> {
        if (40029 == errCode || 45011 == errCode) {
          throw new AuthenticationServiceException("请求微信数据失败", RunException.builder().code(ExceptionCode.GET_WEIXIN_AUTH_FAIL).build());
        }
      });
      UserAccountEntity account = userAccountService.getById(weixinAuthDTO.getOpenid());
      if (account != null) {
        if (!form.getRole().equals(account.getRole())) {
          throw new AuthenticationServiceException("登录身份不匹配", RunException.builder().code(ExceptionCode.LOGIN_ROLE_WRONG).build());
        }
        StringBuilder role = new StringBuilder();
        if (account.getRole() == 0) {
          role.append("ROLE_ADMIN");
        } else {
          role.append(account.getRole() == 1 ? "ROLE_COMPANY" : "ROLE_LAWYER");
          if (account.getCertificationStatus() == 2) {
            role.append("_YES");
          } else {
            if (account.getCertificationStatus() == 0) {
              role.append("_NO");
            } else {
              if (role.toString().equals("ROLE_COMPANY")) {
                if (userCompanyService.getByAccount(account.getId()) != null) {
                  role.append("_YES");
                } else {
                  role.append("_NO");
                }
              } else {
                if (userLawyerService.getByAccount(account.getId()) != null) {
                  role.append("_YES");
                } else {
                  role.append("_NO");
                }
              }
            }


          }
        }
        return new UsernamePasswordAuthenticationToken(account, weixinAuthDTO, Collections.singletonList(new SimpleGrantedAuthority(role.toString())));
      } else {
        if (0 == form.getRole()) {
          throw new AuthenticationServiceException("你不是管理员");
        }
        return new UsernamePasswordAuthenticationToken(userAccountService.addAccount(weixinAuthDTO.getOpenid(), form.getRole()), weixinAuthDTO, Collections.singletonList(new SimpleGrantedAuthority((form.getRole() == 1 ? "ROLE_COMPANY" : "ROLE_LAWYER") + "_NO")));
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
