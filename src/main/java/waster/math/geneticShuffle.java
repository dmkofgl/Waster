package waster.math;

import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.Order;
import waster.domain.entity.calendar.Calendar;
import waster.domain.helper.MapListKey;
import waster.domain.service.BenchScheduleService;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class geneticShuffle {
    //TODO UGLY
    private BenchScheduleService benchScheduleService;
    private int maxGenerationCont = 20;
    final int generationEntityLimit = 5;

    private List<Order> shuffleOrders(List<Order> sourceOrders) {
        List<Order> orders = new ArrayList<>(sourceOrders);
        Collections.shuffle(orders);
        return orders;
    }

    public List<Order> findOptimaOrderSequence(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> prevGeneration = findOptimalMap(limitTimeInHours, orderList);
        return getSomeOptimal(prevGeneration).get(0).getOrderList();
    }

    public BenchScheduler findOptimalBenchScheduler(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> prevGeneration = findOptimalMap(limitTimeInHours, orderList);

        return prevGeneration.get(getSomeOptimal(prevGeneration).get(0));
    }

    private Map<MapListKey, BenchScheduler> findOptimalMap(Long limitTimeInHours, List<Order> orderList) {
        Map<MapListKey, BenchScheduler> prevGeneration = executeGeneration(limitTimeInHours, orderList);
        for (int i = 0; i < maxGenerationCont; i++) {
            Map<MapListKey, BenchScheduler> nextGeneration = calculateNextGeneration(limitTimeInHours, crossover(prevGeneration));
            //if optimal gens are equals then you don't calculate more optimal result
            if (getSomeOptimal(prevGeneration).equals((getSomeOptimal(nextGeneration)))) {
                break;
            }
            prevGeneration.entrySet()
                    .stream()
                    .forEach(es -> System.out.println(findMaxWorkingTime(es.getValue())));
            prevGeneration = nextGeneration;
        }
        return prevGeneration;
    }

    private Map<MapListKey, BenchScheduler> executeGeneration(Long limitTimeInHours, List<Order> orderList) {
        final int genCount = 120;
        Map<MapListKey, BenchScheduler> orderBenchSchedulerMap = new HashMap<>();
        orderBenchSchedulerMap.put(new MapListKey(orderList), benchScheduleService.calculateScheduleForBenchesForOrders(limitTimeInHours, orderList));
        for (int i = 0; i < genCount - 1; i++) {
            List<Order> shuffledOrders = shuffleOrders(orderList);
            orderBenchSchedulerMap.put(new MapListKey(shuffledOrders), benchScheduleService.calculateScheduleForBenchesForOrders(limitTimeInHours, shuffledOrders));
        }
        //TODO if there is no any sequence without overworking then you can't execute this orders
        if (checkToAllOverworked(orderBenchSchedulerMap)) {
            throw new RuntimeException("there is all overworking");
        }

        return orderBenchSchedulerMap;
    }

    private List<MapListKey> crossover(Map<MapListKey, BenchScheduler> OrderBenchSchedulerMap) {
        List<MapListKey> prevOptimalGeneration = getSomeOptimal(OrderBenchSchedulerMap);
        List<MapListKey> nextGeneration = new LinkedList<>(prevOptimalGeneration);
        for (int i = 0; i < prevOptimalGeneration.size() - 1; i++) {

            for (int j = i; j < prevOptimalGeneration.size(); j++) {
                MapListKey temp = halfCombineOrders(prevOptimalGeneration.get(i), prevOptimalGeneration.get(j));
                nextGeneration.add(temp);
            }
        }
        return nextGeneration;
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
            orderBenchSchedulerMap.put(orders, benchScheduleService.calculateScheduleForBenchesForOrders(limitTimeInHours, orders.getOrderList()));
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

    public Long findMaxWorkingTime(BenchScheduler benchScheduler) {
        Map<Bench, Calendar> benchCalendarMap = benchScheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();
        Long maxTime = 0L;
        for (Bench bench : benches) {
            Calendar calendar = benchCalendarMap.get(bench);
            maxTime = calendar.lastActionEndTime() > maxTime
                    ? calendar.lastActionEndTime()
                    : maxTime;
        }
        return maxTime;
    }
}
