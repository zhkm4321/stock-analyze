package com.sword.springboot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtils {
  /**
   * 匹配小括号中的内容
   * 
   * @param content
   * @return
   */
  public static String getParenthesesContent(String content) {
    String regex = "(?<=\\().*(?=\\))";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(content);
    String matherText = null;
    while (m.find()) {
      matherText = m.group();
      break;
    }
    return matherText;
  }
}
