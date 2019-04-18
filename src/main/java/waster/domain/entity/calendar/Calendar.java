package waster.domain.entity.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Calendar {
    public static Date START_DATE = new Date();
    private List<Schedule> schedule = new ArrayList<>();

    public void addOperation(Operation operation) {
        Schedule newTask = applyOperation(operation);
        schedule.add(newTask);
    }

    @Deprecated
    public void addSchedule(Schedule newSchedule) {
        schedule.add(newSchedule);
    }

    public Schedule applyOperation(Operation operation) {
        Schedule newTask = new Schedule(operation);
        reflexiveFindTime(newTask);
        schedule.add(newTask);
        return newTask;
    }

    private void reflexiveFindTime(Schedule newTask) {
        Optional<Schedule> optionalShedule = schedule.stream()
                .filter(schedule -> engagedInitial(schedule, newTask))
                .findFirst();
        if (optionalShedule.isPresent()) {
            Schedule engaged = optionalShedule.get();
            newTask.setStart(engaged.getEnd());
            reflexiveFindTime(newTask);
        }
    }


    //TODO проверить , если начинается раньше и заканчивается раньше end но позже start
    private boolean engagedInitial(Schedule existOperation, Schedule newTask) {
        Long startA = existOperation.getStart();
        Long endA = existOperation.getStart();
        Long startB = newTask.getStart();
        Long endB = newTask.getStart();

        return (startA > startB ? startA : startB) <= (endA < endB ? endA : endB);
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public Long lastActionEndTime() {
        return schedule.stream().map(Schedule::getEnd).max(Long::compareTo).orElse(0L);
    }
}
