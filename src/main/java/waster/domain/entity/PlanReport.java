package waster.domain.entity;

import lombok.*;
import waster.domain.helper.BenchSchedulerConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Order> orders;
    @Convert(converter = BenchSchedulerConverter.class)
    private BenchScheduler benchScheduler;
    private String filePath;
    private String reportTitle;
    private ReportState state;
    private Date createDate;
    private Date endDate;
}
