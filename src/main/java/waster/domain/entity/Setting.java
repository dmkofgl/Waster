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
    /*
   Время обработки 1 погонного метра ткани
    */
    @Id
    @GeneratedValue
    private Long id;
    private Long workingTime;
    private Long prepareTime;
    private Boolean timeDependOLength = true;
}