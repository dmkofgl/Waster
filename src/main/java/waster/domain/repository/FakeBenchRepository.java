package waster.domain.repository;

import waster.domain.entity.Bench;
import waster.domain.entity.Machine;
import waster.domain.repository.abstracts.BenchRepository;

import java.util.Arrays;
import java.util.List;

public class FakeBenchRepository implements BenchRepository {
    private static FakeMachineRepository machineRepository = new FakeMachineRepository();
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();
    private static List<Bench> benches = Arrays.asList(
            createBench(-1, -1L),
            createBench(-2, -2L),
            createBench(10, 10L),
            createBench(13, 10L),
            createBench(14, 10L),
            createBench(80, 10L),
            createBench(81, 10L),
            createBench(157, 10L),
            createBench(166, 10L),
            createBench(16, 14L),
            createBench(34, 14L),
            createBench(149, 14L),
            createBench(151, 14L),
            createBench(152, 14L),
            createBench(40, 20L),
            createBench(41, 21L),
            createBench(63, 30L),
            createBench(86, 44L),
            createBench(87, 45L),
            createBench(88, 45L),
            createBench(89, 45L),
            createBench(90, 45L),
            createBench(91, 45L),
            createBench(92, 45L),
            createBench(93, 45L),
            createBench(94, 45L),
            createBench(95, 45L),
            createBench(96, 45L),
            createBench(97, 45L),
            createBench(99, 45L),
            createBench(98, 45L),
            createBench(100, 45L),
            createBench(101, 45L),
            createBench(102, 45L),
            createBench(103, 45l),
            createBench(104, 45L),
            createBench(105, 45L),
            createBench(106, 45L),
            createBench(107, 45L),
            createBench(108, 45L),
            createBench(109, 45L),
            createBench(110, 45L),
            createBench(111, 45L),
            createBench(126, 56L),
            createBench(127, 56L),
            createBench(139, 61L),
            createBench(140, 61L),
            createBench(170, 61L),
            createBench(144, 64L),
            createBench(165, 76L),
            createBench(168, 78L),
            createBench(169, 79L),
            createBench(173, 80L),
            createBench(175, 82L)
    );


    private static Bench createBench(Integer id, Long machineId) {
        Machine machine =machineRepository.getMachinesById(machineId);
        Bench bench = new Bench();
        bench.setId(id);
        bench.setMachine(machine);
        benchAndMachineRepository.bindBenchAndMachine(machine,bench);
        return bench;
    }

    @Override
    public List<Bench> getBenches() {
        return benches;
    }


}
