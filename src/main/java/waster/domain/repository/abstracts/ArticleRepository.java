package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Article;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    public Optional<Article> findByColoringAndName(String coloring, String name);
}
