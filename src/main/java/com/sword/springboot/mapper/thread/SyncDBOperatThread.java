package com.sword.springboot.mapper.thread;

import java.util.Iterator;
import java.util.List;

import com.sword.springboot.util.LoggerUtils;
import com.sword.springboot.util.ReflectionUtil;
import com.sword.springboot.util.SAMapper;

import tk.mybatis.mapper.entity.Example;

/**
 * 异步数据库操作
 * 
 * @author sword
 *
 */
public class SyncDBOperatThread<T> implements Runnable {

  private SAMapper<T> mapper = null;

  private List<T> list = null;

  private Class<T> clazz = null;

  private String[] PK = null;

  public SyncDBOperatThread(SAMapper<T> mapper, List<T> list, String[] pk, Class<T> clazz) {
    this.mapper = mapper;
    this.list = list;
    this.clazz = clazz;
    this.PK = pk;
  }

  @Override
  public void run() {
    int updateCount = 0;
    int insertCount = 0;
    for (Iterator<T> it = list.iterator(); it.hasNext();) {
      T entity = (T) it.next();
      T record = null;
      try {
        record = (T) clazz.newInstance();
        for (int i = 0; i < PK.length; i++) {
          ReflectionUtil.setFieldValue(clazz, record, PK[i], ReflectionUtil.getFieldValue(clazz, entity, PK[i]));
        }
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      T hisInDB = mapper.selectOne(record);
      if (null != hisInDB) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        for (int i = 0; i < PK.length; i++) {
          criteria.andEqualTo(PK[i], ReflectionUtil.getFieldValue(clazz, entity, PK[i]));
        }
        mapper.updateByExample(entity, example);
        updateCount++;
      } else {
        mapper.insert(entity);
        insertCount++;
      }
    }
    LoggerUtils.info(getClass(), "更新"+updateCount+",新增"+insertCount);
  }

}
