package waster.domain.service;

import waster.domain.entity.calendar.Interruption;
import waster.domain.entity.calendar.Operation;

import java.util.Date;
import java.util.List;

public interface InterruptionService {
    List<Interruption> getAllOverlapInteraptions(Operation operation, Date startDate);
}
