package waster.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    private Long id;
    private Machine machine;
    private Setting setting;
    private String name;

    public Machine getMachine() {
        return machine;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "m=" + machine.getNumberNew()+
                " " +
                "s=" + setting.getId() +
                '}';
    }
}
