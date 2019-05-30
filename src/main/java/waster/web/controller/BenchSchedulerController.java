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
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.PlanReport;
import waster.domain.entity.ReportState;
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
    public ReportState calculateCalendar(@RequestBody CalculateRequest calculateRequest) throws IOException {
        List<Order> orders = calculateRequest.getOrdersId().stream()
                .map(orderRepository::findById)
                .map(Optional::get)
                .collect(Collectors.toList());
        Long limitTimeInMS = calculateRequest.getTimeCountInHour() * 60 * 60 * 1000;
        BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(limitTimeInMS, orders);

        String pathToFile = benchScheduleService.outputInExcelFile(benchScheduler);
        ReportState reportState = benchScheduleService.getOverworkedBenches(benchScheduler).size() > 0 ? ReportState.OVERWORKING : ReportState.RIGHT;
        Date createdDate = new Date();
        Date endDate = new Date(createdDate.getTime() + benchScheduleService.getWorkingTimeScheduler(benchScheduler));
        PlanReport planReport = PlanReport.builder()
                .benchScheduler(benchScheduler)
                .orders(orders)
                .filePath(pathToFile)
                .reportTitle(calculateRequest.getReportTitle())
                .createDate(createdDate)
                .endDate(endDate)
                .state(reportState)
                .build();
        planReportService.save(planReport);
        return reportState;
    }

    @GetMapping("/api/reports")
    public List<PlanReport> viewReports() throws IOException {
        return planReportService.findAll();
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
