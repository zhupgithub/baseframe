package com.zhupeng.baseframe.utils;

import com.zhupeng.baseframe.common.ErrorCodeType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * //FIXME  这个时间工具类得修改
 */
public class DateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 后去日期凌晨 @return Date
     * @author zhupeng
     * @date 2015年4月16日 @throws
     */
    public static Date getDateStart(Date date) {
        Date dt = DateUtils.setHours(date, 0);
        dt = DateUtils.setMinutes(dt, 0);
        dt = DateUtils.setSeconds(dt, 0);
        dt = DateUtils.setMilliseconds(dt, 0);
        return dt;
    }
    //
    // public static void main(String[] args) {
    // Date dt = DateUtil.getDateStart(new Date());
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // System.out.println(format.format(dt));
    // }

    /**
     * 判断时间是否在某个之间之前
     *
     * @author zhupeng
     */
    public static boolean isBefore(Date date, int h, int m, int s) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int ch = cal.get(Calendar.HOUR_OF_DAY);
        int cm = cal.get(Calendar.MINUTE);
        int cs = cal.get(Calendar.SECOND);
        if (ch > h) {
            return false;
        } else if (cm > m) {
            return false;
        } else if (cs > s) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 判断日期是否在几秒中之前
     * @param date
     * @param s
     * @return
     */
    public static boolean isBefore(Date date,int s){
        long d = date.getTime();
        long nd = System.currentTimeMillis();
        return (nd-d)>(s*1000);
    }

    /**
     * 得到两个日期之间的天数只差
     *
     * @author zhupeng
     */
    public static Integer daysBetween(Date smallDate, Date bigDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (smallDate == null || bigDate == null) {
                return -1;
            }
            smallDate = sdf.parse(sdf.format(smallDate));
            bigDate = sdf.parse(sdf.format(bigDate));
        } catch (ParseException e) {
            LOG.error( "系统异常", e);
            // 出错返回-1,
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(smallDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bigDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Date afterDays(Date now, Integer days) {
        long d = days;
        d = d * 24 * 3600 * 1000;
        return new Date(now.getTime() + d);
    }

    /**
     *
     * <p>
     * 得到传入时间所在的月的最后一天
     * </p>
     *
     * @author zhupeng
     * @param date
     * @return
     * @see
     */
    public static Date monthLastTime(Date date, int beforeMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + beforeMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     *
     * <p>
     * 得到传入时间所在月的第一天
     * </p>
     *
     * @author zhupeng
     * @param date
     * @return
     * @see
     */
    public static Date monthFirstTime(Date date, int beforeMonth) {
        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + beforeMonth, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *
     * <p>
     * 得到传入时间所在天的零点
     * </p>
     *
     * @author   zhupeng
     * @param date
     * @return
     * @see
     */
    public static Date dayFirstTime(Date date, int beforeMonth) {
        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + beforeMonth, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     *
     * <p>
     * 得到传入时间所在的天的最后一小时
     * </p>
     *
     * @author zhupeng
     * @param date
     * @return
     * @see
     */
    public static Date dayLastTime(Date date, int beforeMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     *
     * <p>
     * 得到传入时间后几天时间
     * </p>
     *
     * @author zhupeng
     * @date 2013-12-5 下午9:03:54
     * @return
     * @see
     */
    public static Date beforeDate(Date date, int before) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + before);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *
     * <p>
     * 得到传入时间后几天的开始时间
     * </p>
     *
     * @author ztjie
     * @date 2013-12-5 下午9:03:54
     * @return
     * @see
     */
    public static Date beforeDateStartTime(Date date, int before) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + before, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *
     * <p>
     * 得到传入时间后几天的结束时间
     * </p>
     *
     * @author ztjie
     * @date 2013-12-5 下午9:04:18
     * @return
     * @see
     */
    public static Date beforeDateLastTime(Date date, int before) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + before, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 调整时间为得到传入时间所在的天最后一秒
     *
     * @param date
     * @return
     */
    public static Date oneDateLastTime(Date date) {
        if (date == null) {
            return date;
        }
        return DateUtil.beforeDateLastTime(date, 0);
    }

    /**
     *
     * <p>
     * 得到几年前的当前时间
     * </p>
     *
     * @author Think
     * @date 2013-12-7 上午10:16:13
     * @param date
     * @param before
     * @return
     * @see
     */
    public static Date beforeYearTime(Date date, int before) {
        Calendar cal = Calendar.getInstance();// 得到一个Calendar的实例
        cal.setTime(date); // 设置时间为当前时间
        cal.add(Calendar.YEAR, before); // 年份减1
        return cal.getTime();
    }

    /**
     * @author yindezhou
     * @date 2014-9-23 下午4:59:35
     * @param time
     *            23:00:00格式的时间或者 23:00格式
     */
    public static Time getTimeFmt(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        String[] t = time.split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        if (t.length >= 3) {
            cal.set(Calendar.SECOND, Integer.parseInt(t[2]));
        } else {
            cal.set(Calendar.SECOND, 0);
        }
        return new Time(cal.getTimeInMillis());
    }

    public static String getFormatDate(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(date);
    }

    public static String getFormatDateYYYYMMDD(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        return sf.format(date);
    }
    public static String getFormatDateyyyyMMddHHmmss (Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(date);
    }
    /**
     * 返回 YYYY-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getFormatDateYYYY_MM_DD_HH_MM_SS(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    /**
     * 返回 YYYY-MM
     *
     * @param date
     * @return
     */
    public static String getFormatDateYYYY_MM(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(date);
    }

    /**
     * 获取当前时间，并格式化为  xx号xx时xx
     * @return
     */
    public static String getNowTime(){
        String now = getFormatDateYYYY_MM_DD_HH_MM_SS(new Date());
        return now.substring(8, 10) + "号" + now.substring(11, 13) + "时" + now.substring(14, 16);
    }

    /**
     * 返回时间，格式化为  xx号xx时xx
     */
    public static String getFormatDateMMDDHH(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }
        SimpleDateFormat sf = new SimpleDateFormat("dd号HH时mm");
        return sf.format(date);
    }

    /**
     * 时间字符串转成指定时间格式
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date getDateByStr(String dateStr,String pattern){
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sim.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(ErrorCodeType.TYPE_CONVERSION_ERROR.getErrorCode()+
                    "=="+ErrorCodeType.TYPE_CONVERSION_ERROR.getErrorCode());
        }
        return date;
    }

    /**
     * 将时间转成指定格式的字符串
     * @param date
     * @param pattern
     * @return
     */
    public static  String getStrByDate(Date date,String pattern){
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        String str = null;
        try {
            str = sim.format(date);
        }catch (Exception e){
            throw new RuntimeException(ErrorCodeType.TYPE_CONVERSION_ERROR.getErrorCode()+
                    "=="+ErrorCodeType.TYPE_CONVERSION_ERROR.getErrorCode());
        }
        return str;
    }

    /**
     * 获取两个时间的年份差
     * @param beforeDate 较早的时间
     * @param nowDate 较晚的时间
     * @return
     */
    public static int getDateYearPoor(Date beforeDate, Date nowDate){
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar nowCalender = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        nowCalender.setTime(nowDate);
        int yearPoor = nowCalender.get(Calendar.YEAR) - beforeCalendar.get(Calendar.YEAR);
        return yearPoor;
    }

    /**
     * 获取时间
     * @param time
     * @param fmt
     * @return
     */
    public static Date getDate(String time,String fmt){

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
