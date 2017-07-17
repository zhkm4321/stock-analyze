package com.sword.springboot.util;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

public class StringUtils extends org.apache.commons.lang.StringUtils {

  public static boolean isNullOrEmpty(String string) {

    if (null == string) {
      return true;
    }
    else if (string.isEmpty()) {
      return true;
    }
    else {
      return false;
    }

  }

  public static boolean equal(String s1, String s2) {
    return s1.equals(s2);
  }

  // 首字母转小写
  public static String toLowerCaseFirstOne(String s) {
    if (Character.isLowerCase(s.charAt(0)))
      return s;
    else
      return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
  }

  // 首字母转大写
  public static String toUpperCaseFirstOne(String s) {
    if (Character.isUpperCase(s.charAt(0)))
      return s;
    else
      return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
  }

  /**
   * 剪切文本。如果进行了剪切，则在文本后加上"..."
   * 
   * @param s 剪切对象。
   * @param len 编码小于256的作为一个字符，大于256的作为两个字符。
   * @return
   */
  public static String textCut(String s, int len, String append) {
    if (s == null) {
      return null;
    }
    int slen = s.length();
    if (slen <= len) {
      return s;
    }
    // 最大计数（如果全是英文）
    int maxCount = len * 2;
    int count = 0;
    int i = 0;
    for (; count < maxCount && i < slen; i++) {
      if (s.codePointAt(i) < 256) {
        count++;
      }
      else {
        count += 2;
      }
    }
    if (i < slen) {
      if (count > maxCount) {
        i--;
      }
      if (!StringUtils.isEmpty(append)) {
        if (s.codePointAt(i - 1) < 256) {
          i -= 2;
        }
        else {
          i--;
        }
        return s.substring(0, i) + append;
      }
      else {
        return s.substring(0, i);
      }
    }
    else {
      return s;
    }
  }

  public static String htmlCut(String s, int len, String append) {
    String text = html2Text(s, len * 2);
    return textCut(text, len, append);
  }

  public static String html2Text(String html, int len) {
    try {
      Lexer lexer = new Lexer(html);
      Node node;
      StringBuilder sb = new StringBuilder(html.length());
      while ((node = lexer.nextNode()) != null) {
        if (node instanceof TextNode) {
          sb.append(node.toHtml());
        }
        if (sb.length() > len) {
          break;
        }
      }
      return sb.toString();
    }
    catch (ParserException e) {
      throw new RuntimeException(e);
    }
  }

}
