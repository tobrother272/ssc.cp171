/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.parser.ParseException;

/**
 *
 * @author PC
 */
public class StringUtils {

    private static String removeEmoji(String input) {
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        Pattern pattern = Pattern.compile(
                regex,
                Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll("");
        return result;
    }

    public static String removeEmojiList(String input) {
        String result = "";
        List<String> arr = Arrays.asList(input.split(" "));
        for (String string : arr) {
            result = result + removeEmoji(string) + " ";
        }
        return result;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRandomPass(int numberPass) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#";
        return RandomStringUtils.random(numberPass, characters);
    }

    public static String getDateTimeStringByFormat(String format_string) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_string);
        sdf.setTimeZone(TimeZone.getDefault());
        Date dt = new Date();
        return sdf.format(dt);
    }

    public static String convertLongToDataTime(String format_string, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_string);
        sdf.setTimeZone(TimeZone.getDefault());
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static List<String> getListStringBySplit(String inString, String charSplit) {
        List<String> results = new ArrayList<>();
        if (inString.length() == 0) {
            return results;
        }
        if (charSplit.length() == 0) {
            results.add(inString);
            return results;
        }
        String arr[] = inString.split(charSplit);
        for (String string : arr) {
            if (string.trim().length() != 0) {
                results.add(string);
            }
        }
        return results;
    }

    public static long getLongFromDateString(String dateFormat, String strValue) {
        if (strValue.contains("thg")) {

            try {
                String newDate = strValue.replaceAll(" thg ", "/").replaceAll(" ", "/");
                SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
                Date date = formatter.parse(newDate);
                long dateInLong = date.getTime();
                return dateInLong;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            try {
                Date date = formatter.parse(strValue);
                long dateInLong = date.getTime();
                return dateInLong;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

    }
}
