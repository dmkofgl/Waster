package waster.web.controller;

import org.springframework.web.bind.annotation.RestController;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.repository.abstracts.OrderRepository;
import waster.domain.service.BenchScheduleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BenchSchedulerController {
    private OrderRepository orderRepository;
    private BenchScheduleService benchScheduleService;

    public void calculateCalendar(Long timeCountInHour, List<Long> ordersId) {
        List<Order> orders = ordersId.stream()
                .map(orderRepository::findById)
                .map(Optional::get)
                .collect(Collectors.toList());

        BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(timeCountInHour, orders);

    }
}
