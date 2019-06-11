package waster.domain.helper.converter;

import org.junit.Test;
import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Operation;
import waster.domain.entity.calendar.Schedule;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BenchSchedulerConverterTest {

    @Test
    public void convertToDatabaseColumn() {
        BenchSchedulerConverter benchSchedulerConverter = new BenchSchedulerConverter();
        Long limit = 10L;
        Date date = new Date();
        BenchScheduler benchScheduler = new BenchScheduler(limit, date);
        Bench bench = Bench.builder().id(13L).name("bench").build();
        Calendar calendar = new Calendar(date, null);
        calendar.getSchedules().add(new Schedule(Operation.builder().length(10.0).build()));

        benchScheduler.getBenchCalendarMap().put(bench, calendar);
        byte[] bytes = benchSchedulerConverter.convertToDatabaseColumn(benchScheduler);
        BenchScheduler benchScheduler1 = benchSchedulerConverter.convertToEntityAttribute(bytes);
        assertEquals(benchScheduler.getStart(), benchScheduler1.getStart());
        assertEquals(benchScheduler.getLIMIT(), benchScheduler1.getLIMIT());
        benchScheduler.getBenchCalendarMap().forEach((x, y) ->
                assertTrue(benchScheduler1.getBenchCalendarMap().containsKey(x))
        );
    }
}