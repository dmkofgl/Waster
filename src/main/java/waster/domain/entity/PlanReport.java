package waster.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanReport {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private List<Order> orders;
    private BenchScheduler benchScheduler;
    private String filePath;
}
