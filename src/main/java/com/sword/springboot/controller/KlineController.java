package com.sword.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sword.springboot.util.AjaxRespUtils;
import com.sword.springboot.util.LoggerUtils;

/**
 * 股票k线图数据获取
 * 
 * @author sword
 *
 */
@Controller
@RequestMapping("/kline")
public class KlineController {

  /**
   * 
   * @return
   */
  @GetMapping("/{code}")
  @ResponseBody
  public String cacheStock(@PathVariable("code") String code) {
    LoggerUtils.debug(this.getClass(), code + "第三方刚是大法官绝地反击");
    return AjaxRespUtils.renderSuccess(code);
  }

}
