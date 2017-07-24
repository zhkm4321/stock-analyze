package com.sword.springboot.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfig {

  @Bean
  public FilterRegistrationBean getFilterRegistrationBean() {
    FilterRegistrationBean filter = new FilterRegistrationBean();
    filter.setFilter(new WebStatFilter());
    filter.setName("druidWebStatFilter");
    filter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
    filter.addUrlPatterns("/*");
    return filter;
  }

  @Bean
  public ServletRegistrationBean getServletRegistrationBean() {
    ServletRegistrationBean servlet = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    servlet.setName("druidStatViewServlet");
    servlet.addInitParameter("resetEnable", "false");
    return servlet;
  }
}
