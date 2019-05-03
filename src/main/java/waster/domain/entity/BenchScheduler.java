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
    private final Long LIMIT;
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

    public void addInBenchCalendar(Operation operation) {
        Machine machine = operation.getStep().getMachine();
        //достать все машины данного типа
        List<Bench> benches = bindMap.get(machine);
        //
        Bench optimalBench = benches.stream().min((x, y) -> {
            Calendar calendarPrev = benchCalendarMap.get(x);
            if (calendarPrev == null) {
                calendarPrev = new Calendar();
                benchCalendarMap.put(x, calendarPrev);
            }
            Calendar calendarNext = benchCalendarMap.get(y);
            if (calendarNext == null) {
                calendarNext = new Calendar();
                benchCalendarMap.put(y, calendarNext);
            }
            Schedule schedulePrev = calendarPrev.applyOperation(operation);
            Schedule scheduleNext = calendarNext.applyOperation(operation);
            return Long.compare(schedulePrev.getStart(), scheduleNext.getStart());
        }).get();
       if(! benchCalendarMap.containsKey(optimalBench)){
           benchCalendarMap.put(optimalBench, new Calendar());
       }
        benchCalendarMap.get(optimalBench).applyOperation(operation);
    }

}
