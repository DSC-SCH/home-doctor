package homedoctor.medicine.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeHandler {

    public static String cutTime(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd");

        return df.format(date);
    }


    public static List<String> getDateTermsToStringList(Date startDate, Date endDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd");
        List<String> result = new ArrayList<>();
        // 두 날짜 사이의 차이.
        long calDate = endDate.getTime() - startDate.getTime();
        long calDateDays = calDate / (24*60*60*1000);
        calDateDays = Math.abs(calDateDays);
        calendar.setTime(startDate);
        System.out.println("calDateDays : " + calDateDays);

        if (calDate == 0) {
            result.add(cutTime(startDate));
            return result;
        }

        result.add(cutTime(calendar.getTime()));
        for (int i = 0; i < calDateDays; i++) {
            calendar.add(Calendar.DATE, 1);
            result.add(cutTime(calendar.getTime()));
        }

        return result;
    }

    public static void main(String[] args) throws ParseException {
        String date1 = "2020:03:01";
        String date2 = "2020:03:10";
        DateFormat format = new SimpleDateFormat("yyyy:mm:dd");
        Date start = format.parse(date1);
        Date end = format.parse(date2);
        System.out.println("start : " + start);
        System.out.println("end : " + end);
        List<String> dateTerm = getDateTermsToStringList(start, end);
        System.out.println("dateTerms Len : " + dateTerm.size());
        for (String s : dateTerm) {
            System.out.println(s);
        }
    }
}
