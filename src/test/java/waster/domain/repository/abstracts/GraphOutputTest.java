package waster.domain.repository.abstracts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Article;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphOutputTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void checkAutowired() {
        Assert.assertNotNull(articleRepository);
    }
}
