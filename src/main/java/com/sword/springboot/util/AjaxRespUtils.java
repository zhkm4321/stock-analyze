package com.sword.springboot.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sword.springboot.exception.ErrorInfo;

/**
 * ajax请求响应json数据工具类
 * 
 * @author zhenghang E-mail: zhenghang@unitedstone.net
 * @version 创建时间：2016年12月8日 下午11:17:22
 */
public class AjaxRespUtils {

  /**
   * 渲染正常返回数据的json
   * 
   * @param data
   * @return
   */
  public static String renderSuccess() {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("timestamp", new Date().getTime());
    return JSONObject.toJSONString(result);
  }

  /**
   * 渲染正常返回数据的json
   * 
   * @param data
   * @return
   */
  public static String renderSuccess(String message) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", message);
    result.put("timestamp", new Date().getTime());
    return JSONObject.toJSONString(result);
  }

  /**
   * 渲染正常返回数据的json
   * 
   * @param data
   * @param message
   * @return
   */
  public static String renderSuccess(Map<String, Object> data, String message) {
    return renderSuccess(data, message, false, true);
  }

  /**
   * 渲染正常返回数据的json
   * 
   * @param data
   * @param message
   * @param nullToBlank
   * @return
   */
  public static String renderSuccess(Map<String, Object> data, String message, boolean nullToBlank) {
    return renderSuccess(data, message, nullToBlank, true);
  }

  /**
   * 
   * @param data
   *          数据
   * @param message
   *          消息
   * @param nullToBlank
   *          将空值转换为空
   * @param disableCircularReferenceDetect
   *          是否禁用循环引用检测
   * @return
   */
  public static String renderSuccess(Map<String, Object> data, String message, boolean nullToBlank,
      boolean disableCircularReferenceDetect) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", message);
    result.put("data", data);
    result.put("timestamp", new Date().getTime());
    Object[] sfArr = new SerializerFeature[] {};
    if (disableCircularReferenceDetect) {
      sfArr = ArrayUtils.add(sfArr, SerializerFeature.DisableCircularReferenceDetect);
    }
    if (nullToBlank) {
      sfArr = ArrayUtils.addAll(sfArr, new Object[] { SerializerFeature.WriteNullStringAsEmpty,
          SerializerFeature.WriteBigDecimalAsPlain, SerializerFeature.WriteNullNumberAsZero });
      return JSONObject.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", (SerializerFeature[]) sfArr);
    } else {
      return JSONObject.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", (SerializerFeature[]) sfArr);
    }
  }

  /**
   * 渲染异常json
   * 
   * @param message
   * @return
   */
  public static String renderErrors(String message) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", false);
    result.put("message", message);
    result.put("timestamp", new Date().getTime());
    return JSONObject.toJSONString(result);
  }

  /**
   * 渲染异常返回数据的json
   * 
   * @param data
   * @return
   */
  public static String renderErrors(Map<String, Object> data, String message) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", false);
    result.put("message", message);
    result.put("data", data);
    result.put("timestamp", new Date().getTime());
    return JSONObject.toJSONString(result);
  }

  /**
   * 渲染异常返回数据的json
   * 
   * @param data
   * @return
   */
  public static String renderErrors(ErrorInfo<?> data, String message) {
    return JSONObject.toJSONString(data);
  }

}
