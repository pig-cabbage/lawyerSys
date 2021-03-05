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
        .antMatchers("/login", "/logout", "/register", "/doLogin", "/captcha.jpg").permitAll()
        .antMatchers("/bookmanager/books/list").hasAnyRole("ADMIN", "USER")
        .antMatchers("/bookmanager/books/*").hasRole("ADMIN")
        .anyRequest().permitAll()
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
