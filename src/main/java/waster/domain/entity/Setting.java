package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Setting {

    @Id
    private Long id;
    private Double workingSpeed;
    private Long prepareTime;
    private boolean timeDependOnLength = true;
}