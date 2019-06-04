package waster.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import waster.domain.entity.*;
import waster.domain.entity.calendar.Calendar;
import waster.domain.repository.abstracts.OrderRepository;
import waster.domain.service.BenchScheduleService;
import waster.domain.service.PlanReportService;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BenchSchedulerController {
    private OrderRepository orderRepository;
    private BenchScheduleService benchScheduleService;
    private PlanReportService planReportService;

    @Autowired
    public BenchSchedulerController(OrderRepository orderRepository, BenchScheduleService benchScheduleService, PlanReportService planReportService) {
        this.orderRepository = orderRepository;
        this.benchScheduleService = benchScheduleService;
        this.planReportService = planReportService;
    }

    @PostMapping("/api/calculate")

    //TODO split flag to different endpoints
    //TODO change if to @valid
    public ReportState calculateCalendar(@RequestBody CalculateRequest calculateRequest) throws IOException {
        if (calculateRequest.getStartDate() == null) {
            calculateRequest.setStartDate(new Date());
        }

        List<Order> orders = calculateRequest.getOrdersId().stream()
                .map(orderRepository::findById)
                .map(Optional::get)
                .collect(Collectors.toList());
        Date createdDate = new Date();
        PlanReport planReport = PlanReport.builder()
                .orders(orders)
                .reportTitle(calculateRequest.getReportTitle())
                .createDate(createdDate)
                .state(ReportState.CALCULATING)
                .build();

        Runnable task = () -> {
            try {
                calculateTask(calculateRequest, planReport);
            } catch (IOException e) {
               throw  new RuntimeException();
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        planReportService.save(planReport);
        return ReportState.CALCULATING;
    }

    private void calculateTask(CalculateRequest calculateRequest, PlanReport report) throws IOException {
        List<Order> orders = report.getOrders();
        Long limitTimeInMS = calculateRequest.getTimeCountInHour() * 60 * 60 * 1000;
        BenchScheduler benchScheduler = calculateRequest.isOptimal() ?
                benchScheduleService.findOptimalBenchSchedule(calculateRequest.getStartDate(), limitTimeInMS, orders)
                : benchScheduleService.calculateScheduleForBenchesForOrders(calculateRequest.getStartDate(), limitTimeInMS, orders);

        String pathToFile = benchScheduleService.outputInExcelFile(report.getId()+report.getReportTitle(),benchScheduler);
        ReportState reportState = benchScheduleService.getOverworkedBenches(benchScheduler).size() > 0 ? ReportState.OVERWORKING : ReportState.RIGHT;
        Date endDate = new Date(report.getCreateDate().getTime() + benchScheduleService.getWorkingTimeScheduler(benchScheduler));

        report.setBenchScheduler(benchScheduler);
        report.setFilePath(pathToFile);
        report.setEndDate(endDate);
        report.setState(reportState);
        planReportService.save(report);
    }

    @GetMapping("/api/reports")
    public List<PlanReport> viewReports() throws IOException {
        return planReportService.findAll();
    }

    @GetMapping("/api/reports/{id}")
    public Object viewReports(@PathVariable("id") Long reportId) throws IOException {
        PlanReport planReport = planReportService.findById(reportId).orElseThrow(EntityNotFoundException::new);
        List<ReportDetails> reportDetails = planReport.getBenchScheduler().getBenchCalendarMap().entrySet().stream()
                .map(e -> new ReportDetails(e.getKey(), e.getValue())).collect(Collectors.toList());
        return new ReportDetailsResponse(planReport, reportDetails);

    }

    @AllArgsConstructor
    @Getter
    private class ReportDetails {
        private Bench bench;
        private Calendar calendar;
    }

    @AllArgsConstructor
    @Getter
    private class ReportDetailsResponse {
        private PlanReport planReport;
        private List<ReportDetails> reportDetails;
    }


    @GetMapping("/api/reports/{id}/download")
    public ResponseEntity downloadReport(@PathVariable("id") Long reportId) throws IOException {
        PlanReport planReport = planReportService.findById(reportId).orElseThrow(EntityNotFoundException::new);
        MediaType mediaType = MediaType.TEXT_PLAIN;
        final String filenameHeader = getAttachmentHeader(planReport.getFilePath());
        File file = new File(planReport.getFilePath());
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, filenameHeader)
                .contentType(mediaType)
                .body(isr);
    }

    private String getAttachmentHeader(String title) {
        return "attachment;filename=" + title;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class OrderPojo {
        private String name;

    }
}
