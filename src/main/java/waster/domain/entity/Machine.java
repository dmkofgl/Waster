package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Getter
@Setter
public class Machine {
    @Id
    @GeneratedValue
    private Long numberNew;
    private Long numberOld;
    private String name;
    @OneToMany
    private List<Setting> setting;

    @Override
    public boolean equals(Object obj) {
        return ((Machine) obj).getNumberNew().equals(this.numberNew);
    }
}
