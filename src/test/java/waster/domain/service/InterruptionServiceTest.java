package waster.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Setting;
import waster.domain.entity.calendar.Interruption;
import waster.domain.entity.calendar.Operation;
import waster.domain.repository.abstracts.InterruptionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterruptionServiceTest {
    @MockBean
    private InterruptionRepository interruptionRepository;
    @Autowired
    private InterruptionService interruptionService;

    @Test
    public void getFirstOverlapInterruption() throws ParseException {
        Mockito.when(interruptionRepository.findAll()).thenReturn(initInterruptions());

        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("15/6/2019");
        Long initStartDate = 0L;
        Operation operation = Operation.builder()
                .initialStartDate(initStartDate)
                .length(1000.0)
                .setting(Setting.builder().timeDependOnLength(false).workingSpeed(100.0).build())
                .build();
        Optional<Interruption> interruption = interruptionService.getFirstOverlapInterruption(operation, startDate);
        assertTrue(interruption.isPresent());
    }
    @Test
    public void getLastOverlapInterruption() throws ParseException {
        Mockito.when(interruptionRepository.findAll()).thenReturn(initInterruptions());

        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("15/6/2019");
        Long initStartDate = 0L;
        Operation operation = Operation.builder()
                .initialStartDate(initStartDate)
                .length(1000.0)
                .setting(Setting.builder().timeDependOnLength(false).workingSpeed(100.0).build())
                .build();
        Optional<Interruption> interruption = interruptionService.getLastOverlapInterruption(operation, startDate);
        assertTrue(interruption.isPresent());
    }
    @Test
    public void getLastOverlapInterruptionWhenInterrupt() throws ParseException {
        Mockito.when(interruptionRepository.findAll()).thenReturn(initInterruptions());

        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/6/2019");
        Long initStartDate = 0L;
        Operation operation = Operation.builder()
                .initialStartDate(initStartDate)
                .length(1000.0)
                .setting(Setting.builder().timeDependOnLength(false).workingSpeed(100.0).build())
                .build();
        Optional<Interruption> interruption = interruptionService.getLastOverlapInterruption(operation, startDate);
        assertFalse(interruption.isPresent());
    }

    private List<Interruption> initInterruptions() throws ParseException {
        List<Interruption> interruptionList = new ArrayList<>();
        String sDate1 = "8/6/2019";
        String sDate2 = "10/6/2019";
        interruptionList.add(saveInterruption(sDate1, sDate2));
        sDate1 = "15/6/2019";
        sDate2 = "17/6/2019";
        interruptionList.add(saveInterruption(sDate1, sDate2));
        sDate1 = "22/6/2019";
        sDate2 = "24/6/2019";
        interruptionList.add(saveInterruption(sDate1, sDate2));
        sDate1 = "29/6/2019";
        sDate2 = "1/7/2019";
        interruptionList.add(saveInterruption(sDate1, sDate2));
        return interruptionList;
    }

    private Interruption saveInterruption(String startDate, String endDate) throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        Interruption interruption = new Interruption();
        interruption.setStart(date1);
        interruption.setEnd(date2);
        return interruption;

    }

}