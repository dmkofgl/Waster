package waster.domain.entity.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Setting;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class Operation implements Serializable {
    private Double length;
    private Long initialStartDate;
    private Setting setting;

    public Long getTime() {
        Long result;
        if (setting.isTimeDependOnLength()) {
            result = Double.valueOf(length / setting.getWorkingSpeed()).longValue();
        } else {
            result = setting.getWorkingSpeed().longValue();
        }
        return result*60*1000+setting.getPrepareTime();
    }

    public Long getEndTime() {
        return initialStartDate + getTime();
    }


}
