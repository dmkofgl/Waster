package waster.domain.repository;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import waster.domain.entity.Bench;
import waster.domain.entity.Machine;

public class BenchAndMachineRepository {
    private static MultiValueMap<Machine, Bench> bindMap = new LinkedMultiValueMap<>();

    public void bindBenchAndMachine(Machine machine, Bench bench) {
        bindMap.add(machine, bench);
    }

    public MultiValueMap<Machine, Bench> getBindMap() {
        return bindMap;
    }

    public class Entry {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class PhoneDirectory {
        private Entry[] entries;

        public Entry searchEntry(String name) {
            for (Entry entry : entries) {
                if (entry.getName().equals(name)) {
                    return entry;
                }
            }
            throw new RuntimeException("There is no entry with name: "+name);
        }
    }
}

