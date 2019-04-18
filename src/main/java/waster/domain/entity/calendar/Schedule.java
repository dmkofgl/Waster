package waster.domain.entity.calendar;

public class Schedule {
    private Long start;
    private Operation operation;

    public Schedule(Operation operation) {
        this.operation = operation;
        this.start = operation.getInitialStartDate();
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return start + operation.getTime();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

}
