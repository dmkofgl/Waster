package waster.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bench {
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
