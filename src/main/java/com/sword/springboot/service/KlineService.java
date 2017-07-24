package com.sword.springboot.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sword.springboot.model.TbStocks;
import com.sword.springboot.model.TbStocksHistory;
import com.sword.springboot.util.CsvUtils;
import com.sword.springboot.util.DateUtil;
import com.sword.springboot.util.StringUtils;

@Service
public class KlineService {

  @Autowired
  private TbStocksService stockSvc;
  
  private TbStocksHistoryService historySvc;

  /**
   * 日K线记录
   * 
   * @param code
   * @param startDate
   * @param endDate
   * @return
   */
  public List<TbStocksHistory> getStockDailyHistory(String code, String startDate, String endDate) {
    List<TbStocksHistory> hisList = historySvc.getDaliyHistoryFromCache(code, startDate, endDate);
    if(CollectionUtils.isEmpty(hisList)){
      hisList = cacheDaliyHistory(code, startDate, endDate);
    }
    return hisList;
  }

  private List<TbStocksHistory> cacheDaliyHistory(String code, String startDate, String endDate){
    List<TbStocksHistory> hisList = get163DaliyHistory(code, startDate, endDate);
    //将数据缓存到redis中
    historySvc.redisSaveHistory(hisList);
    //异步持久化到数据库
    historySvc.syncDBSaveHistory(hisList);
    return hisList;
  }
  
  private List<TbStocksHistory> get163DaliyHistory(String code, String startDate, String endDate){
    String httpUrl = "http://quotes.money.163.com/service/chddata.html";
    Map<String, String> maps = new HashMap<String, String>();
    TbStocks stock = stockSvc.redisGetStock(code);
    List<TbStocksHistory> hisList = new ArrayList<>();
    if (null != stock) {
      String codeStr = null;
      if (stock.getStockExchange().equals("sh")) {
        codeStr = "0" + code;
      } else if (stock.getStockExchange().equals("sz")) {
        codeStr = "1" + code;
      }
      maps.put("code", codeStr);
      maps.put("start", startDate);
      maps.put("end", endDate);
      List<String[]> result = CsvUtils.readCsvFromUrl(httpUrl, maps, "gbk");
      for (Iterator<String[]> iterator = result.iterator(); iterator.hasNext();) {
        String[] cells = (String[]) iterator.next();
        try {
          TbStocksHistory his = new TbStocksHistory(stock.getCode(), DateUtil.sdf.parse(cells[0]), getBD(cells[3]), getBD(cells[4]),
              getBD(cells[5]), getBD(cells[6]), getBD(cells[7]), getBD(cells[8]), getBD(cells[9]), getBD(cells[10]),
              getBD(cells[11]), getBD(cells[12]), getBD(cells[13]), getBD(cells[14]), getBD(cells[15]));
          hisList.add(his);
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    }
    return hisList;
  }

  private BigDecimal getBD(String sumStr) {
    if(sumStr.equals("None")){
      return null;
    }
    sumStr = StringUtils.replace(sumStr, "E+", "E");
    sumStr = StringUtils.replace(sumStr, "e+", "E");
    return new BigDecimal(sumStr);
  }

  /**
   * 月线记录
   * 
   * @return
   */
  public List<TbStocksHistory> getStockMonthlyHistory(String code, String startDate, String endDate) {
    return null;
  }

  /**
   * 周线记录
   * 
   * @return
   */
  public List<TbStocksHistory> getStockWeeklyHistory(String code, String startDate, String endDate) {
    return null;
  }
}
