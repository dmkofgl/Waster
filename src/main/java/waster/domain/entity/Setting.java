package waster.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    @Id
    private Long id;
    private Double workingSpeed;
    @Builder.Default
    private Long prepareTime = 15 * 60 * 1000L;
    private boolean timeDependOnLength = true;
    @ManyToOne
    private Machine machine;

}