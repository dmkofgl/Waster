package waster.domain.helper;

import org.junit.Test;
import waster.domain.entity.BenchScheduler;

import static org.junit.Assert.*;

public class BenchSchedulerConverterTest {

    @Test
    public void convertToDatabaseColumn() {
        Long limit =8L;
        BenchScheduler benchScheduler = new BenchScheduler(limit);
        BenchSchedulerConverter benchSchedulerConverter = new BenchSchedulerConverter();
        benchSchedulerConverter.convertToDatabaseColumn(benchScheduler);
    }

    @Test
    public void convertToEntityAttribute() {
    }
}