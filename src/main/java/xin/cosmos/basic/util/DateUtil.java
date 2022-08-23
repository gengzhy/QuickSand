package xin.cosmos.basic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期时间处理工具
 */
public class DateUtil {
    /**
     * ThreadLocal 提供一种 lombda 构造方式
     * 返回此线程局部变量的当前线程的“初始值”。线程第一次使用 get() 方法访问变量时将调用此方法，
     * 但如果线程之前调用了 set(T) 方法，则不会对该线程再调用 initialValue 方法。通常，此方法
     * 对每个线程最多调用一次，但如果在调用 get() 后又调用了 remove()，则可能再次调用此方法。
     */
    private static final ThreadLocal<DateFormat> FORMAT_YMD = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final ThreadLocal<DateFormat> FORMAT_YMD_HMS = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<DateFormat> FORMAT_HMS = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    /**
     *
     * @param date - 输入参数（格式：yyyy-MM-dd）
     * @return
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException {
        return FORMAT_YMD.get().parse(date);
    }

    public static String format(Date date){
        return FORMAT_YMD_HMS.get().format(date);
    }

    public static String formatDate(Date date){
        return FORMAT_YMD.get().format(date);
    }

    public static String formatTime(Date date){
        return FORMAT_HMS.get().format(date);
    }

    /**
     * LocalDate -> Date
     * @param localDate
     * @return
     */
    public Date fromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date -> LocalDate
     * @param date
     * @return
     */
    public LocalDate fromDate(Date date) {
        return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDateTime -> Date
     * @param localDateTime
     * @return
     */
    public Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date -> LocalDateTime
     * @param date
     * @return
     */
    public LocalDateTime fromDateTime(Date date) {
        return LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
    }

    public static void main(String[] args) {
        System.out.println("formatDateTime(new Date()) = " + formatDate(new Date()));
        System.out.println("formatDateTime(new Date()) = " + format(new Date()));
        System.out.println("formatDateTime(new Date()) = " + formatTime(new Date()));
    }
}
