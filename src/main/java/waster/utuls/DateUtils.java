package waster.utuls;

import waster.domain.entity.calendar.Schedule;

public class DateUtils {
    public static boolean isOverlap(Schedule existSchedule, Schedule newSchedule) {
        Long startA = existSchedule.getStart();
        Long endA = existSchedule.getEnd();
        Long startB = newSchedule.getStart();
        Long endB = newSchedule.getEnd();

        return isOverlap(startA, endA, startB, endB);
    }

    public static boolean isOverlap(Long startA, Long endA, Long startB, Long endB) {
        return max(startA, startB) <= min(endA, endB);
    }

    private static Long max(Long a, Long b) {
        return a > b ? a : b;
    }

    private static Long min(Long a, Long b) {
        return a < b ? a : b;
    }
}
