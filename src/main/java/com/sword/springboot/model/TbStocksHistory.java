package com.sword.springboot.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

@Table(name = "tb_stocks_history")
public class TbStocksHistory extends BaseEntity{
    /**
     * 股票代码
     */
    private String code;

    /**
     * 交易时间
     */
    private Date jysj;

    /**
     * 收盘价
     */
    private BigDecimal spj;

    /**
     * 最高价格
     */
    private BigDecimal zgj;

    /**
     * 最低价格
     */
    private BigDecimal zdj;

    /**
     * 开盘价格
     */
    private BigDecimal kpj;

    /**
     * 前收盘价
     */
    private BigDecimal qsp;

    /**
     * 涨跌额
     */
    private BigDecimal zde;

    /**
     * 涨跌幅
     */
    private BigDecimal zdf;

    /**
     * 换手率
     */
    private BigDecimal hsl;

    /**
     * 成交量
     */
    private BigDecimal cjl;

    /**
     * 成交金额
     */
    private BigDecimal cjje;

    /**
     * 总市值
     */
    private BigDecimal zsz;

    /**
     * 流通市值
     */
    private BigDecimal ltsz;

    /**
     * 成交笔数
     */
    private BigDecimal cjbs;
    
    public TbStocksHistory() {
    }

    public TbStocksHistory(String code, Date jysj, BigDecimal spj, BigDecimal zgj, BigDecimal zdj, BigDecimal kpj,
        BigDecimal qsp, BigDecimal zde, BigDecimal zdf, BigDecimal hsl, BigDecimal cjl, BigDecimal cjje, BigDecimal zsz,
        BigDecimal ltsz, BigDecimal cjbs) {
      this.code = code;
      this.jysj = jysj;
      this.spj = spj;
      this.zgj = zgj;
      this.zdj = zdj;
      this.kpj = kpj;
      this.qsp = qsp;
      this.zde = zde;
      this.zdf = zdf;
      this.hsl = hsl;
      this.cjl = cjl;
      this.cjje = cjje;
      this.zsz = zsz;
      this.ltsz = ltsz;
      this.cjbs = cjbs;
    }

    /**
     * 获取股票代码
     *
     * @return code - 股票代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置股票代码
     *
     * @param code 股票代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取交易时间
     *
     * @return jysj - 交易时间
     */
    public Date getJysj() {
        return jysj;
    }

    /**
     * 设置交易时间
     *
     * @param jysj 交易时间
     */
    public void setJysj(Date jysj) {
        this.jysj = jysj;
    }

    /**
     * 获取收盘价
     *
     * @return spj - 收盘价
     */
    public BigDecimal getSpj() {
        return spj;
    }

    /**
     * 设置收盘价
     *
     * @param spj 收盘价
     */
    public void setSpj(BigDecimal spj) {
        this.spj = spj;
    }

    /**
     * 获取最高价格
     *
     * @return zgj - 最高价格
     */
    public BigDecimal getZgj() {
        return zgj;
    }

    /**
     * 设置最高价格
     *
     * @param zgj 最高价格
     */
    public void setZgj(BigDecimal zgj) {
        this.zgj = zgj;
    }

    /**
     * 获取最低价格
     *
     * @return zdj - 最低价格
     */
    public BigDecimal getZdj() {
        return zdj;
    }

    /**
     * 设置最低价格
     *
     * @param zdj 最低价格
     */
    public void setZdj(BigDecimal zdj) {
        this.zdj = zdj;
    }

    /**
     * 获取开盘价格
     *
     * @return kpj - 开盘价格
     */
    public BigDecimal getKpj() {
        return kpj;
    }

    /**
     * 设置开盘价格
     *
     * @param kpj 开盘价格
     */
    public void setKpj(BigDecimal kpj) {
        this.kpj = kpj;
    }

    /**
     * 获取前收盘价
     *
     * @return qsp - 前收盘价
     */
    public BigDecimal getQsp() {
        return qsp;
    }

    /**
     * 设置前收盘价
     *
     * @param qsp 前收盘价
     */
    public void setQsp(BigDecimal qsp) {
        this.qsp = qsp;
    }

    /**
     * 获取涨跌额
     *
     * @return zde - 涨跌额
     */
    public BigDecimal getZde() {
        return zde;
    }

    /**
     * 设置涨跌额
     *
     * @param zde 涨跌额
     */
    public void setZde(BigDecimal zde) {
        this.zde = zde;
    }

    /**
     * 获取涨跌幅
     *
     * @return zdf - 涨跌幅
     */
    public BigDecimal getZdf() {
        return zdf;
    }

    /**
     * 设置涨跌幅
     *
     * @param zdf 涨跌幅
     */
    public void setZdf(BigDecimal zdf) {
        this.zdf = zdf;
    }

    /**
     * 获取换手率
     *
     * @return hsl - 换手率
     */
    public BigDecimal getHsl() {
        return hsl;
    }

    /**
     * 设置换手率
     *
     * @param hsl 换手率
     */
    public void setHsl(BigDecimal hsl) {
        this.hsl = hsl;
    }

    /**
     * 获取成交量
     *
     * @return cjl - 成交量
     */
    public BigDecimal getCjl() {
        return cjl;
    }

    /**
     * 设置成交量
     *
     * @param cjl 成交量
     */
    public void setCjl(BigDecimal cjl) {
        this.cjl = cjl;
    }

    /**
     * 获取成交金额
     *
     * @return cjje - 成交金额
     */
    public BigDecimal getCjje() {
        return cjje;
    }

    /**
     * 设置成交金额
     *
     * @param cjje 成交金额
     */
    public void setCjje(BigDecimal cjje) {
        this.cjje = cjje;
    }

    /**
     * 获取总市值
     *
     * @return zsz - 总市值
     */
    public BigDecimal getZsz() {
        return zsz;
    }

    /**
     * 设置总市值
     *
     * @param zsz 总市值
     */
    public void setZsz(BigDecimal zsz) {
        this.zsz = zsz;
    }

    /**
     * 获取流通市值
     *
     * @return ltsz - 流通市值
     */
    public BigDecimal getLtsz() {
        return ltsz;
    }

    /**
     * 设置流通市值
     *
     * @param ltsz 流通市值
     */
    public void setLtsz(BigDecimal ltsz) {
        this.ltsz = ltsz;
    }

    /**
     * 获取成交笔数
     *
     * @return cjbs - 成交笔数
     */
    public BigDecimal getCjbs() {
        return cjbs;
    }

    /**
     * 设置成交笔数
     *
     * @param cjbs 成交笔数
     */
    public void setCjbs(BigDecimal cjbs) {
        this.cjbs = cjbs;
    }
}