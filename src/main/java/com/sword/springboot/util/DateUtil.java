package com.sword.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

public abstract class DateUtil {

  public static final String YYYY_MM_DD_HH_MM_SS_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

  public static final String YYYYMMDD_FORMAT = "yyyyMMdd";

  public static final SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);

  public static final SimpleDateFormat sdf2 = new SimpleDateFormat(YYYYMMDD_FORMAT);

  public static Date time() {
    return Calendar.getInstance().getTime();
  }

  public static String getDateStr(Date date, String dateFormat) throws IllegalArgumentException {
    if(null==date){
      date = Calendar.getInstance().getTime();
    }
    if (StringUtils.isNotBlank(dateFormat) && dateFormat.equals(YYYYMMDD_FORMAT)) {
      return sdf2.format(date);
    } else if (StringUtils.isNotBlank(dateFormat) && dateFormat.equals(YYYYMMDD_FORMAT)) {
      return sdf.format(date);
    } else {
      try {
        SimpleDateFormat _sdf = new SimpleDateFormat(dateFormat);
        return _sdf.format(date);
      } catch (Exception e) {
        throw new IllegalArgumentException("日期格式不支持", e);
      }
    }
  }
  
  public static String getDateStr(String dateFormat) throws IllegalArgumentException {
    return getDateStr(null, dateFormat);
  }
  
  /**
   * 获取上个月的时间段
   */
  public static Date[] getLastWeekDay() {
    Date startDate = new Date();
    Date endDate = new Date();
    Calendar startCal = new GregorianCalendar();
    // 设置到本月的1号
    startCal.setTime(startDate);
    startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    // 起始时间设置到七天前
    startDate = DateUtils.addDays(startCal.getTime(), -7);
    startDate = DateUtils.truncate(startDate, Calendar.HOUR_OF_DAY);
    // 结束时间加6天
    endDate = DateUtils.addDays(startDate, 6);
    endDate = DateUtils.truncate(endDate, Calendar.HOUR_OF_DAY);
    return new Date[] { startDate, endDate };
  }

  public static Date[] getLastMonthDay() {
    Date startDate = new Date();
    Date endDate = new Date();
    Calendar startCal = new GregorianCalendar();
    // 设置到本月的1号
    startCal.setTime(startDate);
    startCal.set(Calendar.DAY_OF_MONTH, 1);
    startCal = DateUtils.truncate(startCal, Calendar.HOUR_OF_DAY);
    startDate = startCal.getTime();
    // 起始时间设置到七天前
    endDate = DateUtils.addMonths(startCal.getTime(), 1);
    endDate = DateUtils.addDays(endDate, -1);
    endDate = DateUtils.truncate(endDate, Calendar.HOUR_OF_DAY);
    return new Date[] { startDate, endDate };
  }

  /**
   * 获取上一年的时间段
   */
  public static Date[] getLastYearRange() {

    Date startDate = new Date();
    Date endDate = new Date();
    startDate = DateUtils.addYears(startDate, -1);
    startDate = DateUtils.truncate(startDate, Calendar.YEAR);
    endDate = DateUtils.addYears(startDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);
    return new Date[] { startDate, endDate };
  }

  public static Date[] getTimeRange(String startDateStr, String endDateStr) {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM");
    Date startDate = null;
    Date endDate = null;
    try {
      startDate = sdf1.parse(startDateStr);
      endDate = sdf1.parse(endDateStr);
    } catch (ParseException e) {}
    if (null == startDate) {
      try {
        startDate = sdf2.parse(startDateStr);
        endDate = sdf2.parse(endDateStr);
      } catch (ParseException e) {}
    }
    if (null == startDate) {
      try {
        startDate = sdf3.parse(startDateStr);
        endDate = sdf3.parse(endDateStr);
      } catch (ParseException e) {}
    }
    if (null == startDate) {
      try {
        startDate = sdf4.parse(startDateStr);
        endDate = sdf4.parse(endDateStr);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
    endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
    endDate = DateUtils.addDays(endDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);
    return new Date[] { startDate, endDate };
  }

  public static Date[] getMonthRange(String startDateStr, String endDateStr) {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
    Date startDate = null;
    Date endDate = null;
    if (null == startDate) {
      try {
        startDate = sdf2.parse(startDateStr);
        endDate = sdf2.parse(endDateStr);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    if (null == startDate) {
      try {
        startDate = sdf1.parse(startDateStr);
        endDate = sdf1.parse(endDateStr);
      } catch (ParseException e) {}
    }

    startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
    endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
    endDate = DateUtils.addMonths(endDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);
    return new Date[] { startDate, endDate };
  }

  public static List<Date[]> getMonthRangeList(String startDateStr, String endDateStr) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    Date startDate = sdf.parse(startDateStr);
    Date endDate = sdf.parse(endDateStr);
    List<Date[]> dateList = getMonthRangeList(startDate, endDate);
    return dateList;
  }

  public static List<Date[]> getMonthRangeList(Date startDate, Date endDate) throws ParseException {
    List<Date[]> dateList = new ArrayList<Date[]>();
    Calendar start = new GregorianCalendar();
    start.setTime(startDate);
    Calendar end = new GregorianCalendar();
    end.setTime(endDate);
    if (null != startDate && null != endDate) {
      startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
      endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
      endDate = DateUtils.addMonths(endDate, 1);
      Date tmpS = startDate;
      Date tmpE = DateUtils.addMonths(startDate, 1);
      tmpE = DateUtils.addMilliseconds(tmpE, -1);
      do {
        dateList.add(new Date[] { tmpS, tmpE });
        tmpS = DateUtils.addMilliseconds(tmpE, 1);
        tmpE = DateUtils.addMonths(tmpS, 1);
        tmpE = DateUtils.addMilliseconds(tmpE, -1);
      } while (tmpE.before(endDate));
    }
    return dateList;
  }

  /**
   * 获取两个月份的差值
   * 
   * @param start
   * @param end
   * @return
   */
  public static int getMonthDValue(Date start, Date end) {
    if (end.before(start)) {
      throw new IllegalArgumentException("结束时间不能大于起始时间");
    }
    Calendar startCal = new GregorianCalendar();
    startCal.setTime(start);
    Calendar endCal = new GregorianCalendar();
    endCal.setTime(end);
    int yearStart = startCal.get(Calendar.YEAR);
    int yearEnd = endCal.get(Calendar.YEAR);
    int dValue = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);
    if (yearEnd - yearStart > 0) {
      dValue = dValue + 12 * (yearEnd - yearStart);
    }
    return dValue;
  }

  // public static void main(String[] args) throws ParseException {
  // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  // System.out.println(getInterval(sdf.parse("2017-05-18 10:00:00")));
  // }

  /**
   * 以几秒前、几分钟前、几个小时前、几天前的样式显示日期，超出一定的天数才完整显示日期
   * 
   * @param date
   * @return
   */
  public static String getDateGap(Date date, Integer fullFormatDay) { // 传入的时间格式必须类似于2012-8-21
    long currentSeconds = System.currentTimeMillis();
    long timeGap = (currentSeconds - date.getTime()) / 1000;// 与现在时间相差秒数
    String timeStr = null;
    if (null == fullFormatDay) {
      fullFormatDay = 5;
    }
    if (timeGap > fullFormatDay * 24 * 60 * 60) {// 7天以上
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
      timeStr = sdf.format(date);
    } else if (timeGap > 24 * 60 * 60) {// 1天以上
      timeStr = timeGap / (24 * 60 * 60) + "天前";
    } else if (timeGap > 60 * 60) {// 1小时-24小时
      timeStr = timeGap / (60 * 60) + "小时前";
    } else if (timeGap > 60) {// 1分钟-59分钟
      timeStr = timeGap / 60 + "分钟前";
    } else {// 1秒钟-59秒钟
      timeStr = "刚刚";
    }
    return timeStr;
  }
}
