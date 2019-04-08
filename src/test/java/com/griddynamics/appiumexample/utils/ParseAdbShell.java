package com.griddynamics.appiumexample.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseAdbShell {

    public static final String generalPattern = "(\\d)+";
    public static final String uiPattern = "[^(\\d.\\d)\\n](\\d)+";



    public static List<Integer> captureValues(String largeText, String pattern) {
        Pattern ptn = Pattern.compile(pattern);
        Matcher mtch = ptn.matcher(largeText);
        List<Integer> values = new ArrayList<>();
        while (mtch.find()) {

            values.add(Integer.valueOf((mtch.group().replaceAll("\\s+", ""))));


        }
        return values;
    }

    public static List<String> captureString(String largeText, String pattern) {
        Pattern ptn = Pattern.compile(pattern);
        Matcher mtch = ptn.matcher(largeText);
        List<String> values = new ArrayList<>();
        while (mtch.find()) {

            values.add((mtch.group().replaceAll("\\s+", "")));


        }
        return values;
    }

}