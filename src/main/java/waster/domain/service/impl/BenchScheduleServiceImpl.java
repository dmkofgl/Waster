package waster.domain.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import waster.domain.entity.*;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Operation;
import waster.domain.helper.BenchScheduleChartBuilder;
import waster.domain.helper.BenchScheduleExcelFileBuilder;
import waster.domain.repository.abstracts.StepRepository;
import waster.domain.service.BenchScheduleService;
import waster.domain.service.ProcessMapService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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

    private void fillBenchSchedulerWithOrder(BenchScheduler benchScheduler, Order order) {
        Article article = order.getArticle();
        Long initDate = 0L;
        String operationName = article.getName() + article.getColoring();
        ProcessMap processMap = article.getProcessMap();

        processMapService.getShortestPath(processMap)
                .stream()
                .map(stepId -> buildOperation(stepId, initDate, operationName, order.getLength()))
                .forEach(benchScheduler::addInBenchCalendar);
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
    public void outputInExcelFile(BenchScheduler benchScheduler) throws IOException {
        final String EXCEL_FILE_LOCATION = System.getProperty("user.dir") + "/src/main/resources/gantt/" + "testing.xlsx";
        this.outputInExcelFile(EXCEL_FILE_LOCATION, benchScheduler);
    }

    @Override
    public void outputInExcelFile(String pathToFile, BenchScheduler benchScheduler) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();

        BenchScheduleExcelFileBuilder.createCommonSheet(workbook, benchCalendarMap);
        for (Bench bench : benches) {
            BenchScheduleExcelFileBuilder.createSheet(bench, benchCalendarMap.get(bench), workbook);
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
            BenchScheduleChartBuilder benchScheduleChartBuilder = new BenchScheduleChartBuilder(deadlineTime, benchScheduler);
            benchScheduleChartBuilder.createChartForBench(bench, calendar);
        }

    }

}
