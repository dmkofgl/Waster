package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Article;

public interface ArticleRepository extends CrudRepository<Article,Long> {
}
