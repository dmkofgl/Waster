package waster.domain.entity;

import org.springframework.util.MultiValueMap;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Operation;
import waster.domain.entity.calendar.Schedule;
import waster.domain.repository.BenchAndMachineRepository;
import waster.domain.repository.FakeBenchRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenchScheduler {
    public final Long LIMIT;
    private static FakeBenchRepository fakeBenchRepository = new FakeBenchRepository();
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();
    private MultiValueMap<Machine, Bench> bindMap = benchAndMachineRepository.getBindMap();
    private Map<Bench, Calendar> benchCalendarMap = new HashMap<>();

    public BenchScheduler(Long limit) {
        this.LIMIT = limit;
    }

    public Map<Bench, Calendar> getBenchCalendarMap() {
        return benchCalendarMap;
    }
}
