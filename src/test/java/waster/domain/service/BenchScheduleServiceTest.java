package waster.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.Setting;
import waster.domain.entity.Step;
import waster.domain.entity.calendar.Operation;
import waster.domain.repository.abstracts.ArticleRepository;
import waster.domain.repository.abstracts.OrderRepository;
import waster.domain.repository.abstracts.SettingsRepository;
import waster.domain.repository.abstracts.StepRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BenchScheduleServiceTest {
    @Autowired
    private BenchScheduleService benchScheduleService;
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SettingsRepository settingsRepository;

    @Test
    public void addOperationInBenchScheduler() throws IOException {
        final Long LIMIT = 8L * 60 * 60 * 1000;
        BenchScheduler benchScheduler = new BenchScheduler(LIMIT);

        benchScheduleService.addOperationInBenchScheduler(benchScheduler, buildOperation());
        benchScheduleService.addOperationInBenchScheduler(benchScheduler, buildOperation());
        benchScheduler = benchScheduleService.addOperationInBenchScheduler(benchScheduler, buildOperation());
        benchScheduler.getBenchCalendarMap();
        benchScheduleService.outputInExcelFile(benchScheduler);
    }

    private Operation buildOperation() {
        final Long stepId = 6L;
        final double LENGTH = 1000;
        final Long INIT_START_DATE = 0L;
        Setting setting = settingsRepository.findById(stepId).get();
        return Operation.builder()
                .length(LENGTH)
                .initialStartDate(INIT_START_DATE)
                .setting(setting)
                .build();
    }

    @Test
    public void findOptimal() throws IOException {
        final Long LIMIT = 8L * 60 * 60 * 1000*28;
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            double min = 0;
            double max = 1 * 1000 * 60 * 60;
            Random random = new Random();
            Double length = min + (max - min) * random.nextDouble();
            orders.add(createOrder(3L, length));
        }
        BenchScheduler benchScheduler = benchScheduleService.findOptimalBenchSchedule(LIMIT, orders);

        benchScheduleService.outputInExcelFile(benchScheduler);

    }

    private Order createOrder(Long articleId, Double length) {
        Order order = new Order();

        order.setLength(length);
        order.setArticle(articleRepository.findById(articleId).get());
        return order;
    }

}