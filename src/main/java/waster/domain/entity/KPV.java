package waster.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KPV  {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Machine machine;
    private Double minSpeed;
    private Double maxSpeed;
    private Double rate;

}
