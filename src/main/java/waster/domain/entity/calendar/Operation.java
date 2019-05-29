package waster.domain.entity.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Setting;

@Builder
@Getter
@Setter
public class Operation {
    private Double length;
    private Long initialStartDate;
    private Setting setting;

    public Long getTime() {
        Long result;
        if (setting.isTimeDependOnLength()) {
            result = Double.valueOf(length / setting.getWorkingSpeed()).longValue() + setting.getPrepareTime();
        } else {
            result = setting.getWorkingSpeed().longValue() + setting.getPrepareTime();
        }
        return result;
    }

    public Long getEndTime() {
        return initialStartDate + getTime();
    }


}
