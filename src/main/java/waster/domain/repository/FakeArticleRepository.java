package waster.domain.repository;

import waster.domain.entity.Article;
import waster.domain.entity.ProcessMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeArticleRepository  {
    private static FakeProcessMapRepository fakeProcessMapRepository = new FakeProcessMapRepository();
    public static List<Article> articles = Arrays.asList(
            createArticle(0L, "00с65гл+ВОсн", "11001"),
            createArticle(1L, "00с65гл+ВОсн", "130706"),
            createArticle(2L, "00с65гл+ВОсн", "261005"),
            createArticle(3L, "08с6гл+во-у", "110701"),
            createArticle(4L, "00с65гл+ВОсн", "191862"),
            createArticle(5L, "08с6отб+гом", "10101"),
            createArticle(6L, "08с6гл+гом", "110701"),
            createArticle(7L, "08с6гл+гом", "191862"),
            createArticle(8L, "4с5гл+МВО", "261005")
    );

    private static Article createArticle(Long id, String art, String color) {

        Article article = new Article();
        article.setName(art);
        article.setColoring(color);
        article.setId(id);
        article.setProcessMap(fakeProcessMapRepository.getByArticleId(id).get());
        return article;
    }

    private static Article createArticle(Long id, String art, String color, ProcessMap processMap) {

        Article article = new Article();
        article.setName(art);
        article.setColoring(color);
        article.setId(id);
        article.setProcessMap(processMap);
        return article;
    }

    public Optional<Article> getById(Long id) {
        return articles.stream().filter(x -> x.getId().equals(id)).findFirst();
    }
}
