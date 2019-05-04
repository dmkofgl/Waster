package waster.domain.service;

import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.calendar.Operation;

import java.io.IOException;
import java.util.List;

public interface BenchScheduleService {

    BenchScheduler calculateScheduleForBenchesForOrders(Long limitTimeInHours, Iterable<Order> orderList);

    BenchScheduler findOptimalBenchSchedule(Long limitTimeInHours, List<Order> orderList);

    void outputInExcelFile(BenchScheduler benchScheduler) throws IOException;

    void outputInExcelFile(String pathToFile, BenchScheduler benchScheduler) throws IOException;


    void outputAsPicture(BenchScheduler benchScheduler) throws IOException;

    void outputAsPicture(Double deadlineTime, BenchScheduler benchScheduler) throws IOException;

    BenchScheduler addOperationInBenchScheduler(BenchScheduler benchScheduler, Operation operation);
}
