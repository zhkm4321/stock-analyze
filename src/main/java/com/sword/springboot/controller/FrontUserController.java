package com.sword.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sword.springboot.exception.ErrorInfo;
import com.sword.springboot.model.TbUser;
import com.sword.springboot.service.TbUserService;
import com.sword.springboot.util.AjaxRespUtils;
import com.sword.springboot.util.LoggerUtils;

@Controller
public class FrontUserController {

  @GetMapping("/login")
  public String index(ModelMap model) {
    model.put("errors", "");
    return "user/login";
  }

  @GetMapping("/regist")
  public String regist(ModelMap model) {
    model.put("errors", "");
    return "user/regist";
  }

  @PostMapping("/login")
  @ResponseBody
  public String postLogin(String username, String password) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("errors", "");
    return AjaxRespUtils.renderSuccess(result, "登录成功");
  }

  @PostMapping("/regist")
  @ResponseBody
  public String postRegist(String username, String password, String repeatPassword) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("errors", "");
    if (password.equals(repeatPassword)) {
      TbUser dbUser = null;
      try {
        dbUser = userSvc.findByUsername(username);
      } catch (UsernameNotFoundException e1) {
      }
      if (null != dbUser) {
        return AjaxRespUtils.renderErrors("已经存在同名用户");
      }
      try {
        String encodedPwd = passwordEncoder.encode(password);
        TbUser user = new TbUser();
        user.setPassword(encodedPwd);
        user.setUsername(username);
        userSvc.save(user);
        return AjaxRespUtils.renderSuccess(result, "注册成功");
      } catch (RuntimeException e) {
        LoggerUtils.error(this.getClass(), e.getMessage(), e);
      }
    } else {
      return AjaxRespUtils.renderErrors("两次输入的密码不一致");
    }
    return AjaxRespUtils.renderErrors("注册失败");
  }

  @GetMapping("/ajaxLogout")
  @ResponseBody
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    } else {
      return AjaxRespUtils.renderErrors("当前没有用户登录");
    }
    return AjaxRespUtils.renderSuccess("注销成功");
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public String jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    ErrorInfo<String> r = new ErrorInfo<>();
    r.setMessage(e.getMessage());
    r.setCode(ErrorInfo.ERROR);
    r.setUrl(req.getRequestURL().toString());
    e.printStackTrace();
    return AjaxRespUtils.renderErrors(r, r.getMessage());
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private TbUserService userSvc;
}
