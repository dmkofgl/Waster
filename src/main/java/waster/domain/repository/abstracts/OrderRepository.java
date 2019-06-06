package waster.domain.repository.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Order;

public interface OrderRepository   extends JpaRepository<Order,Long> {
}
