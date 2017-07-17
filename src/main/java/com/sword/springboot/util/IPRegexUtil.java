package com.sword.springboot.util;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * IP地址匹配工具类
 * 
 * @author zhenghang E-mail: zhenghang@unitedstone.net
 * @version 创建时间：2017年3月28日 下午4:28:35
 */
public class IPRegexUtil {

  private static NumberFormat nf = NumberFormat.getInstance();
  static {
    nf.setGroupingUsed(false);
  }

  /**
   * 匹配IP地址是否在子网掩码范围内
   * 
   * @param ipStr
   * @param ipMask
   * @return
   * @throws IllegalArgumentException
   */
  public static boolean validateIpByMask(String ipStr, String ipMask)
      throws IllegalArgumentException {
    ipStr = StringUtils.trim(ipStr);
    ipMask = StringUtils.trim(ipMask);
    if (!isIp(ipStr)) {
      throw new IllegalArgumentException("这不是一个IP地址");
    }
    if (ipMask.indexOf("/") > 0) {
      String[] ipPart = ipMask.split("/");
      int ipMaskInt = Integer.parseInt(StringUtils.trim(ipPart[1]));
      long minIpLong = ipToLong(StringUtils.trim(ipPart[0]));
      long maxIpLong = (long) (ipToLong(StringUtils.trim(ipPart[0]))
          + Math.pow(2, 32 - ipMaskInt) - 1L);
      long iplong = ipToLong(ipStr);
      if (minIpLong < iplong && maxIpLong > iplong) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      if (ipStr.equals(ipMask)) {
        return true;
      }
      else {
        return false;
      }
    }
  }

  public static boolean validateIpByIpRange(String ipStr, String ipRange) {
    ipStr = StringUtils.trim(ipStr);
    ipRange = StringUtils.trim(ipRange);
    if (StringUtils.contains(ipRange, '-')) {
      String[] ipPart = StringUtils.split(ipRange, '-');
      long start = ipToLong(StringUtils.trim(ipPart[0]));
      long end = ipToLong(StringUtils.trim(ipPart[1]));
      long iplong = ipToLong(StringUtils.trim(ipStr));
      if (start <= iplong && end >= iplong) {
        return true;
      }
    }
    return false;
  }

  private static Long ipToLong(String ipStr) {
    String[] ipClasses = ipStr.split("\\.");
    long longIp = 0;
    for (int i = 0; i < ipClasses.length; i++) {
      longIp += (Long.parseLong(ipClasses[i]) << (8 * (3 - i)));
    }
    return longIp;
  }

  public static boolean isIp(String ipStr) {
    String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(ipStr).matches();
  }

  @Deprecated
  public static void main(String[] args) {
    boolean test1 = validateIpByMask("10.10.88.0", "10.10.87.200/26");
    System.out.println(test1);
    boolean test2 = validateIpByIpRange("117.136.16.12", "117.136.16.0-117.136.16.12");
    System.out.println(test2);
  }

  public static Long handlerIpNum(String ipStr) {
    if (StringUtils.isBlank(ipStr)) {
      return null;
    }
    String[] ips = ipStr.split(",");
    String[] segment = com.sword.springboot.util.StringUtils.trim(ips[0]).split("\\.");
    long cur = 0;
    long ipnum = 0;
    for (int i = segment.length - 1; i >= 0; i--) {
      cur = Long.parseLong(segment[segment.length - i - 1]);
      ipnum += (cur * (long) (Math.pow(256, i)));
    }
    return ipnum;
  }
}
