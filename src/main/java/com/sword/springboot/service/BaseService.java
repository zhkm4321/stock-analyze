package com.sword.springboot.service;

import com.sword.springboot.util.CameTools;
import com.sword.springboot.util.StringUtils;

import tk.mybatis.mapper.entity.Example;

public class BaseService {

  protected void orderHandler(Example example, String columns, String orders) {
    if (StringUtils.isNotBlank(columns) && StringUtils.isNotBlank(orders)) {
      String[] cols = columns.split(",");
      String[] ods = orders.split(",");
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < ods.length; i++) {
        sb.append(CameTools.convertCame(cols[i]) + " " + ods[i]);
        if (i != ods.length - 1) {
          sb.append(",");
        }
      }
      example.setOrderByClause(sb.toString());
    }
  }
}
