package org.example.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class DatesAndStrings {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    public static Optional<Date> stringToTime (String dateString) {
        try {
            return Optional.of(simpleDateFormat.parse(dateString));
        } catch (ParseException parseException) {
            System.out.println(parseException);
            return Optional.empty();
        }
    }

    public static String fromatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        return simpleDateFormat.format(date);
    }

    /***
     *
     * @param startDate
     * @param endDate
     * @return true if startDate < endDate
     * @throws ParseException
     */
    public static boolean compareDatesString(String startDate, String endDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        return simpleDateFormat.parse(startDate).before(simpleDateFormat.parse(endDate));
    }
}