package waster.domain.entity.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Setting;
import waster.domain.entity.Step;

@Builder
@Getter
@Setter
public class Operation {
    private Double length;
    private Long initialStartDate;
    private Step step;

    public Long getTime() {
        Long result = 0L;
        Setting setting = step.getSetting();
        if (setting.getTimeDependOLength()) {
            result = Double.valueOf(setting.getWorkingTime() * length + setting.getPrepareTime()).longValue();
        } else {
            result = Double.valueOf(setting.getWorkingTime() + setting.getPrepareTime()).longValue();
        }
        return result;
    }


}
