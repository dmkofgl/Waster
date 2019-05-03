package waster.domain.service;

import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;

import java.io.IOException;

public interface BenchScheduleService {

    BenchScheduler calculateScheduleForBenchesForOrders(Long limitTimeInHours, Iterable<Order> orderList);

    void outputInExcelFile(BenchScheduler benchScheduler) throws IOException;

    void outputInExcelFile(String pathToFile, BenchScheduler benchScheduler) throws IOException;


    void outputAsPicture(BenchScheduler benchScheduler) throws IOException;

    void outputAsPicture(Double deadlineTime, BenchScheduler benchScheduler) throws IOException;
}
