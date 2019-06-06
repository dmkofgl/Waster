package waster.domain.repository.abstracts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Article;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void checkAutowired() {
        Assert.assertNotNull(articleRepository);
    }

    @Test
    public void createArticle() {
        Article article = Article.builder().build();
        article = articleRepository.save(article);
        Assert.assertNotNull(article.getId());
    }

    @Test
    public void deleteArticle() {
        Article article = Article.builder().build();
        article = articleRepository.save(article);

        articleRepository.deleteById(article.getId());
        Assert.assertFalse(articleRepository.findById(article.getId()).isPresent());
    }
}
