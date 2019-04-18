package waster.domain.repository;

import waster.domain.entity.Machine;
import waster.domain.entity.Setting;
import waster.domain.repository.abstracts.MachineRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FakeMachineRepository implements MachineRepository {
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();

    private static List<Machine> machines = Arrays.asList(
            createMachine(-1L, null, "ИСТОК", null),
            createMachine(-2L, null, "ВТОК", null),
            createMachine(82L, 157L, "CIBITEX", null),
            createMachine(20L, 5L, "ЛОО БЕНИНГЕР", null),
            createMachine(21L, 92L, "ПНМ ВАКАЯМА", null),
            createMachine(79L, null, null, null),
            createMachine(78L, 156L, null, null),
            createMachine(76L, 131L, null, null),
            createMachine(61L, 41L, null, null),
            createMachine(10L, 13L, null, null),
            createMachine(44L, 117L, null, null),
            createMachine(64L, 77L, null, null),
            createMachine(56L, 22L, null, null),
            createMachine(45L, null, null, null),
            createMachine(14L, 76L, null, null),
            createMachine(30L, 116L, null, null),
            createMachine(80L, null, null, null),
            createMachine(22L, null, null, null)
    );

    //TODO USE AOP
    private static Machine createMachine(Long numberNew, Long numberOld, String name, List<Setting> settings) {
        Machine machine = new Machine();
        machine.setNumberNew(numberNew);
        machine.setNumberOld(numberOld);
        machine.setName(name);
        machine.setSetting(settings);
      //  benchAndMachineRepository.addMachine(machine);
        return machine;
    }

    public List<Machine> getMachines() {
        machines.sort(Comparator.comparingLong(Machine::getNumberNew));
        return machines;
    }

    public Machine getMachinesById(Long id) {
        return machines.stream().filter(x -> x.getNumberNew().equals(id)).findFirst().get();
    }
}
