package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.calendar.Calendar;

import java.util.HashMap;
import java.util.Map;

public class BenchScheduler {
    public final Long LIMIT;
    @Getter
    private Map<Bench, Calendar> benchCalendarMap = new HashMap<>();

    public BenchScheduler(Long limit) {
        this.LIMIT = limit;
    }
}
