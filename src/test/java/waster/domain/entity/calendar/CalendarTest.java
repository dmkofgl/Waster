package waster.domain.entity.calendar;

import com.googlecode.charts4j.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Setting;
import waster.domain.repository.abstracts.InterruptionRepository;
import waster.domain.service.InterruptionService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarTest {
    @MockBean
    private InterruptionRepository interruptionRepository;
    @Autowired
    private InterruptionService interruptionService;

    @Test
    public void applyOperation_whenInterrupted_shouldSplitToTwoOperations() {
        Long prepareTime = 100L;
        Long startDate = 0L;
        Calendar calendar = new Calendar(new Date(startDate), interruptionService);

        Long interruptionStart = startDate + 150L;
        Long interruptionEnd = interruptionStart + 300L;
        Double length = 1000.0;
        Double workingSpeed = 15.0;
        Setting setting = Setting.builder()
                .workingSpeed(workingSpeed)
                .prepareTime(prepareTime)
                .build();
        Operation operation = Operation.builder()
                .initialStartDate(startDate)
                .length(length)
                .setting(setting)
                .build();
        List<Interruption> interruption = Lists.of(Interruption.builder()
                .start(new Date(interruptionStart))
                .end(new Date(interruptionEnd))
                .build());
        Mockito.when(interruptionRepository.findAll()).thenReturn(interruption);

        calendar.applyOperation(operation);
        assertEquals(2, calendar.getSchedules().size());
    }

    @Test
    public void applyOperation_whenInterrupted_shouldSplitToThreeOperations() {
        Long prepareTime = 100L;
        Long startDate = 0L;
        Calendar calendar = new Calendar(new Date(startDate), interruptionService);


        Double length = 1000.0;
        Double workingSpeed = 15.0;
        Setting setting = Setting.builder()
                .workingSpeed(workingSpeed)
                .prepareTime(prepareTime)
                .build();
        Operation operation = Operation.builder()
                .initialStartDate(startDate)
                .length(length)
                .setting(setting)
                .build();

        Long interruptionStart = startDate + 150L;
        Long interruptionEnd = interruptionStart + 300L;

        Long interruptionStartNext = interruptionEnd + 1000L;
        Long interruptionEndNext = interruptionStartNext + 400L;

        List<Interruption> interruption = Lists.of(
                Interruption.builder()
                        .start(new Date(interruptionStart))
                        .end(new Date(interruptionEnd))
                        .build(),
                Interruption.builder()
                        .start(new Date(interruptionStartNext))
                        .end(new Date(interruptionEndNext))
                        .build());
        Mockito.when(interruptionRepository.findAll()).thenReturn(interruption);

        calendar.applyOperation(operation);
        assertEquals(3, calendar.getSchedules().size());
    }
    @Test
    public void applyOperation_whenStartDateExists_shouldSplitToThreeOperations() {

        Long startDate = new Date().getTime();
        Long initDate = 0L;
        Calendar calendar = new Calendar(new Date(startDate), interruptionService);

        Long prepareTime = 100L;
        Double workingSpeed = 15.0;
        Setting setting = Setting.builder()
                .workingSpeed(workingSpeed)
                .prepareTime(prepareTime)
                .build();

        Double length = 1000.0;
        Operation operation = Operation.builder()
                .initialStartDate(initDate)
                .length(length)
                .setting(setting)
                .build();

        Long interruptionStart =startDate+ initDate + 1500L;
        Long interruptionEnd = interruptionStart + 300L;

        Long interruptionStartNext = interruptionEnd + 1000L;
        Long interruptionEndNext = interruptionStartNext + 400L;

        List<Interruption> interruption = Lists.of(
                Interruption.builder()
                        .start(new Date(interruptionStart))
                        .end(new Date(interruptionEnd))
                        .build(),
                Interruption.builder()
                        .start(new Date(interruptionStartNext))
                        .end(new Date(interruptionEndNext))
                        .build());
        Mockito.when(interruptionRepository.findAll()).thenReturn(interruption);

        calendar.applyOperation(operation);
        assertEquals(3, calendar.getSchedules().size());
    }
}