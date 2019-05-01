package waster.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Step {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Machine machine;
    @ManyToOne
    private Setting setting;
    private String name;

    @Override
    public String toString() {
        return "{" +
                "m=" + machine.getNumberNew() +
                " " +
                "s=" + setting.getId() +
                '}';
    }
}
