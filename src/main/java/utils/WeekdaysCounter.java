package utils;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeekdaysCounter {

    private static Logger log = Logger.getLogger(WeekdaysCounter.class.getName());

    /**
     * Returns number of weekdays between startDate and endDate
     *
     * @param startDate
     * @param endDate
     */
    public static int countWeekdays(LocalDate startDate, LocalDate endDate) {

        final int allDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        LocalDate currentDate = startDate;
        int weekDays = 0;
        int sideWeeksDays = 0;
        int sideWeeksWeekends = 0;
        if (allDays > 25) {
            // если дней много, то вычисляются 2 крайние, неполные недели перебором. Оставшиеся целые арифметически
            while (currentDate.getDayOfWeek().getValue() != 1) {
                sideWeeksDays++;
                if (currentDate.getDayOfWeek().getValue() == 7 || currentDate.getDayOfWeek().getValue() == 6) {
                    sideWeeksWeekends++;
                }
                currentDate = currentDate.plusDays(1);
            }

            currentDate = endDate;
            while (currentDate.getDayOfWeek().getValue() != 7) {
                sideWeeksDays++;
                if (currentDate.getDayOfWeek().getValue() == 7 || currentDate.getDayOfWeek().getValue() == 6) {
                    sideWeeksWeekends++;
                }
                currentDate = currentDate.minusDays(1);
            }
            int fullWeeksWeekends = 2 * (allDays - sideWeeksDays) / 7;
            weekDays = allDays - sideWeeksWeekends - fullWeeksWeekends;

        } else {
            //если дней мало, то все дни вычисляется перебором
            while (!currentDate.isAfter(endDate)) {
                if (currentDate.getDayOfWeek().getValue() != 6 && currentDate.getDayOfWeek().getValue() != 7) {
                    weekDays++;
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        log.log(Level.FINEST, "weekDays have been counted successfully");
        return weekDays;
    }
}
