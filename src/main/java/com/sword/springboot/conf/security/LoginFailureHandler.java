package com.sword.springboot.conf.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.sword.springboot.util.AjaxRespUtils;

public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    response.getOutputStream().write(AjaxRespUtils.renderErrors("用户名密码错误").getBytes("UTF-8"));
    response.getOutputStream().close();
  }

}
