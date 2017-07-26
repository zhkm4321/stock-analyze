package com.sword.springboot.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sword.springboot.mapper.TbStocksHistoryMapper;
import com.sword.springboot.mapper.thread.SyncDBOperatThread;
import com.sword.springboot.model.TbStocksHistory;
import com.sword.springboot.util.DateUtil;
import com.sword.springboot.util.LoggerUtils;
import com.sword.springboot.util.RedisUtils;
import com.sword.springboot.util.SerializeUtil;
import com.sword.springboot.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class TbStocksHistoryService {
  private final String STOCK_HISTORY = "STOCK_HISTORY_HISORY";

  @Autowired
  private TbStocksHistoryMapper stockHistoryMapper;

  /**
   * 
   * @param bean
   * @return
   */
  public List<TbStocksHistory> selectByExample(TbStocksHistory bean) {
    if (bean.getPage() != null && bean.getRows() != null) {
      PageHelper.startPage(bean.getPage(), bean.getRows());
    }
    Example example = new Example(bean.getClass());
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(bean.getCode())) {
      criteria.andEqualTo("code", bean.getCode());
    }
    if (null != bean.getJysj()) {
      criteria.andEqualTo("jysj", bean.getJysj());
    }
    return stockHistoryMapper.selectByExample(example);
  }

  /**
   * 
   * @param code
   * @param startDate
   * @param endDate
   * @return
   */
  public List<TbStocksHistory> getDaliyHistoryFromCache(String code, String startDate, String endDate) {
    try {
      return redisGetHistory(code, DateUtil.sdf2.parse(startDate), DateUtil.sdf2.parse(endDate));
    } catch (ParseException e) {
      LoggerUtils.error(getClass(), "时间格式解析错误", e);
    } ;
    return null;
  }

  /**
   * 将结果保存到redis中
   * 
   * @param hisList
   */
  public void redisSaveHistory(List<TbStocksHistory> hisList) {
    Jedis jedis = null;
    try {
      jedis = RedisUtils.getJedis();
      Pipeline pipe = jedis.pipelined();
      for (Iterator<TbStocksHistory> it = hisList.iterator(); it.hasNext();) {
        TbStocksHistory bean = (TbStocksHistory) it.next();
        String stockKey = "code_" + bean.getCode();
        pipe.zadd(stockKey.getBytes(), dateScore(bean.getJysj()), SerializeUtil.serialize(bean));
      }
      pipe.close();
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
  }

  /**
   * 从redis中获取时间范围结果
   * 
   * @param code
   * @param startDate
   * @param endDate
   * @return
   */
  public List<TbStocksHistory> redisGetHistory(String code, Date startDate, Date endDate) {
    Jedis jedis = null;
    List<TbStocksHistory> list = new ArrayList<>();
    try {
      jedis = RedisUtils.getJedis();
      String stockKey = "code_" + code;
      Set<byte[]> data = jedis.zrangeByScore(stockKey.getBytes(), dateScore(startDate), dateScore(endDate));
      for (Iterator<byte[]> it = data.iterator(); it.hasNext();) {
        byte[] bs = (byte[]) it.next();
        list.add((TbStocksHistory) SerializeUtil.unserialize(bs));
      }
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
    return list;
  }

  private void redisClearHistory(String code) {
    Jedis jedis = null;
    try {
      jedis = RedisUtils.getJedis();
      String stockKey = "code_" + code;
      jedis.zremrangeByScore(stockKey.getBytes(), 0, dateScore(new Date()));
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
  }

  private Double dateScore(Date date) {
    return Double.parseDouble(DateUtil.getDateStr(date, "yyyyMMDD"));
  }

  /**
   * 将数据保存到数据库中
   * 
   * @param hisList
   */
  public void syncDBSaveHistory(List<TbStocksHistory> hisList) {
    Thread saveTh = new Thread(
        new SyncDBOperatThread<>(stockHistoryMapper, hisList, new String[] { "code", "jysj" }, TbStocksHistory.class));
    saveTh.setPriority(Thread.MIN_PRIORITY);
    saveTh.start();
  }

  /**
   * 刷新股票历史k线记录
   * @param code
   */
  public void clearKlineData(String code) {
    Example example = new Example(TbStocksHistory.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("code", code);
    stockHistoryMapper.deleteByExample(example);
    redisClearHistory(code);
  }

}
