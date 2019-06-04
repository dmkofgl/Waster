package waster.domain.helper;

import org.junit.Test;
import waster.domain.entity.BenchScheduler;
import waster.domain.helper.converter.BenchSchedulerConverter;

import java.util.Date;

public class BenchSchedulerConverterTest {

    @Test
    public void convertToDatabaseColumn() {
        Long limit = 8L;
        Date date = new Date();
        BenchScheduler benchScheduler = new BenchScheduler(limit, date);
        BenchSchedulerConverter benchSchedulerConverter = new BenchSchedulerConverter();
        benchSchedulerConverter.convertToDatabaseColumn(benchScheduler);
    }

    @Test
    public void convertToEntityAttribute() {
    }
}