package com.griddynamics.appiumexample.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseAdbShell {


    private static final Logger LOG = Logger.getLogger(ParseAdbShell.class.getName());

    public static final String memPattern = "(\\d)+";
    public static final String uiPattern = "[^(\\d.\\d)\\n](\\d)+";
    public static final String DATEPATTERN = "\\d{4}-\\d{2}-\\d{2}.{13}";

    public static List<Integer> captureValues(String largeText, String pattern) {
        Pattern ptn = Pattern.compile(pattern);
        Matcher mtch = ptn.matcher(largeText);
        List<Integer> values = new ArrayList<Integer>();
        while (mtch.find()) {
            values.add(Integer.valueOf(mtch.group().replaceAll("\\s+", "")));
        }
        return values;
    }

    public static List<Date> captureDates(String date, String pattern) {

        Pattern ptn = Pattern.compile(pattern);
        Matcher mtch = ptn.matcher(date);
        List<String> strDates = new ArrayList<>();
        while (mtch.find()) {
            strDates.add(mtch.group());
        }
        return convertToDate(strDates);
    }

    private static List<Date> convertToDate(List<String> listOfDates) {
        List<Date> dates = new ArrayList<>();
        if (listOfDates.size() < 2 || listOfDates == null) {
            LOG.info("Collection of CPU usage info failed. There is no time info when usage was collcted");
            return dates;
        }
        for (String stDate : listOfDates) {

            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                dates.add(format.parse(stDate));
//                System.out.println();
//                System.out.println("*****************************");
                Date date = format.parse(stDate);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
      return dates;
    }
}