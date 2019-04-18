package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Setting {
    /*
   Время обработки 1 погонного метра ткани
    */
    private Long id;
    private Long workingTime;
    private Long prepareTime;
    private Boolean timeDependOLength = true;
}