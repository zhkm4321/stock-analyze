/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sword.springboot.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageInfo;
import com.sword.springboot.model.TbStocks;
import com.sword.springboot.service.TbStocksService;
import com.sword.springboot.util.AjaxRespUtils;
import com.sword.springboot.util.HttpClientUtils;
import com.sword.springboot.util.LoggerUtils;
import com.sword.springboot.util.RegxUtils;
import com.sword.springboot.util.StringUtils;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@Controller
@RequestMapping("/stocks")
public class TbStockController {

  @Autowired
  private TbStocksService stockSvc;

  @GetMapping
  public ModelAndView listAll(TbStocks stock) {
    ModelAndView result = new ModelAndView("index");
    List<TbStocks> stockList = stockSvc.selectByExample(stock, null, null);
    result.addObject("pageInfo", new PageInfo<TbStocks>(stockList));
    result.addObject("queryBean", stock);
    return result;
  }

  @RequestMapping(value = "/add")
  public ModelAndView add() {
    ModelAndView result = new ModelAndView("view");
    result.addObject("country", new TbStocks());
    return result;
  }

  @RequestMapping(value = "/view/{id}")
  public ModelAndView view(@PathVariable Integer id) {
    ModelAndView result = new ModelAndView("view");
    TbStocks bean = stockSvc.getById(id);
    result.addObject("bean", bean);
    return result;
  }

  @RequestMapping(value = "/delete/{id}")
  public ModelAndView delete(@PathVariable Integer id, RedirectAttributes ra) {
    ModelAndView result = new ModelAndView("redirect:/stocks");
    stockSvc.deleteById(id);
    ra.addFlashAttribute("msg", "删除成功!");
    return result;
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ModelAndView save(TbStocks bean) {
    ModelAndView result = new ModelAndView("view");
    String msg = bean.getId() == null ? "新增成功!" : "更新成功!";
    stockSvc.saveOrUpdate(bean);
    result.addObject("country", bean);
    result.addObject("msg", msg);
    return result;
  }

  /**
   * 深圳上海股票
   */
  @GetMapping("/refresh")
  @ResponseBody
  public String stockList() {
    String url = "http://quote.eastmoney.com/stock_list.html";// 请求接口地址
    try {
      String html = HttpClientUtils.sendHttpGet(url, null, "gb2312");
      Document doc = Jsoup.parse(html);
      Elements listDiv = doc.select("div[id=quotesearch]");
      Elements ul = listDiv.select("ul");
      String code = null;
      List<TbStocks> stockList = new ArrayList<TbStocks>();
      String contentText = null;
      String exchange = null;// 交易所类型
      for (int i = 0; i < ul.size(); i++) {
        Elements liCol = ul.get(i).select("li");
        if (i == 0) {
          exchange = "sh";
        } else {
          exchange = "sz";
        }
        for (Iterator<Element> it = liCol.iterator(); it.hasNext();) {
          Element element = (Element) it.next();
          contentText = element.text();
          code = RegxUtils.getParenthesesContent(contentText);
          if (code.startsWith("0") || code.startsWith("7") || code.startsWith("6") || code.startsWith("3")) {
            TbStocks stocks = new TbStocks();
            stocks.setCode(code);
            stocks.setStockName(contentText.substring(0, contentText.indexOf("(")));
            stocks.setStockExchange(exchange);
            stockList.add(stocks);
          }
        }
      }
      int count = 0;
      for (Iterator<TbStocks> iterator = stockList.iterator(); iterator.hasNext();) {
        TbStocks stocks = (TbStocks) iterator.next();
        stockSvc.saveOrUpdate(stocks);
        count++;
      }
      LoggerUtils.info(getClass(), "共刷新" + count + "条记录");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return AjaxRespUtils.renderSuccess("刷新成功");

  }

  @GetMapping("/list")
  public ModelAndView stockList(TbStocks stock) {
    ModelAndView result = new ModelAndView("stock_list");
    List<TbStocks> stockList = stockSvc.selectByExample(stock, null, null);
    result.addObject("pageInfo", new PageInfo<TbStocks>(stockList));
    result.addObject("queryBean", stock);
    Date nowDate = new Date();
    Calendar cal = new GregorianCalendar();
    cal.setTime(nowDate);
    cal.add(Calendar.DAY_OF_YEAR, -120);
    Date[] dateRange = new Date[] { cal.getTime(), nowDate };
    result.addObject("dateRange", dateRange);
    return result;
  }

  @PostMapping("/list")
  @ResponseBody
  public String postStockList(TbStocks stock, String searchString, String columns, String orders) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    try {
      if(StringUtils.isNotBlank(searchString)){
        Long.parseLong(searchString);
        if(null==stock.getCode()){
          stock.setCode(searchString);
        }
      }
    } catch (NumberFormatException e) {
      if(null==stock.getStockName()){
        stock.setStockName(searchString);
      }
    }
    List<TbStocks> stockList = stockSvc.selectByExample(stock, columns, orders);
    result.put("pageInfo", new PageInfo<TbStocks>(stockList));
    result.put("queryBean", stock);
    
    return AjaxRespUtils.renderSuccess(result, "获取成功");
  }

}
