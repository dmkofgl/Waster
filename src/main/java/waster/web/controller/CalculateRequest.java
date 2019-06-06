package waster.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequest {
    private Long timeCountInHour;
    private Date startDate;
    private List<Long> ordersId;
    private String reportTitle;
    private boolean optimal;

}