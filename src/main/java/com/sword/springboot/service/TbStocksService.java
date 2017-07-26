package com.sword.springboot.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.sword.springboot.mapper.TbStocksMapper;
import com.sword.springboot.model.TbStocks;
import com.sword.springboot.util.LoggerUtils;
import com.sword.springboot.util.RedisUtils;
import com.sword.springboot.util.SerializeUtil;
import com.sword.springboot.util.StringUtils;

import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class TbStocksService extends BaseService {

  private final String STOCKS = "STOCKS";

  @Autowired
  private TbStocksMapper stockMapper;

  public List<TbStocks> selectByExample(TbStocks bean, String columns, String orders) {
    if (bean.getPage() != null && bean.getRows() != null) {
      PageHelper.startPage(bean.getPage(), bean.getRows());
    }
    Example example = new Example(TbStocks.class);
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(bean.getCode())) {
      criteria.andLike("code", "%" + bean.getCode() + "%");
    }
    if (StringUtils.isNotBlank(bean.getStockName())) {
      criteria.andLike("stockName", "%" + bean.getStockName() + "%");
    }
    orderHandler(example, columns, orders);
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
      String stockKey = bean.getCode();
      String stockJson = jedis.hget(STOCKS, stockKey);
      if (StringUtils.isBlank(stockJson)) {
        stockMapper.insert(bean);
        jedis.hset(STOCKS.getBytes(), stockKey.getBytes(), SerializeUtil.serialize(bean));
      } else {
        TbStocks cacheBean = JSON.parseObject(stockJson, TbStocks.class);
        bean.setId(cacheBean.getId());
        stockMapper.updateByPrimaryKey(bean);
      }
      jedis.hset(STOCKS.getBytes(), stockKey.getBytes(), SerializeUtil.serialize(bean));
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
      String stockKey = bean.getCode();
      jedis.hset(STOCKS.getBytes(), stockKey.getBytes(), SerializeUtil.serialize(bean));
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
      String stockKey = code;
      byte[] stockData = jedis.hget(STOCKS.getBytes(), stockKey.getBytes());
      return (TbStocks) SerializeUtil.unserialize(stockData);
    } catch (Exception e) {
      LoggerUtils.error(getClass(), "保存到redis失败", e);
    } finally {
      RedisUtils.closeConn(jedis);
    }
    return null;
  }

}
