package waster.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"numberNew"})
public class Machine {
    @Id
    private Long numberNew;
    private Long numberOld;
    private String name;
    @OneToMany
    private List<Setting> setting;

}
