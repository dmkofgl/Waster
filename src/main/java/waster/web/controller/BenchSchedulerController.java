package waster.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.PlanReport;
import waster.domain.repository.abstracts.OrderRepository;
import waster.domain.service.BenchScheduleService;
import waster.domain.service.PlanReportService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BenchSchedulerController {
    private OrderRepository orderRepository;
    private BenchScheduleService benchScheduleService;
    private PlanReportService planReportService;

    @Autowired
    public BenchSchedulerController(OrderRepository orderRepository, BenchScheduleService benchScheduleService) {
        this.orderRepository = orderRepository;
        this.benchScheduleService = benchScheduleService;
    }

    @PostMapping("/api/calculate")
    public void calculateCalendar(@RequestBody CalculateRequest calculateRequest) throws IOException {
        List<Order> orders = calculateRequest.getOrdersId().stream()
                .map(orderRepository::findById)
                .map(Optional::get)
                .collect(Collectors.toList());

        BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(calculateRequest.getTimeCountInHour(), orders);

        String pathToFile = benchScheduleService.outputInExcelFile(benchScheduler);
        PlanReport planReport = PlanReport.builder()
                .benchScheduler(benchScheduler)
                .orders(orders)
                .filePath(pathToFile)
                .build();
        planReportService.save(planReport);
    }

    @GetMapping("/api/reports")
    public List<PlanReport> viewReports() throws IOException {
        return planReportService.findAll();
    }
    @GetMapping("/api/reports/{id}/download")
    public List<PlanReport> downloadReport() throws IOException {
        return planReportService.findAll();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class OrderPojo {
        private String name;

    }


}
