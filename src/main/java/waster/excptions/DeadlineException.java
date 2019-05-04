package waster.excptions;

import lombok.Getter;
import waster.domain.entity.Bench;

public class DeadlineException extends RuntimeException {
    @Getter
    private Bench bench;

    public DeadlineException(Bench bench) {
        this.bench = bench;
    }
}
