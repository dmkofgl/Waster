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
@EqualsAndHashCode(of={"id"})
public class Bench implements Serializable {
    //номер машины
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Machine machine;
}
