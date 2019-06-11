package waster.math;

import lombok.*;
import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.calendar.Calendar;
import waster.domain.helper.MapListKey;
import waster.domain.service.BenchScheduleService;
import waster.excptions.AllBenchesOverworkingException;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneticShuffle {
    private BenchScheduleService benchScheduleService;
    private int maxGenerationCont = 20;
    private int generationEntityLimit = 5;
    private int genCount = 120;
    private Date startCalculateDate = new Date();


    private List<Order> shuffleOrders(List<Order> sourceOrders) {
        List<Order> orders = new ArrayList<>(sourceOrders);
        Collections.shuffle(orders);
        return orders;
    }

    public BenchScheduler findOptimalBenchScheduler(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> prevGeneration = findOptimalMap(limitTimeInHours, orderList);

        return prevGeneration.get(getSomeOptimal(prevGeneration).get(0));
    }

    private Map<MapListKey, BenchScheduler> findOptimalMap(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> prevGeneration = executeGeneration(limitTimeInHours, orderList);
        for (int i = 0; i < maxGenerationCont; i++) {
            List<MapListKey> nextOrders = crossover(prevGeneration);
            Map<MapListKey, BenchScheduler> nextGeneration = calculateNextGeneration(limitTimeInHours, nextOrders);
            //if optimal gens are equals then you don't calculate more optimal result
            if (getSomeOptimal(prevGeneration).equals((getSomeOptimal(nextGeneration)))) {
                break;
            }
            prevGeneration = nextGeneration;
        }
        return prevGeneration;
    }

    private Map<MapListKey, BenchScheduler> executeGeneration(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> orderBenchSchedulerMap = new HashMap<>();
        orderBenchSchedulerMap.put(new MapListKey(orderList), benchScheduleService.calculateScheduleForBenchesForOrders(startCalculateDate, limitTimeInHours, orderList));
        for (int i = 0; i < genCount - 1; i++) {
            List<Order> shuffledOrders = shuffleOrders(orderList);
            BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(startCalculateDate, limitTimeInHours, shuffledOrders);
            orderBenchSchedulerMap.put(new MapListKey(shuffledOrders), benchScheduler);
        }
        if (checkToAllOverworked(orderBenchSchedulerMap)) {
            throw new AllBenchesOverworkingException();
        }

        return orderBenchSchedulerMap;
    }

    private List<MapListKey> crossover(Map<MapListKey, BenchScheduler> OrderBenchSchedulerMap) {
        List<MapListKey> prevOptimalGeneration = getSomeOptimal(OrderBenchSchedulerMap);
        List<MapListKey> nextGeneration = new LinkedList<>(prevOptimalGeneration);
        for (int i = 0; i < prevOptimalGeneration.size(); i++) {
            for (int j = i; j < prevOptimalGeneration.size(); j++) {
                MapListKey halfCombineOrders = halfCombineOrders(prevOptimalGeneration.get(i), prevOptimalGeneration.get(j));
                MapListKey serialCombineOrders = serialCombineOrders(prevOptimalGeneration.get(i), prevOptimalGeneration.get((j)));
                nextGeneration.add(halfCombineOrders);
                nextGeneration.add(serialCombineOrders);
            }
        }
        return nextGeneration;
    }

    private MapListKey halfCombineOrders(MapListKey firstGen, MapListKey secondGen) {
        List<Order> result = new ArrayList<>();
        List<Order> firstGenArray = new ArrayList<>(firstGen.getOrderList());
        List<Order> secondGenArray = new ArrayList<>(secondGen.getOrderList());
        int firstGenHalfSize = firstGenArray.size() / 2;
        for (int i = 0; i < firstGenHalfSize; i++) {
            Order order = firstGenArray.get(i);
            result.add(order);
            secondGenArray.remove(order);
        }
        result.addAll(secondGenArray);
        return new MapListKey(result);
    }

    private MapListKey serialCombineOrders(MapListKey firstGen, MapListKey secondGen) {
        List<Order> result = new ArrayList<>();
        List<Order> firstGenArray = new ArrayList<>(firstGen.getOrderList());
        List<Order> secondGenArray = new ArrayList<>(secondGen.getOrderList());
        int i = 0;
        while (firstGenArray.size() > 0) {
            Order order = i % 2 == 0 ? firstGenArray.get(0) : secondGenArray.get(0);
            firstGenArray.remove(order);
            secondGenArray.remove(order);
            result.add(order);
            i++;
        }
        return new MapListKey(result);
    }


    private List<MapListKey> getSomeOptimal(Map<MapListKey, BenchScheduler> OrderBenchSchedulerMap) {
        return OrderBenchSchedulerMap.entrySet()
                .parallelStream()
                .sorted(Comparator.comparingLong(es -> this.findMaxWorkingTime(es.getValue())))
                .limit(generationEntityLimit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<MapListKey, BenchScheduler> calculateNextGeneration(Long limitTimeInHours, List<MapListKey> orderList) {
        Map<MapListKey, BenchScheduler> orderBenchSchedulerMap = new HashMap<>();
        for (MapListKey orders : orderList) {
            BenchScheduler benchScheduler = benchScheduleService.calculateScheduleForBenchesForOrders(startCalculateDate, limitTimeInHours, orders.getOrderList());
            orderBenchSchedulerMap.put(orders, benchScheduler);
        }
        return orderBenchSchedulerMap;
    }

    private boolean checkToAllOverworked(Map<MapListKey, BenchScheduler> OrderBenchSchedulerMap) {
        Set<MapListKey> orderKeySet = OrderBenchSchedulerMap.keySet();
        List<MapListKey> ordersWithoutOverworking = orderKeySet.stream()
                .filter(list -> benchScheduleService.getOverworkedBenches(OrderBenchSchedulerMap.get(list)).size() == 0)
                .collect(Collectors.toList());
        return ordersWithoutOverworking.isEmpty();

    }

    public Long findMaxWorkingTime(BenchScheduler benchScheduler) {
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();
        Long maxTime = 0L;
        for (Bench bench : benches) {
            Calendar calendar = benchCalendarMap.get(bench);
            maxTime = calendar.lastSchedulesActionEndTime() > maxTime
                    ? calendar.lastSchedulesActionEndTime()
                    : maxTime;
        }
        return maxTime;
    }
}
