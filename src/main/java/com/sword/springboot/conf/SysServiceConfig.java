package com.sword.springboot.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.pagehelper.autoconfigure.MapperAutoConfiguration;
import com.sword.springboot.service.TbStockService;

/**
 * 系统服务配置类
 * 
 * @author sword
 *
 */
@Configuration
@AutoConfigureAfter(MapperAutoConfiguration.class)
public class SysServiceConfig implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private TbStockService stockSvc;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    stockSvc.initRedisCache();
  }

}
