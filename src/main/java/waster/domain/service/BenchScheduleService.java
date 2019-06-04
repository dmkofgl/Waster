package waster.domain.service;

import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.calendar.Operation;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface BenchScheduleService {

    BenchScheduler calculateScheduleForBenchesForOrders(Date startDate, Long limitTimeInHours, Iterable<Order> orderList);

    BenchScheduler findOptimalBenchSchedule(Date startDate,Long limitTimeInHours, List<Order> orderList);

    Long calculateEndTimeForOperation(BenchScheduler benchScheduler, Operation operation);

    String outputInExcelFile(BenchScheduler benchScheduler) throws IOException;

    String outputInExcelFile(String pathToFile, BenchScheduler benchScheduler) throws IOException;

    Long getWorkingTimeScheduler(BenchScheduler benchScheduler);

    void outputAsPicture(BenchScheduler benchScheduler) throws IOException;

    void outputAsPicture(Double deadlineTime, BenchScheduler benchScheduler) throws IOException;

    BenchScheduler addOperationInBenchScheduler(BenchScheduler benchScheduler, Operation operation);

    Collection<Bench> getOverworkedBenches(BenchScheduler benchScheduler);
}
