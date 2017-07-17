package com.sword.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sword.springboot.mapper.TbStocksMapper;
import com.sword.springboot.model.TbStocks;

@Service
public class TbStockService {

  @Autowired
  private TbStocksMapper stockMapper;

  public List<TbStocks> getAll(TbStocks bean) {
    if (bean.getPage() != null && bean.getRows() != null) {
      PageHelper.startPage(bean.getPage(), bean.getRows());
    }
    return stockMapper.selectAll();
  }

  public TbStocks getById(Integer id) {
    return stockMapper.selectByPrimaryKey(id);
  }

  public void deleteById(Integer id) {
    stockMapper.deleteByPrimaryKey(id);
  }

  public void save(TbStocks bean) {
    if (bean.getId() != null) {
      stockMapper.updateByPrimaryKey(bean);
    } else {
      stockMapper.insert(bean);
    }
  }

}
