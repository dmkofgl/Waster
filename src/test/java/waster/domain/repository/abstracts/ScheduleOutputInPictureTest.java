package waster.domain.repository.abstracts;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Schedule;
import waster.domain.service.BenchScheduleService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO replace this method in service class
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleOutputInPictureTest {
    @Autowired
    private BenchScheduleService benchScheduleService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void checkAutowired() {
        Assert.assertNotNull(benchScheduleService);
    }


}
