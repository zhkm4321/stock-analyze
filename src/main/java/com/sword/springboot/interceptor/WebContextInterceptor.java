package com.sword.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sword.springboot.conf.security.SecurityUser;

public class WebContextInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (null != SecurityContextHolder.getContext().getAuthentication()) {
      Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (null != userDetails) {
        if (userDetails instanceof SecurityUser) {
          SecurityUser user = (SecurityUser) userDetails;
          request.setAttribute("curuser", user);
        }
      }
    }
    // 获取session
    // HttpSession session = request.getSession(true);
    // 判断用户ID是否存在，不存在就跳转到登录界面
    request.setAttribute("res", "/static");
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {

  }

}
