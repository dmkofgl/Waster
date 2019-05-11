package waster.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "fabric_order")
@EqualsAndHashCode(of = {"id"})
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Article article;
    private Double length;
    private Date date;
}
