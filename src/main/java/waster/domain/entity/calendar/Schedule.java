package waster.domain.entity.calendar;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Schedule implements Serializable {
    private Long start;
    private Operation operation;

    public Schedule(Operation operation) {
        this.operation = operation;
        this.start = operation.getInitialStartDate();
    }

    public Long getEnd() {
        return start + operation.getTime();
    }
}
