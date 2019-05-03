package waster.domain.repository.abstracts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.service.BenchScheduleService;

import java.io.IOException;

//TODO replace this method in service class
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
        //TODO write test for it
        // benchScheduleService.outputInExcelFile();
    }

}
