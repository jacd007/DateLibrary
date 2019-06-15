package com.zippyttech.datelib.DateUtils;

/**
 * Created by zippyttech on 15/06/19.
 */

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

    public static String dateFormatAll(String formate) {
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    public static String getDay(String date) {
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        Date d = StringToDate(date, "EEEE");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        return formatter.format(d);
    }

    public static String dateFormat(String data, String formate) {
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        Date date = StringToDate(data, formate);
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    public static Integer Epoch(String timestamp,String formate){
        if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formate);
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            return null;
        }
    }

    private static String textDateFromDifferencesDays(String date, String format) throws ParseException{
        String HOUR_SHORT = "HH:mm",FORMAT_SHORT="dd-MM-yy";
        String today = UtilsDate.dateFormatAll(FORMAT_SHORT);
        String auxDate = UtilsDate.changeData(date,format,FORMAT_SHORT);
        int dias = UtilsDate.differencesDate(auxDate,today);

        switch (dias) {
            case 0:
                return "Hoy a las "+UtilsDate.changeData(date,format,HOUR_SHORT);
            case 1:
                return "Ayer a las "+UtilsDate.changeData(date,format,HOUR_SHORT);
            default:
                int semanas = (int) dias/7;
                int difDias = (dias-(7*semanas));
                int meses = (int) semanas/4;
                int difSem = semanas-(4*meses);

                String difSemanas = semanas>1? semanas+"sem ": (semanas==0)?"":semanas+"sem ";
                String difD = difDias>1?difDias+" dias ":(difDias==0?"":difDias+" dia ") ;
                difD = (semanas!=0 && difDias!=0)?" y "+difD:difD;
                String ff = UtilsDate.changeData(date,format,HOUR_SHORT);

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


    public static String DateToString(Date date, String formate){
        String string="";

        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        string = sdf.format(new Date());

        return string;
    }

    public static Date StringToDate(String string, String formate){
        DateFormat format = new SimpleDateFormat(formate, Locale.ENGLISH);
        try {
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateVariable(String fecha, int dias, String formate) {//TODO: Suma y/o Resta a FECHAS
        //tomo string fecha sumo o resto "dias" y devuelvo el string modificado de fecha
        Date date=null;
        DateFormat format = new SimpleDateFormat(formate);
        try {
            date = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        Date dcalenda = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String s = sdf.format(dcalenda);
        return s;
    }

    public static String filterTextView( String Date, String formate, String formate2) {
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
        }
        return resp;
    }
    public static String filterTextViewMonth( String Date, String formate, String formate2) {
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
        }
        return resp;
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

    public static int differencesDate(String dateVisite, String dateCompare) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date fechaInicial= null;
        Date fechaFinal= null;
        try {
            fechaInicial = dateFormat.parse(dateVisite);
            fechaFinal=dateFormat.parse(dateCompare);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

        if(dias<0)return dias *= (-1);
        else return dias;
    }

    public static String changeData(String dateOld,String originalFormat, String newFormat){
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
