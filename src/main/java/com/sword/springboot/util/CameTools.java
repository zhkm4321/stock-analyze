package com.sword.springboot.util;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CameTools {
  public static void main(String[] args) {
    String str = "{'user_name':'o,k','user_sex':0,'object_info':{'business_code':'0001','object_info2':{'object_1':'ok'}}}";
    String str2 = "{'object_info':{'business_code':'ok'}}";
    System.out.println(convertJson(str));
    System.out.println(getSetMethodName("col1"));
    System.out.println(convertStr("is_run"));
    System.out.println(convertCame("isRun desc"));
  }

  public static String getSetMethodName(String propName) {
    propName = "get_" + propName;
    return convertStr(propName);
  }

  public static String getGetMethodName(String propName) {
    propName = "set_" + propName;
    return convertStr(propName);
  }

  public static String convertStr(String str) {
    Pattern linePattern = Pattern.compile("_(\\w)");
    str = str.toLowerCase();
    Matcher matcher = linePattern.matcher(str);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  public static String convertCame(String str) {
    Pattern humpPattern = Pattern.compile("[A-Z]");
    Matcher matcher = humpPattern.matcher(str);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  public final static void convertJson(Object json) {
    if (json instanceof JSONArray) {
      JSONArray arr = (JSONArray) json;
      for (Object obj : arr) {
        convertJson(obj);
      }
    } else if (json instanceof JSONObject) {
      JSONObject jo = (JSONObject) json;
      Set<String> keys = jo.keySet();
      String[] array = keys.toArray(new String[keys.size()]);
      for (String key : array) {
        Object value = jo.get(key);
        String[] key_strs = key.split("_");
        if (key_strs.length > 1) {
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < key_strs.length; i++) {
            String ks = key_strs[i];
            if (!"".equals(ks)) {
              if (i == 0) {
                sb.append(ks);
              } else {
                int c = ks.charAt(0);
                if (c >= 97 && c <= 122) {
                  int v = c - 32;
                  sb.append((char) v);
                  if (ks.length() > 1) {
                    sb.append(ks.substring(1));
                  }
                } else {
                  sb.append(ks);
                }
              }
            }
          }
          jo.remove(key);
          jo.put(sb.toString(), value);
        }
        convertJson(value);
      }
    }
  }

  public final static String convertJson(String json) {
    JSON obj = (JSON) JSON.parse(json);
    convertJson(obj);
    return obj.toJSONString();
  }

}
