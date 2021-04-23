package cabbage.project.lawyerSys.config;


import cabbage.project.lawyerSys.auth.AuthenHandler;
import cabbage.project.lawyerSys.auth.AuthenProvider;
import cabbage.project.lawyerSys.auth.CustomLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private AuthenProvider myAuthrnProvider;
  @Autowired
  private AuthenHandler myAuthenHandler;


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/api/dataStatistics/*").hasRole("ADMIN")
        .antMatchers("/api/project/lawyer/*/*").hasRole("LAWYER_YES")
        .antMatchers("/api/project/company/demand/add").hasRole("COMPANY_YES")
        .antMatchers("/api/project/company/*/allList").hasRole("COMPANY_YES")
        .antMatchers("/api/project/*/system/*").hasRole("ADMIN")
        .antMatchers("/api/project/*/company/*").hasRole("COMPANY_YES")
        .antMatchers("/api/project/*/lawyer/*").hasRole("LAWYER_YES")
        .antMatchers("/api/project/system/*").hasRole("ADMIN")
        .antMatchers("/api/project/distributeLawyer/*/latestRecord").hasAnyRole("ADMIN", "COMPANY_YES")
        .antMatchers("/api/project/distributePlan/*/closestRecord").hasAnyRole("ADMIN", "COMPANY_YES")
        .antMatchers("/api/project/changeLawyer/*/latestInfo").hasAnyRole("ADMIN", "COMPANY_YES", "LAWYER_YES")
        .antMatchers("/api/project/file/*").hasAnyRole("ADMIN", "COMPANY_YES", "LAWYER_YES")
        .antMatchers("/api/project/*").hasRole("ADMIN")
        .antMatchers("/api/service/file/template/*/list").hasAnyRole("ADMIN", "COMPANY_YES", "LAWYER_YES")
        .antMatchers("/api/service/file/template/*").hasRole("ADMIN")
        .antMatchers("/api/service/level/add").hasRole("ADMIN")
        .antMatchers("/api/service/plan/update").hasRole("ADMIN")
        .antMatchers("/api/service/plan/delete").hasRole("ADMIN")
        .antMatchers("/api/service/plan/*/fileUpload").hasRole("ADMIN")
        .antMatchers("/api/process/user/*").hasRole("ADMIN")
        .antMatchers("/api/user/company/auth/apply").hasAnyRole("COMPANY_NO", "COMPANY_YES")
        .antMatchers("/api/user/lawyer/auth/apply").hasAnyRole("LAWYER_NO", "LAWYER_YES")
        .antMatchers("/api/user/*/auth/*").hasRole("ADMIN")
        .antMatchers("/api/user/*/search").hasRole("ADMIN")
        .antMatchers("/oss/policy").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().hasAnyRole("ADMIN", "COMPANY_YES", "LAWYER_YES")
        .and().formLogin()
        .and().exceptionHandling()
        .accessDeniedHandler(myAuthenHandler)
        .and().logout()
        .logoutSuccessHandler(myAuthenHandler)
        .and().cors().and().csrf().disable();
    http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
    authenticationManagerBuilder.authenticationProvider(myAuthrnProvider);
  }

  @Bean
  CustomLoginFilter customAuthenticationFilter() throws Exception {
    CustomLoginFilter customLoginFilter = new CustomLoginFilter();
    customLoginFilter.setAuthenticationFailureHandler(myAuthenHandler);
    customLoginFilter.setAuthenticationSuccessHandler(myAuthenHandler);
    customLoginFilter.setAuthenticationManager(super.authenticationManager());
    return customLoginFilter;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.setAllowCredentials(true);

    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
