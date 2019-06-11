package waster.domain.entity.calendar;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.KPV;
import waster.domain.entity.Order;
import waster.domain.entity.Setting;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class Operation implements Serializable {
    private Double length;
    private Long initialStartDate;
    private Setting setting;
    private Operation prevOperation;
    @OneToMany(fetch = FetchType.EAGER)
    private Order order;

    public Long getTime() {
        Double result;
        if (setting.isTimeDependOnLength()) {
            result = length / setting.getWorkingSpeed();
        } else {
            result = setting.getWorkingSpeed();
        }
        Double kpv;
        try {
            kpv = setting.getMachine().getKpvList().stream()
                    .filter(this::isSuit)
                    .findFirst()
                    .map(KPV::getRate)
                    .orElse(0.8);
        } catch (Exception e) {
            kpv = 0.8;
        }
        result = convertMinuteToMillisecond(result) / kpv;
        return Double.valueOf(result + setting.getPrepareTime()).longValue();
    }

    private boolean isSuit(KPV kpv) {
        Double speed = setting.getWorkingSpeed();
        return speed >= kpv.getMinSpeed() && speed <= kpv.getMaxSpeed();
    }

    public Double calculateLengthToTime(Long startTime) {
        Long timeWithoutPrepare = startTime - setting.getPrepareTime();
        Double result = timeWithoutPrepare * convertMeterPerMinuteToMillisecond(setting.getWorkingSpeed());
        return result;
    }

    private Double convertMinuteToMillisecond(Double time) {
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
