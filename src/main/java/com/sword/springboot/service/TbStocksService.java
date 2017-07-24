package com.sword.springboot.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.sword.springboot.mapper.TbStocksMapper;
import com.sword.springboot.model.TbStocks;
import com.sword.springboot.model.TbStocksExample;
import com.sword.springboot.util.LoggerUtils;
import com.sword.springboot.util.RedisUtils;
import com.sword.springboot.util.StringUtils;

import redis.clients.jedis.Jedis;

@Service
public class TbStocksService {

  private final String STOCKS = "STOCKS";

  @Autowired
  private TbStocksMapper stockMapper;

  public List<TbStocks> selectByExample(TbStocks bean) {
    if (bean.getPage() != null && bean.getRows() != null) {
      PageHelper.startPage(bean.getPage(), bean.getRows());
    }
    TbStocksExample example = new TbStocksExample();
    TbStocksExample.Criteria criteria = example.createCriteria();
    if(StringUtils.isNotBlank(bean.getCode())){
      criteria.andCodeLike(bean.getCode());
    }
    if(StringUtils.isNotBlank(bean.getStockName())){
      criteria.andStockNameLike(bean.getStockName());
    }
    return stockMapper.selectByExample(example);
  }

  public TbStocks getById(Integer id) {
    return stockMapper.selectByPrimaryKey(id);
  }

  public void deleteById(Integer id) {
    stockMapper.deleteByPrimaryKey(id);
  }

  public void saveOrUpdate(TbStocks bean) {
    Jedis jedis = null;
    try {
      jedis = RedisUtils.getJedis();
      String stockKey = bean.getStockExchange() + bean.getCode();
      String stockJson = jedis.hget(STOCKS, stockKey);
      if (StringUtils.isBlank(stockJson)) {
        stockMapper.insert(bean);
        jedis.hset(STOCKS, stockKey, JSON.toJSONString(bean));
      } else {
        TbStocks cacheBean = JSON.parseObject(stockJson, TbStocks.class);
        bean.setId(cacheBean.getId());
        stockMapper.updateByPrimaryKey(bean);
        jedis.hset(STOCKS, stockKey, JSON.toJSONString(bean));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      RedisUtils.closeConn(jedis);
    }
  }

  public void initRedisCache() {
    List<TbStocks> stockList = stockMapper.selectAll();
    for (Iterator<TbStocks> it = stockList.iterator(); it.hasNext();) {
      TbStocks bean = (TbStocks) it.next();
      redisSaveStock(bean);
    }
  }

  public void redisSaveStock(TbStocks bean) {
    Jedis jedis = null;
    try {
      jedis = RedisUtils.getJedis();
      String stockKey = "code_" + bean.getCode();
      jedis.hset(STOCKS, stockKey, JSON.toJSONString(bean));
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
  }

  public TbStocks redisGetStock(String code) {
    Jedis jedis = null;
    try {
      jedis = RedisUtils.getJedis();
      String stockKey = "code_"+code;
      String stockJson = jedis.hget(STOCKS, stockKey);
      return JSON.parseObject(stockJson, TbStocks.class);
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
    return null;
  }

}
