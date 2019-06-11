package waster.domain.helper;

import org.junit.Test;
import waster.domain.entity.Setting;
import waster.domain.entity.calendar.Operation;
import waster.domain.entity.calendar.Schedule;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BenchScheduleExcelFileBuilderTest {

    @Test
    public void durationHourAndMinutes() {
        Date date = new Date();
        BenchScheduleExcelFileBuilder benchScheduleExcelFileBuilder = new BenchScheduleExcelFileBuilder(date, null);
        Long startTime = 0L;
        Long prepareTime = 150L;
        Double speed = 100.0;
        Double length = 1000.0;
        Setting setting = Setting.builder()
                .timeDependOnLength(true)
                .prepareTime(prepareTime)
                .workingSpeed(speed)
                .build();
        Operation operation = Operation.builder()
                .initialStartDate(startTime)
                .setting(setting)
                .length(length)
                .build();
        Schedule schedule = new Schedule(operation);
        String result = benchScheduleExcelFileBuilder.durationHourAndMinutes(schedule);
        String expected = "0:00:12:30";
        assertEquals(expected, result);
    }
    @Test
    public void durationHourAndMinutes_whenNotDependsOnLength() {
        Date date = new Date();
        BenchScheduleExcelFileBuilder benchScheduleExcelFileBuilder = new BenchScheduleExcelFileBuilder(date, null);
        Long startTime = 0L;
        Long prepareTime = 150L;
        Double speed = 100.0;
        Double length = 1000.0;
        Setting setting = Setting.builder()
                .timeDependOnLength(false)
                .prepareTime(prepareTime)
                .workingSpeed(speed)
                .build();
        Operation operation = Operation.builder()
                .initialStartDate(startTime)
                .setting(setting)
                .length(length)
                .build();
        Schedule schedule = new Schedule(operation);
        String result = benchScheduleExcelFileBuilder.durationHourAndMinutes(schedule);
        String expected = "0:02:05:00";
        assertEquals(expected, result);
    }
}