package com.zippyttech.datelib.DateUtils;

/**
 * Created by zippyttech on 15/06/19.
 */

import android.os.ParcelFormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author zippyttech
 */
public class UtilsDate {

    private static final String OK_TAG = "UtilsDate";
    private static final String ERROR_TAG = "ERROR";

    public static String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String getDateDay(String Date) throws ParseException {
        String Format = "EEEE";
        DateFormat df = new SimpleDateFormat(Format, Locale.ENGLISH);
        Date d = df.parse(Date);
        SimpleDateFormat formatter = new SimpleDateFormat(Format);
        return formatter.format(d);
    }
    public static String getDateMonth(String Date) throws ParseException {
        String Format = "MMMM";
        DateFormat df = new SimpleDateFormat(Format, Locale.ENGLISH);
        Date d = df.parse(Date);
        SimpleDateFormat formatter = new SimpleDateFormat(Format);
        return formatter.format(d);
    }

    public static String getDateHours(String Date) throws ParseException {
        String Format = "KK:mm:ss a";
        DateFormat df = new SimpleDateFormat(Format, Locale.ENGLISH);
        Date d = df.parse(Date);
        SimpleDateFormat formatter = new SimpleDateFormat(Format);
        return formatter.format(d);
    }

    public static String dateFormat(String data, String Format) throws ParseException{
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        DateFormat df = new SimpleDateFormat(Format, Locale.ENGLISH);
        Date d = df.parse(data);
        SimpleDateFormat formatter = new SimpleDateFormat(Format);
        return formatter.format(d);
    }

    public static Integer Epoch(String timestamp,String formate) {
        if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formate);
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            return 0;
        }
    }

    public static String textFromDiffDate(String OriginDate, String Originformat){
        String HOUR_SHORT = "KK:mm a",FORMAT_SHORT="dd-MM-yy",FORMAT="dd-MM-yyyy HH:mm:ss";

        String TODAY = UtilsDate.dateTodayFormat(FORMAT);
        OriginDate = UtilsDate.refractorFormat(OriginDate,Originformat,FORMAT);

        int dias = UtilsDate.differencesDate(OriginDate,TODAY,FORMAT);


        switch (dias) {
            case 0:
                return "Hoy a las "+UtilsDate.refractorFormat(OriginDate,FORMAT,HOUR_SHORT);
            case 1:
                return "Ayer a las "+UtilsDate.refractorFormat(OriginDate,FORMAT,HOUR_SHORT);
            default:
                int semanas = (int) dias/7;
                int difDias = (dias-(7*semanas));
                int meses = (int) semanas/4;
                int difSem = semanas-(4*meses);

                String difSemanas = semanas>1? semanas+"sem": (semanas==0)?"":semanas+"Sem";
                String difD = difDias>1?difDias+" dias":(difDias==0?"":difDias+"Día") ;
                difD = (semanas!=0 && difDias!=0)?", "+difD:difD;
                String ff = UtilsDate.refractorFormat(OriginDate,FORMAT,HOUR_SHORT);

                return difSemanas+difD + ", "+ff;
        }

    }


    public static String reformateDate( String Date, String formate, String formate2) {
//        dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        SimpleDateFormat parseador = new SimpleDateFormat(formate);
        SimpleDateFormat formateador = new SimpleDateFormat(formate2);

        try {
            date = parseador.parse(Date);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String DateToString(Date date, String Format){
        String string="";

        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        string = sdf.format(new Date());

        return string;
    }

    public static Date StringToDate(String string, String Format){
        DateFormat format = new SimpleDateFormat(Format, Locale.ENGLISH);
        try {
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateVariable(String date, int dias, String Format) {//TODO: Suma y/o Resta a FECHAS
        //tomo string fecha sumo o resto "dias" y devuelvo el string modificado de fecha
        Date d=null;
        DateFormat format = new SimpleDateFormat(Format);
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        Date dcalenda = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        String s = sdf.format(dcalenda);
        return s;
    }

    public static String getTimeZone() {
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
// Got local offset, now loop through available timezone id(s).
        String[] ids = TimeZone.getAvailableIDs();
        String name = null;
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            int hours = tz.getDSTSavings();
            int raw = tz.getRawOffset();
            int s = tz.getOffset(milliDiff);
            if (tz.getRawOffset() == milliDiff) {
                // Found a match.
                name = id;
                break;
            }
        }

        return name;
    }

    public static float getHoursTimeZone() {
        Calendar calendar = Calendar.getInstance();
        long milliDiff = calendar.get(Calendar.ZONE_OFFSET);
        float hours = (milliDiff / 1000) / 3600;
        return hours;
    }

    public static String dateTodayGMTFormat(String value_format) {
        Date date = Calendar.getInstance().getTime();
        String timezone = getTimeZone();
        SimpleDateFormat formatter = new SimpleDateFormat(value_format);
        String today = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        int hours_add = (int) (getHoursTimeZone() * (-1));
        calendar.add(Calendar.HOUR, hours_add);  // numero de horas a añadir, o restar en caso de horas<0
        calendar.add(Calendar.MINUTE, -5); // revisar 5 minutos antes de la hora del server
        Date newDate = calendar.getTime();
        today = formatter.format(newDate);
        return today;
    }

    public static String dateTodayFormat(String valueFormat) {
        Date date = Calendar.getInstance().getTime();
        String timezone = getTimeZone();
        SimpleDateFormat formatter = new SimpleDateFormat(valueFormat);
        String today = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        int hours_add = (int) (getHoursTimeZone() * (-1));
        //calendar.add(Calendar.HOUR, hours_add);  // numero de horas a añadir, o restar en caso de horas<0
        // calendar.add(Calendar.MINUTE,-30); // revisar 5 minutos antes de la hora del server
        Date newDate = calendar.getTime();
        today = formatter.format(newDate);
        return today;
    }

    public static int differencesDate(String dateString, String dateCompare, String Format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Format);

        Date fechaInicial= null;
        Date fechaFinal= null;
        try {
            fechaInicial = dateFormat.parse(dateString);
            fechaFinal=dateFormat.parse(dateCompare);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

        if(dias<0)return dias *= (-1);
        else return dias;
    }

    public static String refractorFormat(String dateOld,String originalFormat, String newFormat){
//     dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        SimpleDateFormat parseador = new SimpleDateFormat(originalFormat);
        SimpleDateFormat formateador = new SimpleDateFormat(newFormat);

        try {
            date = parseador.parse(dateOld);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resp;
    }


}
