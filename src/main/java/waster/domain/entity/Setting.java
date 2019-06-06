package waster.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {
    @Id
    private Long id;
    // meter/minute
    private Double workingSpeed;
    @Builder.Default
    private Long prepareTime = 15 * 60 * 1000L;
    @Builder.Default
    private boolean timeDependOnLength = true;
    @ManyToOne
    private Machine machine;
}