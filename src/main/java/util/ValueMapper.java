package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikola on 22.6.2015..
 */
public class ValueMapper {

    public static final String YES = "da";
    public static final String NO = "ne";

    public static String mapBoolean(String value) {
        String ret = null;

        switch (value) {
            case "1":
                ret = YES;
                break;
            case "0":
                ret = NO;
                break;
            case YES:
                ret = "1";
                break;
            case NO:
                ret = "0";
                break;

        }

        return ret;
    }

    private static final String DOTS_DATE_REGEX = "\\d+\\.\\d+\\.\\d+\\.";

    public static String mapDate(String value) {
        String ret = null;

        Date date;
        SimpleDateFormat dotsDf = new SimpleDateFormat("dd.MM.yyyy.");
        SimpleDateFormat dashesDf = new SimpleDateFormat("yyyy-MM-dd");

        String pattern = DOTS_DATE_REGEX;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);

        try {
            if (m.find()) {
                date = dotsDf.parse(value);
                ret = dashesDf.format(date);
            } else {
                date = dashesDf.parse(value);
                ret = dotsDf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private ValueMapper() {
    }
}
