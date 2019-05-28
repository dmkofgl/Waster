package waster.domain.repository.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import waster.domain.entity.Article;
import waster.domain.entity.Order;
import waster.domain.repository.abstracts.ArticleRepository;
import waster.domain.repository.abstracts.OrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class DbInitFromFoxPro implements CommandLineRunner {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";
        Class.forName("com.caigen.sql.dbf.DBFDriver");
        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);

        copyArticles(conn);
        copyOrders(conn);
    }

    private void copyArticles(Connection connection) throws SQLException {
        String sql = "select kartatp.art as artName, kartatp.naimk_o as modifyName, kartatp.id as processMapId, NKL as coloring " +
                "from  kartatp inner join kartatp1 " +
                "where kartatp.id =kartatp1.ID_KARTA ";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(sql);


        List<Article> articles = new ArrayList<>();
        while (rs.next()) {
            String art = rs.getObject(1) + (String) rs.getObject(2);

            Article article = Article.builder()
                    .name(art)
                    .coloring((String) rs.getObject(4))
                    .build();
            articles.add(article);
        }
        articleRepository.saveAll(articles);
    }

    public void copyOrders(Connection connection) throws ClassNotFoundException, SQLException {

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " plan_pr.art," +//1
                "plan_pr.metr," +//2
                "plan_pr.dats," +//3
                "kartatp.id," +//4
                "plan_pr.nkol " +//5
                "from  plan_pr inner join kartatp " +
                "where plan_pr.art = kartatp.art + kartatp.naimk_o";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            String art = (String) rs.getObject(1);
            Double length = rs.getDouble(2);
            Date expireDate = rs.getDate(3);
            Long artId = rs.getLong(4);
            String coloring = rs.getString(5);

            Article article = articleRepository.findByColoringAndName(coloring,art).orElse(null);
            Order order = Order.builder()
                    .length(length)
                    .expireDate(expireDate)
                    .article(article)
                    .build();

            orders.add(order);
        }
        orderRepository.saveAll(orders);
    }

}
