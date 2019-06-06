package waster.domain.entity;

import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import waster.domain.helper.converter.MultivalueMapConverter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessMap implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processMapId;
    private Long articleId;
    @Convert(converter = MultivalueMapConverter.class)
    @Builder.Default
    private MultiValueMap<Long, Setting> path = new LinkedMultiValueMap<>();
}
