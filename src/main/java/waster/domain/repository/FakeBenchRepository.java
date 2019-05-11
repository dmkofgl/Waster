package waster.domain.repository;

import waster.domain.entity.Bench;
import waster.domain.entity.Machine;

import java.util.Arrays;
import java.util.List;

public class FakeBenchRepository {
    private static FakeMachineRepository machineRepository = new FakeMachineRepository();
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();
    private static List<Bench> benches = Arrays.asList(
            createBench(-1L, -1L),
            createBench(-2L, -2L),
            createBench(10L, 10L),
            createBench(13L, 10L),
            createBench(14L, 10L),
            createBench(80L, 10L),
            createBench(81L, 10L),
            createBench(157L, 10L),
            createBench(166L, 10L),
            createBench(16L, 14L),
            createBench(34L, 14L),
            createBench(149L, 14L),
            createBench(151L, 14L),
            createBench(152L, 14L),
            createBench(40L, 20L),
            createBench(41L, 21L),
            createBench(63L, 30L),
            createBench(86L, 44L),
            createBench(87L, 45L),
            createBench(88L, 45L),
            createBench(89L, 45L),
            createBench(90L, 45L),
            createBench(91L, 45L),
            createBench(92L, 45L),
            createBench(93L, 45L),
            createBench(94L, 45L),
            createBench(95L, 45L),
            createBench(96L, 45L),
            createBench(97L, 45L),
            createBench(99L, 45L),
            createBench(98L, 45L),
            createBench(100L, 45L),
            createBench(101L, 45L),
            createBench(102L, 45L),
            createBench(103L, 45L),
            createBench(104L, 45L),
            createBench(105L, 45L),
            createBench(106L, 45L),
            createBench(107L, 45L),
            createBench(108L, 45L),
            createBench(109L, 45L),
            createBench(110L, 45L),
            createBench(111L, 45L),
            createBench(126L, 56L),
            createBench(127L, 56L),
            createBench(139L, 61L),
            createBench(140L, 61L),
            createBench(170L, 61L),
            createBench(144L, 64L),
            createBench(165L, 76L),
            createBench(168L, 78L),
            createBench(169L, 79L),
            createBench(173L, 80L),
            createBench(175L, 82L)
    );


    private static Bench createBench(Long id, Long machineId) {
        Machine machine =machineRepository.getMachinesById(machineId);
        Bench bench = new Bench();
        bench.setId(id);
        bench.setMachine(machine);
        benchAndMachineRepository.bindBenchAndMachine(machine,bench);
        return bench;
    }

    public List<Bench> getBenches() {
        return benches;
    }


}
