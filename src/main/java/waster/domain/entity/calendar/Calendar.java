package waster.domain.entity.calendar;

import lombok.Getter;
import waster.domain.service.InterruptionService;
import waster.utuls.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Calendar implements Serializable {
    //TODO replace it to schedule
    private Date startDate = new Date();
    private transient InterruptionService interruptionService;

    @Getter
    private List<Schedule> schedules = new ArrayList<>();

    public Calendar(Date startDate, InterruptionService interruptionService) {
        this.startDate = startDate;
        this.interruptionService = interruptionService;
    }

    @Deprecated
    public void addSchedule(Schedule newSchedule) {
        schedules.add(newSchedule);
    }

    //TODO check if exist case when replace after interrupt when operation on this time already exists
    public Schedule applyOperation(Operation operation) {
        Long calculatedStartTime = calculateStartTimeForOperation(operation);
        operation.setInitialStartDate(calculatedStartTime);

        batchingOperationIfOverlap(operation);

        Schedule newTask = new Schedule(operation);
        schedules.add(newTask);

        return newTask;
    }

    //TODO DANGEROUS RECURSION
    private void batchingOperationIfOverlap(Operation operation) {
        Optional<Interruption> overlapInterruption = interruptionService.getFirstOverlapInterruption(operation, startDate);

        overlapInterruption.ifPresent(i -> {
            if (operation.getOrder().isAvailableBatching() &&
                    operation.getInitialStartDate() + startDate.getTime() + operation.getSetting().getPrepareTime() < i.getStart().getTime()) {
                Long availableWorkingTime = i.getStart().getTime() - operation.getInitialStartDate() - startDate.getTime();
                Double newLength = operation.calculateLengthToTime(availableWorkingTime);
                Operation oper = Operation.builder()
                        .initialStartDate(operation.getInitialStartDate())
                        .length(newLength)
                        .setting(operation.getSetting())
                        .order(operation.getOrder())
                        .build();
                applyOperation(oper);
                operation.setLength(operation.getLength() - newLength);
            }
            operation.setInitialStartDate(i.getEnd().getTime() - startDate.getTime());
            Long calculatedStartTime = calculateStartTimeForOperation(operation);
            operation.setInitialStartDate(calculatedStartTime);
            batchingOperationIfOverlap(operation);

        });

    }

    public Long calculateEndTimeForOperation(Operation operation) {
        Operation newOperation = Operation.builder()
                .initialStartDate(operation.getInitialStartDate())
                .length(operation.getLength())
                .setting(operation.getSetting())
                .build();

        Schedule newTask = new Schedule(newOperation);
        calculateInitialStartTime(newTask);
        return newTask.getEnd();
    }

    public Long calculateStartTimeForOperation(Operation operation) {
        Operation newOperation = Operation.builder()
                .initialStartDate(operation.getInitialStartDate())
                .length(operation.getLength())
                .setting(operation.getSetting())
                .build();

        Schedule newTask = new Schedule(newOperation);
        calculateInitialStartTime(newTask);
        return newTask.getStart();
    }

    private void calculateInitialStartTime(Schedule newTask) {
        Optional<Schedule> overlappedSchedule = schedules.stream()
                .filter(schedule -> DateUtils.isOverlap(schedule, newTask))
                .findFirst();
        if (overlappedSchedule.isPresent()) {
            Schedule engaged = overlappedSchedule.get();
            newTask.setStart(engaged.getEnd() + 1);
            calculateInitialStartTime(newTask);
        }
    }
    public Long lastSchedulesActionEndTime() {
        return schedules.stream()
                .map(Schedule::getEnd)
                .max(Long::compareTo)
                .orElse(0L);
    }
}
