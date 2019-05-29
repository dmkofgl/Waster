package waster.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import waster.domain.entity.Article;
import waster.domain.entity.Order;
import waster.domain.repository.abstracts.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class OrdersController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/api/orders")
    public List<OrderPojo> getOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderPojo> orderPojos = new ArrayList<>();
        for (Order order : orders) {
            Long id = order.getId();
            Date date = order.getExpireDate();
            Article article = order.getArticle();
            Double length = order.getLength();
            String art = "";
            String color = "";
            if (article != null) {
                art = article.getName();
                color = article.getColoring();
            }
            OrderPojo orderPojo = new OrderPojo(id, date, art, color, length);
            orderPojos.add(orderPojo);
        }
        return orderPojos;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class OrderPojo {
        private Long id;
        private Date expireDate;
        private String article;
        private String color;
        private Double length;

    }

}
