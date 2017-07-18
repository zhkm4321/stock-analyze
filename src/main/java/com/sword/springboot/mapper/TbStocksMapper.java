package com.sword.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sword.springboot.model.TbStocks;
import com.sword.springboot.util.SAMapper;

public interface TbStocksMapper extends SAMapper<TbStocks> {

  @Select("SELECT id,code,stock_name,stock_exchange FROM tb_stocks order by code")
  List<TbStocks> selectAll();
}