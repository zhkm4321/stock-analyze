package com.sword.springboot.conf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomUserDetailsService customUserDetailSvc;

  // http://localhost:8080/login 输入正确的用户名密码 并且选中remember-me 则登陆成功，转到 index页面
  // 再次访问index页面无需登录直接访问
  // 访问http://localhost:8080/home 不拦截，直接访问，
  // 访问http://localhost:8080/hello 需要登录验证后，且具备
  // “ADMIN”权限hasAuthority("ADMIN")才可以访问
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers(new String[] { "/favicon.ico", "/static/**", "/login", "/regist" }).permitAll()// 访问：/login
        // 无需登录认证权限
        .anyRequest().authenticated() // 其他所有资源都需要认证，登陆后访问
         // 登陆后之后拥有“admin”权限才可以访问/login方法，否则系统会出现“403”权限不足的提示
        .and().formLogin().loginPage("/login")// 指定登录页是”/login”
        .permitAll().successHandler(loginSuccessHandler()).failureHandler(loginFailHandler()) // 登录成功后可使用loginSuccessHandler()存储用户信息，可选。
        .and().logout().logoutSuccessUrl("/login") // 退出登录后的默认网址是”/home”
        .permitAll().invalidateHttpSession(true).and().rememberMe()// 登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
        .tokenValiditySeconds(1209600);
    // 如果有需要取消csrf的就可以参考这里的配置
    http.csrf().ignoringAntMatchers("/druid/**");
    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // 指定密码加密所使用的加密器为passwordEncoder()
    // 需要将密码加密后写入数据库
    auth.userDetailsService(customUserDetailSvc).passwordEncoder(passwordEncoder());
    auth.eraseCredentials(false);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(4);
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler loginFailHandler() {
    return new LoginFailureHandler();
  }

}
