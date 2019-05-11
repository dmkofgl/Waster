package waster.domain.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knowm.xchart.XYChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import waster.domain.entity.*;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Operation;
import waster.domain.helper.BenchScheduleChartBuilder;
import waster.domain.helper.BenchScheduleExcelFileBuilder;
import waster.domain.repository.BenchAndMachineRepository;
import waster.domain.repository.abstracts.StepRepository;
import waster.domain.service.BenchScheduleService;
import waster.domain.service.ProcessMapService;
import waster.math.GeneticShaffle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BenchScheduleServiceImpl implements BenchScheduleService {
    @Autowired
    private ProcessMapService processMapService;
    @Autowired
    private StepRepository stepRepository;

    @Override
    public BenchScheduler calculateScheduleForBenchesForOrders(Long limitTimeInHours, Iterable<Order> orderList) {
        BenchScheduler benchScheduler = new BenchScheduler(limitTimeInHours);
        for (Order order : orderList) {
            fillBenchSchedulerWithOrder(benchScheduler, order);
        }
        return benchScheduler;
    }

    @Override
    public BenchScheduler findOptimalBenchSchedule(Long limitTimeInHours, List<Order> orderList) {
        GeneticShaffle geneticShaffle = new GeneticShaffle();
        geneticShaffle.setBenchScheduleService(this);
        return geneticShaffle.findOptimalBenchScheduler(limitTimeInHours, orderList);
    }


    private void fillBenchSchedulerWithOrder(BenchScheduler benchScheduler, Order order) {
        Article article = order.getArticle();
        Long initDate = 0L;
        String operationName = article.getName() + article.getColoring();
        ProcessMap processMap = article.getProcessMap();

        processMapService.getShortestPath(processMap)
                .stream()
                .map(stepId -> buildOperation(stepId, initDate, operationName, order.getLength()))
                .forEach(operation -> addOperationInBenchScheduler(benchScheduler, operation));
    }

    private Operation buildOperation(Long stepId, Long initDate, String name, Double length) {
        Step step = stepRepository.findById(stepId).orElseThrow(RuntimeException::new);
        return Operation.builder()
                .initialStartDate(initDate)
                .step(Step.builder()
                        .id(step.getId())
                        .machine(step.getMachine())
                        .name(name)
                        .setting(step.getSetting())
                        .build())
                .length(length)
                .build();
    }


    @Override
    public BenchScheduler addOperationInBenchScheduler(BenchScheduler benchScheduler, Operation operation) {
        //TODO RENAME IT
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();

        Bench optimalBench = getOptimalBenchForExecuteOperation(operation, benchCalendarMap);
        //if there is only one bench then min() doesn't called
        if (!benchCalendarMap.containsKey(optimalBench)) {
            benchCalendarMap.put(optimalBench, new Calendar());
        }
        benchCalendarMap.get(optimalBench).applyOperation(operation);
        return benchScheduler;
    }

    @Override
    public Long calculateEndTimeForOperation(BenchScheduler benchScheduler, Operation operation) {
        //TODO RENAME IT
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();

        Bench optimalBench = getOptimalBenchForExecuteOperation(operation, benchCalendarMap);
        //if there is only one bench then min() doesn't called
        if (!benchCalendarMap.containsKey(optimalBench)) {
            benchCalendarMap.put(optimalBench, new Calendar());
        }
        return benchCalendarMap.get(optimalBench).calculateStartTimeForOperation(operation);
    }

    private Bench getOptimalBenchForExecuteOperation(Operation operation, Map<Bench, Calendar> benchCalendarMap) {
        final int SMALLER = -1, EQUALS = 0, MORE = 1;
        MultiValueMap<Machine, Bench> bindMap = new BenchAndMachineRepository().getBindMap();
        Machine machine = operation.getStep().getMachine();
        //достать все машины данного типа
        List<Bench> benches = bindMap.get(machine);
        return benches.stream().min((x, y) -> {
            if (!(benchCalendarMap.containsKey(x) || benchCalendarMap.containsKey(x))) {
                return EQUALS;
            }
            if (!benchCalendarMap.containsKey(x)) {
                return MORE;
            }
            if (!benchCalendarMap.containsKey(y)) {
                return SMALLER;
            }
            Calendar calendarPrev = getCalendarForBench(benchCalendarMap, x);
            Calendar calendarNext = getCalendarForBench(benchCalendarMap, y);
            return compareBenchesStartTimeToOperation(calendarPrev, calendarNext, operation);
        }).get();
    }


    //TODO CHANGE IT
    private Calendar getCalendarForBench(Map<Bench, Calendar> benchCalendarMap, Bench bench) {
        if (!benchCalendarMap.containsKey(bench)) {
            benchCalendarMap.put(bench, new Calendar());
        }
        return benchCalendarMap.get(bench);
    }

    // Compare equals benches which can start earlier
    private int compareBenchesStartTimeToOperation(Calendar calendarPrev, Calendar calendarNext, Operation operation) {
        Long startSchedulePrev = calendarPrev.calculateStartTimeForOperation(operation);
        Long startScheduleNext = calendarNext.calculateStartTimeForOperation(operation);
        return Long.compare(startSchedulePrev, startScheduleNext);
    }

    @Override
    public void outputInExcelFile(BenchScheduler benchScheduler) throws IOException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        final String EXCEL_FILE_LOCATION = System.getProperty("user.dir") + "/src/main/resources/gantt/" + dateFormat.format(date) + ".xlsx";
        this.outputInExcelFile(EXCEL_FILE_LOCATION, benchScheduler);
    }

    @Override
    public void outputInExcelFile(String pathToFile, BenchScheduler benchScheduler) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();
        List<Bench> benchArrayList = new ArrayList<>(benches);
        benchArrayList.sort((x, y) -> (int) (x.getId() - y.getId()));
        BenchScheduleExcelFileBuilder excelFileBuilder = new BenchScheduleExcelFileBuilder(workbook);
        excelFileBuilder.createCommonSheet(benchCalendarMap);
        excelFileBuilder.createOverWorkedList(getOverworkedBenches(benchScheduler));
        for (Bench bench : benchArrayList) {
            excelFileBuilder.createSheet(bench, benchCalendarMap.get(bench), benchScheduler.LIMIT);
        }
        FileOutputStream fos = new FileOutputStream(pathToFile);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    @Override
    public void outputAsPicture(BenchScheduler benchScheduler) throws IOException {
        final double RED_LINE = 8 * 60 * 60 * 1000;
        this.outputAsPicture(RED_LINE, benchScheduler);
    }

    @Override
    public void outputAsPicture(Double deadlineTime, BenchScheduler benchScheduler) throws IOException {
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();
        for (Bench bench : benches) {
            Calendar calendar = benchCalendarMap.get(bench);
            BenchScheduleChartBuilder benchScheduleChartBuilder = new BenchScheduleChartBuilder(deadlineTime);
            XYChart chart = benchScheduleChartBuilder.getChartBytesForBench(bench, calendar);
            benchScheduleChartBuilder.saveChartInFile(chart, "./benchGraph/" + bench.getId());
        }

    }

    public Collection<Bench> getOverworkedBenches(BenchScheduler benchScheduler) {
        List<Bench> overworkedBenches = new ArrayList<>();
        Set<Bench> benches = benchScheduler.getBenchCalendarMap().keySet();
        for (Bench bench : benches) {
            Calendar calendar = benchScheduler.getBenchCalendarMap().get(bench);
            if (calendar.lastActionEndTime() > benchScheduler.LIMIT) {
                overworkedBenches.add(bench);
            }
        }
        return overworkedBenches;
    }

}