package waster.domain.entity;

import java.util.List;

public class Machine {
    private Long numberNew;
    private Long numberOld;
    private String name;

    private List<Setting> setting;

    public Long getNumberNew() {
        return numberNew;
    }

    public void setNumberNew(Long numberNew) {
        this.numberNew = numberNew;
    }

    public Long getNumberOld() {
        return numberOld;
    }

    public void setNumberOld(Long numberOld) {
        this.numberOld = numberOld;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Setting> getSetting() {
        return setting;
    }

    public void setSetting(List<Setting> setting) {
        this.setting = setting;
    }


    @Override
    public boolean equals(Object obj) {
        return ((Machine) obj).getNumberNew().equals(this.numberNew);
    }
}
