package waster.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"numberNew"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Serializable {
    @Id
    private Long numberNew;
    private Long numberOld;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Setting> setting;

}
