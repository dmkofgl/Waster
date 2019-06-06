package waster.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bench implements Serializable {
    //номер машины
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Machine machine;

    @Override
    public boolean equals(Object obj) {
        return ((Bench) obj).getId() == this.id;
    }
}
