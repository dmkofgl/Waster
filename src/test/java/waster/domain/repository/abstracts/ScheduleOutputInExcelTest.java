package waster.domain.repository.abstracts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.service.BenchScheduleService;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleOutputInExcelTest {
    @Autowired
    private BenchScheduleService benchScheduleService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void checkAutowired() {
        Assert.assertNotNull(benchScheduleService);
    }

    @Test
    public void outputInExcelFile() throws IOException {
        final Long LIMIT = 8L * 60 * 60 * 1000;
        Iterable<Order> orders = orderRepository.findAll();
        Date date = new Date();
        BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(date, LIMIT, orders);
        benchScheduleService.outputInExcelFile(benchScheduler);
    }

}
