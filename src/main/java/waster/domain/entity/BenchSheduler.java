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

public class BenchSheduler {
    private final Long LIMIT;
    private static FakeBenchRepository fakeBenchRepository = new FakeBenchRepository();
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();
    private MultiValueMap<Machine, Bench> bindMap = benchAndMachineRepository.getBindMap();
    private Map<Bench, Calendar> benchCalendarMap = new HashMap<>();

    public BenchSheduler(Long limit) {
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
            Calendar calendar1 = benchCalendarMap.get(x);
            if (calendar1 == null) {
                calendar1 = new Calendar();
                benchCalendarMap.put(x, calendar1);
            }
            Calendar calendar2 = benchCalendarMap.get(y);
            if (calendar2 == null) {
                calendar2 = new Calendar();
                benchCalendarMap.put(y, calendar2);
            }
            Schedule schedule1 = calendar1.applyOperation(operation);
            Schedule schedule2 = calendar2.applyOperation(operation);
            return Long.compare(schedule1.getStart(), schedule2.getStart());
        }).get();
       if(! benchCalendarMap.containsKey(optimalBench)){
           benchCalendarMap.put(optimalBench, new Calendar());
       }
        benchCalendarMap.get(optimalBench).applyOperation(operation);
    }

}
