package waster.domain.helper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import waster.domain.entity.Order;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "orderList")
public class MapListKey {
    private List<Order> orderList;
}
