package waster.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"numberNew"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Serializable {
    @Id
    private Long numberNew;
    private Long numberOld;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Setting> setting;
    @OneToMany(fetch = FetchType.EAGER)
    @Singular("kpvList")
    private transient List<KPV> kpvList = new ArrayList<>();

    public void addKPV(KPV kpv) {
        kpvList.add(kpv);

    }
}
