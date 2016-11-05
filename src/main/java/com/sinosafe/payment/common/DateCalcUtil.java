package com.sinosafe.payment.common;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalcUtil {
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat NARROW_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat NO_SIGNAL_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static String SHORT_YEAR = "^[1-9]{1}[0-9]{1}$";
    private final static String SHORT_MONTH = "^(0)[1-9]$|^(1)[0-2]$";

    public static Date RemoveTimePart(Date date) throws Exception {
        return SIMPLE_DATE_FORMAT.parse(SIMPLE_DATE_FORMAT.format(date));
    }

    public static Date parseDateTime(String inputDate) throws Exception {
        return DATE_FORMAT.parse(inputDate);
    }

    public static Date parseDate(String inputDate) throws Exception {
        return SIMPLE_DATE_FORMAT.parse(inputDate);
    }
    public static Date narrowParseDate(String inputDate) throws Exception {
        return NARROW_DATE_FORMAT.parse(inputDate);
    }
    public static Date noSignalDateFormat(String source) throws ParseException {
        return NO_SIGNAL_DATE_FORMAT.parse(source);
    }
    public static String formatDate(Date date) throws Exception {
        return SIMPLE_DATE_FORMAT.format(date);
    }
    public static String narrowFormatDate(Date date) throws Exception {
        return SIMPLE_DATE_FORMAT.format(date);
    }


    public static String formatDate(Date date, DateFormat format) {
        if (format == null) return null;
        return format.format(date);
    }

    public static String formatDatetime(Date date) {
        return formatDate(date, DATE_FORMAT);
    }

    public static Timestamp parseTimestamp(String inputDate) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(inputDate));
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new Integer(calendar.get(Calendar.DAY_OF_WEEK)).toString();
    }

    public static Integer getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new Integer(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static String getHourAndMinuteOfDay(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(date);
        return time;
    }

    public static Date getNearDay(Date date, int offset) {
        if (offset == 0)
            return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int target = offset;
        int step = 1;
        if (target < 0) {
            target *= -1;
            step = -1;
        }
        while (target > 0) {
            calendar.add(Calendar.DAY_OF_YEAR, step);
            target--;
        }
        return calendar.getTime();
    }

    public static Date getNearDateTime(Date date, int offset, int type) {
        if (offset == 0)
            return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int target = offset;
        int step = 1;
        if (target < 0) {
            target *= -1;
            step = -1;
        }
        while (target > 0) {
            calendar.add(type, step);
            target--;
        }
        return calendar.getTime();
    }

    public static int GetDateDifferenceByType(Date date1, Date date2, int CalendarType) {
        Date earlier;
        Date later;
        Boolean switched = false;
        Calendar calendarEarlier = Calendar.getInstance();
        Calendar calendarLater = Calendar.getInstance();
        if (date1.equals(date2)) {
            return 0;
        }
        if (date1.after(date2)) {
            earlier = date2;
            later = date1;
            switched = true;
        } else {
            earlier = date1;
            later = date2;
            switched = false;
        }
        calendarEarlier.setTime(earlier);
        calendarLater.setTime(later);
        int count = 0;
        while (calendarEarlier.before(calendarLater)) {
            calendarEarlier.add(CalendarType, 1);
            count++;
        }
        if (switched == true) {
            count *= -1;
        }
        return count;
    }

    public static Date GetNextHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

    public static Date GetEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getDatePart(Date date) throws ParseException {
        String dateStr = SIMPLE_DATE_FORMAT.format(date);
        return SIMPLE_DATE_FORMAT.parse(dateStr);
    }

    public static Date getBeginDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getEndDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Long compareDates(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Long dateDiff;
        try {
            dateDiff = df.parse(df.format(date1)).getTime() - df.parse(df.format(date2)).getTime();
            return dateDiff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getAge(String birthDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = myFormatter.parse(birthDate);
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birth);
        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH);
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);
        Integer age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else if (monthNow < monthBirth) {
                age--;
            }
        }
        return age;
    }

    public static  boolean  isValidShortYear(String year){
        if(!StringUtils.isNumeric(year)||year.length()!=2||Integer.valueOf(year)<getShortYear()){
            return false;
        }
        return year.matches(SHORT_YEAR);
    }
    public static  boolean  isValidShortMonth(String month){
        if(StringUtils.isBlank(month)||month.length()!=2){
            return false;
        }
        return month.matches(SHORT_MONTH);
    }
    public static int getShortYear(){
       return  Integer.valueOf(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2));
    }

    public static void main(String[] args) {
        if(!isValidShortMonth("00")){
            System.out.println("00not month");
        }
        if(!isValidShortMonth("09")){
            System.out.println("09not month");
        }
        if(!isValidShortMonth("12")){
            System.out.println("12not month");
        }
        if(!isValidShortMonth("22")){
            System.out.println("22not month");
        }
        if(!isValidShortMonth("13")){
            System.out.println("13not month");
        }
        if(!isValidShortMonth("133")){
            System.out.println("133not month");
        }
        if(!isValidShortMonth("-00")){
            System.out.println("-00not month");
        }
        if(!isValidShortYear("15")){
            System.out.println("15not year");
        }
        if(!isValidShortYear("16")){
            System.out.println("16not year");
        }
        if(!isValidShortYear("99")){
            System.out.println("99not year");
        }
        if(!isValidShortYear("999")){
            System.out.println("999not year");
        }
        if(!isValidShortYear("38")){
            System.out.println("38not year");
        }
        if(!isValidShortYear("x2")){
            System.out.println("x2not year");
        }
        System.out.println("我是谁拿".length());
    }




}