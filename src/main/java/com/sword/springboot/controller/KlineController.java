package com.sword.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sword.springboot.model.TbStocks;
import com.sword.springboot.model.TbStocksHistory;
import com.sword.springboot.service.KlineService;
import com.sword.springboot.service.TbStocksService;
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

  @Autowired
  private TbStocksService stockSvc;

  /**
   * 
   * @return
   */
  @PostMapping("/{code}")
  @ResponseBody
  public String cacheStock(@PathVariable("code") String code, String startDate, String endDate) {
    Map<String, Object> result = new HashMap<String, Object>();
    TbStocks stock = stockSvc.redisGetStock(code);
    List<TbStocksHistory> hisList = klineSvc.getStockDailyHistory(code, startDate, endDate);
    result.put("lineData", hisList);
    result.put("stock", stock);
    return AjaxRespUtils.renderSuccess(result, "返回成功");
  }

  @GetMapping("/refresh/{code}")
  @ResponseBody
  public String refreshStockKline(@PathVariable("code") String code) {
    Map<String, Object> result = new HashMap<String, Object>();
    TbStocks stock = stockSvc.redisGetStock(code);
    if (null != stock) {
      klineSvc.refreshStockKline(code);
      result.put("code", code);
    }
    return AjaxRespUtils.renderSuccess(result, "返回成功");
  }

}
