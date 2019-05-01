package waster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import waster.domain.repository.abstracts.ArticleRepository;

@SpringBootApplication
public class SpringServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringServerApplication.class, args);
    }
}
