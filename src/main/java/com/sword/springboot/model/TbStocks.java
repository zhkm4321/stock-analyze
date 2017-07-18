package com.sword.springboot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stocks")
public class TbStocks extends BaseEntity {
  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "stock_exchange")
  private String stockExchange;

  @Column(name = "stock_name")
  private String stockName;

  /**
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return stock_exchange
   */
  public String getStockExchange() {
    return stockExchange;
  }

  /**
   * @param stockExchange
   */
  public void setStockExchange(String stockExchange) {
    this.stockExchange = stockExchange;
  }

  /**
   * @return stock_name
   */
  public String getStockName() {
    return stockName;
  }

  /**
   * @param stockName
   */
  public void setStockName(String stockName) {
    this.stockName = stockName;
  }
}