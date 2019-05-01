package waster.domain.repository;

import waster.domain.entity.ProcessMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeProcessMapRepository  {
    public static List<ProcessMap> processMaps = Arrays.asList(
            createProcessMap1(),
            createProcessMap2(),
            createProcessMap3(),
            createProcessMap4(),
            createProcessMap6(),
            createProcessMap7(),
            createProcessMap8(),
            createProcessMap0(),
            createProcessMap5()
    );

    private static ProcessMap createProcessMap1() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(1L);
        processMap.addStart(0L);
        processMap.addVertex(1L);
        processMap.addVertex(2L);
        processMap.addVertex((3L));
        processMap.addVertex((4L));
        processMap.addVertex(5L);
        processMap.addVertex(6L);
        processMap.addVertex((7L));
        processMap.addVertex((8L));
        processMap.addVertex((9L));
        processMap.addVertex(10L);
        processMap.addLast((11L));

        processMap.addEdge((0L), (1L));
        processMap.addEdge((1L), (2L));
        processMap.addEdge((2L), (3L));
        processMap.addEdge((3L), (4L));
        processMap.addEdge((3L), (5L));
        processMap.addEdge((4L), (6L));
        processMap.addEdge((5L), (6L));
        processMap.addEdge((6L), (7L));
        processMap.addEdge((6L), (8L));
        processMap.addEdge((6L), (9L));
        processMap.addEdge((7L), (10L));
        processMap.addEdge((8L), (10L));
        processMap.addEdge((9L), (10L));
        processMap.addEdge((10L), (11L));

        return processMap;
    }

    private static ProcessMap createProcessMap2() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(2L);
        processMap.addStart(0L);
        processMap.addVertex(1L);
        processMap.addVertex(2L);
        processMap.addVertex((3L));
        processMap.addVertex((4L));
        processMap.addVertex(5L);
        processMap.addVertex(6L);
        processMap.addVertex((7L));
        processMap.addVertex((8L));
        processMap.addVertex((9L));
        processMap.addVertex(10L);
        processMap.addLast((11L));

        processMap.addEdge((0L), (1L));
        processMap.addEdge((1L), (2L));
        processMap.addEdge((2L), (3L));
        processMap.addEdge((3L), (4L));
        processMap.addEdge((3L), (5L));
        processMap.addEdge((4L), (6L));
        processMap.addEdge((5L), (6L));
        processMap.addEdge((6L), (7L));
        processMap.addEdge((6L), (8L));
        processMap.addEdge((6L), (9L));
        processMap.addEdge((7L), (10L));
        processMap.addEdge((8L), (10L));
        processMap.addEdge((9L), (10L));
        processMap.addEdge((10L), (11L));

        return processMap;
    }

    private static ProcessMap createProcessMap3() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(3L);
        processMap.addStart(16L);
        processMap.addVertex(17L);
        processMap.addVertex(13L);
        processMap.addVertex((14L));
        processMap.addLast((15L));

        processMap.addEdge((16L), (17L));
        processMap.addEdge((17L), (13L));
        processMap.addEdge((13L), (16L));
        processMap.addEdge((16L), (14L));
        processMap.addEdge((14L), (15L));

        return processMap;
    }

    private static ProcessMap createProcessMap4() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(4L);

        processMap.addStart(18L);
        processMap.addStart(19L);
        processMap.addStart(20L);
        processMap.addVertex(13L);
        processMap.addVertex((14L));
        processMap.addLast((15L));

        processMap.addEdge((18L), (19L));
        processMap.addEdge((19L), (20L));
        processMap.addEdge((20L), (13L));
        processMap.addEdge((13L), (14L));
        processMap.addEdge((14L), (15L));

        return processMap;
    }

    private static ProcessMap createProcessMap5() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(5L);
        processMap.addStart(18L);
        processMap.addVertex(13L);
        processMap.addVertex((14L));
        processMap.addLast((15L));

        processMap.addEdge((18L), (13L));
        processMap.addEdge((13L), (14L));
        processMap.addEdge((14L), (15L));

        return processMap;
    }
    private static ProcessMap createProcessMap6() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(6L);

        return processMap;
    }
    private static ProcessMap createProcessMap0() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(0L);

        return processMap;
    }
    private static ProcessMap createProcessMap7() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(7L);

        return processMap;
    }
    private static ProcessMap createProcessMap8() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(8L);

        return processMap;
    }

    public Optional<ProcessMap> getByArticleId(Long id) {
        return processMaps.stream().filter(x -> x.getArticleId().equals(id)).findFirst();
    }
}
