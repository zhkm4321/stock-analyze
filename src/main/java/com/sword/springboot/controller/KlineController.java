package com.sword.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sword.springboot.model.TbStocksHistory;
import com.sword.springboot.service.KlineService;
import com.sword.springboot.util.AjaxRespUtils;

/**
 * 股票k线图数据获取
 * 
 * @author sword
 *
 */
@Controller
@RequestMapping("/kline")
public class KlineController {

  @Autowired
  private KlineService klineSvc;

  /**
   * 
   * @return
   */
  @PostMapping("/{code}")
  @ResponseBody
  public String cacheStock(@PathVariable("code") String code, String startDate, String endDate) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<TbStocksHistory> stocks = klineSvc.getStockDailyHistory(code, startDate, endDate);
    result.put("lineData", stocks);
    return AjaxRespUtils.renderSuccess(result, "返回成功");
  }

}
