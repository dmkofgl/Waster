package waster.domain.entity.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Order;
import waster.domain.entity.Setting;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class Operation implements Serializable {
    private Double length;
    private Long initialStartDate;
    private Setting setting;
    private Operation prevOperation;
    private Order order;

    public Long getTime() {
        Long result;
        if (setting.isTimeDependOnLength()) {
            result = Double.valueOf(length / setting.getWorkingSpeed()).longValue();
        } else {
            result = setting.getWorkingSpeed().longValue();
        }
        return convertMinuteToMillisecond(result) + setting.getPrepareTime();
    }

    public Double calculateLengthToTime(Long startTime) {
        Long timeWithoutPrepare = startTime - setting.getPrepareTime();
        Double result = timeWithoutPrepare * convertMeterPerMinuteToMillisecond(setting.getWorkingSpeed());
        return result;
    }

    private Long convertMinuteToMillisecond(Long time) {
        return time * 60 * 1000;
    }

    private Double convertMeterPerMinuteToMillisecond(Double speed) {
        Double result = speed / (60 * 1000);

        return result;
    }


    public Long getEndTime() {
        return initialStartDate + getTime();
    }


}
