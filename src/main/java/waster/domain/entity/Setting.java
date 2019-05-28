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
    private Long prepareTime;
    private boolean timeDependOnLength = true;
    @ManyToOne
    private Machine machine;
}