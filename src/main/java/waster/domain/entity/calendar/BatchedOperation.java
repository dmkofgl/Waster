package waster.domain.entity.calendar;

import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Setting;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BatchedOperation implements Serializable {
    private Long length;
    private List<Operation> operations;
    private Setting setting;
}
