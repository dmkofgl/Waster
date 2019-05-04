package waster.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    private Long id;
    private String coloring;
    private String name;
    @OneToOne
    private ProcessMap processMap;

}
