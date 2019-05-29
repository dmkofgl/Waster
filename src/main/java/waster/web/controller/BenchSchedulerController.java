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
import waster.domain.repository.abstracts.OrderRepository;
import waster.domain.service.BenchScheduleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class BenchSchedulerController {
    private OrderRepository orderRepository;
    private BenchScheduleService benchScheduleService;

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

        benchScheduleService.outputInExcelFile(benchScheduler);
    }

    @GetMapping("/api/reports")
    public void viewReports() throws IOException {
        String pathToReports = System.getProperty("user.dir") + "/src/main/resources/gantt/";

        try (Stream<Path> paths = Files.walk(Paths.get(pathToReports))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class OrderPojo {
        private String name;

    }


}
