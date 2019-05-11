package waster.domain.repository;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import waster.domain.entity.Bench;
import waster.domain.entity.Machine;

public class BenchAndMachineRepository {
    private static MultiValueMap<Machine, Bench> bindMap = new LinkedMultiValueMap<>();

    public static void bindBenchAndMachine(Machine machine, Bench bench) {
        bindMap.add(machine, bench);
    }

    public MultiValueMap<Machine, Bench> getBindMap() {
        return bindMap;
    }

}

