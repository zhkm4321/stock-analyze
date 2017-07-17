package com.sword.springboot.exception;

import java.util.Date;

public class ErrorInfo<T> {

  public static final Integer OK = 0;
  public static final Integer ERROR = 100;

  private Integer code;
  private Boolean success;
  private String message;
  private String url;
  private T data;
  private long timestamp = new Date().getTime();

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

}
