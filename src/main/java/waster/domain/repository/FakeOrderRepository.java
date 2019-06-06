package waster.domain.repository;

import lombok.Getter;
import waster.domain.entity.Order;

import java.util.Arrays;
import java.util.List;

public class FakeOrderRepository {
    private static FakeArticleRepository fakeArticleRepository = new FakeArticleRepository();
    @Getter
    private static List<Order> orders = Arrays.asList(
//            createOrder(0L, 1000.0),
            createOrder(1L, 1000.0),
            createOrder(2L, 1000.0),
            createOrder(3L, 1000.0),
            createOrder(4L, 1000.0)
//            createOrder(5L, 1000.0),
//            createOrder(6L, 1000.0),
//            createOrder(7L, 1000.0),
//            createOrder(8L, 1000.0)
    );

    private static Order createOrder(Long articleId, Double length) {
        Order order = new Order();

        order.setLength(length);
        order.setArticle(fakeArticleRepository.getById(articleId).get());
        return order;
    }
}
