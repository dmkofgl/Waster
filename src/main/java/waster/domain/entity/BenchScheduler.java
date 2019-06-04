package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.calendar.Calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BenchScheduler implements Serializable {
    private Date start;
    public final Long LIMIT;
    private Map<Bench, Calendar> benchCalendarMap = new HashMap<>();

    public BenchScheduler(Long limit, Date start) {
        this.start = start;
        this.LIMIT = limit;
    }
}
