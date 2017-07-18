package com.sword.springboot.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description:redis工具类
 * @ClassName:
 * @date 2016年10月31日 上午11:25:06
 */
@Component
@ConfigurationProperties(prefix = "redis.pool")
public class RedisUtils {
  @Value("${redis.pool.ip}")
  private String ip; // ip
  @Value("${redis.pool.port}")
  private Integer port; // 端口
  @Value("${redis.pool.password}")
  private String password; // 密码(原始默认是没有密码)
  private int MAX_ACTIVE = 1024; // 最大连接数
  private int MAX_IDLE = 200; // 设置最大空闲数
  private int MAX_WAIT = 10000; // 最大连接时间
  private int timeout; // 超时时间
  private boolean BORROW = true; // 在borrow一个事例时是否提前进行validate操作
  private static JedisPool pool = null;

  /**
   * 初始化线程池
   */
  @PostConstruct
  public void init() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(MAX_ACTIVE);
    config.setMaxIdle(MAX_IDLE);
    config.setMaxWaitMillis(MAX_WAIT);
    config.setTestOnBorrow(BORROW);
    if (StringUtils.isNotBlank(password)) {
      pool = new JedisPool(config, ip, port, timeout, password);
    } else {
      pool = new JedisPool(config, ip, port, timeout);
    }
  }

  /**
   * 获取连接
   */
  public synchronized static Jedis getJedis() {
    try {
      if (pool != null) {
        return pool.getResource();
      } else {
        return null;
      }
    } catch (Exception e) {
      LoggerUtils.info(RedisUtils.class, "连接池连接异常");
      return null;
    }

  }

  /**
   * @Description:设置失效时间
   * @param @param
   *          key
   * @param @param
   *          seconds
   * @param @return
   * @return boolean 返回类型
   */
  public void disableTime(String key, int seconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.expire(key, seconds);

    } catch (Exception e) {
      LoggerUtils.debug(this.getClass(), "设置失效失败.");
    } finally {
      closeConn(jedis);
    }
  }

  /**
   * @Description:插入对象
   * @param @param
   *          key
   * @param @param
   *          obj
   * @param @return
   * @return boolean 返回类型
   */
  public boolean addObject(String key, Object obj) {

    Jedis jedis = null;
    String value = JSONObject.toJSONString(obj);
    try {
      jedis = getJedis();
      String code = jedis.set(key, value);
      if (code.equals("ok")) {
        return true;
      }
    } catch (Exception e) {
      LoggerUtils.debug(this.getClass(), "插入数据有异常.");
      return false;
    } finally {
      closeConn(jedis);
    }
    return false;
  }

  /**
   * @Description:存储key~value
   * @param @param
   *          key
   * @param @param
   *          value
   * @return void 返回类型
   */

  public static boolean addValue(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String code = jedis.set(key, value);
      if (code.equals("ok")) {
        return true;
      }
    } catch (Exception e) {
      LoggerUtils.debug(RedisUtils.class, "插入数据有异常.");
      return false;
    } finally {
      closeConn(jedis);
    }
    return false;
  }

  public String getValue(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String value = jedis.get(key);
      return value;
    } catch (Exception e) {
      LoggerUtils.debug(this.getClass(), "获取数据有异常.");
    } finally {
      closeConn(jedis);
    }
    return null;
  }

  /**
   * @Description:删除key
   * @param @param
   *          key
   * @param @return
   * @return boolean 返回类型
   */
  public boolean delKey(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long code = jedis.del(key);
      if (code > 1) {
        return true;
      }
    } catch (Exception e) {
      LoggerUtils.debug(this.getClass(), "删除key异常.");
      return false;
    } finally {
      closeConn(jedis);
    }
    return false;
  }

  /**
   * @Description: 关闭连接
   * @param @param
   *          jedis
   * @return void 返回类型
   */

  public static void closeConn(Jedis jedis) {
    if (jedis != null) {
      jedis.close();
    }
  }

}