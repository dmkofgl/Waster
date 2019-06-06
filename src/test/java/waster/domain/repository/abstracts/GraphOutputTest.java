package waster.domain.repository.abstracts;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waster.domain.entity.Article;
import waster.domain.entity.Step;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//TODO replace this method in service class
@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphOutputTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void checkAutowired() {
        Assert.assertNotNull(articleRepository);
    }

    @Test
    public void outputInFile() {
        final Long ARTICLE_ID = 3L;
        Article article = articleRepository.findById(ARTICLE_ID).get();
        tryView(article);
    }


    private void tryView(Article article) {
        final String FILE_EXTENSION = "PNG";
        final String PATH_TO_SAVE = System.getProperty("user.dir")
                + "/src/main/resources/graphs/"
                + article.getName() + "_"
                + article.getColoring()
                + "." + FILE_EXTENSION;

        Graph graph = article.getProcessMap().getGraph();
        JGraphXAdapter<Step, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(PATH_TO_SAVE);

        try {
            ImageIO.write(image, FILE_EXTENSION, imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tryFlatMap() {
        // Creating a List of Strings
        List<String> list = Arrays.asList("Geeks", "GFG",
                "GeeksforGeeks", "gfg");

        // Using Stream flatMap(Function mapper)
        list.stream().flatMap(str ->
                Stream.of(str.toCharArray())).
                forEach(System.out::println);
    }
}
