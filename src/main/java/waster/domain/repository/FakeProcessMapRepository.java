package waster.domain.repository;

import waster.domain.entity.ProcessMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class FakeProcessMapRepository {
    private static FakeStepRepository fakeStepRepository = new FakeStepRepository();
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

        addEdge(processMap,(0L), (1L));
        addEdge(processMap,(1L), (2L));
        addEdge(processMap,(2L), (3L));
        addEdge(processMap,(3L), (4L));
        addEdge(processMap,(3L), (5L));
        addEdge(processMap,(4L), (6L));
        addEdge(processMap,(5L), (6L));
        addEdge(processMap,(6L), (7L));
        addEdge(processMap,(6L), (9L));
        addEdge(processMap,(6L), (8L));
        addEdge(processMap,(7L), (10L));
        addEdge(processMap,(8L), (10L));
        addEdge(processMap,(9L), (10L));
        addEdge(processMap,(10L), (11L));


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

        addEdge(processMap,(0L), (1L));
        addEdge(processMap,(1L), (2L));
        addEdge(processMap,(2L), (3L));
        addEdge(processMap,(3L), (4L));
        addEdge(processMap,(3L), (5L));
        addEdge(processMap,(4L), (6L));
        addEdge(processMap,(5L), (6L));
        addEdge(processMap,(6L), (7L));
        addEdge(processMap,(6L), (9L));
        addEdge(processMap,(6L), (8L));
        addEdge(processMap,(7L), (10L));
        addEdge(processMap,(8L), (10L));
        addEdge(processMap,(9L), (10L));
        addEdge(processMap,(10L), (11L));

        return processMap;
    }

    private static ProcessMap createProcessMap3() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(3L);
        processMap.addStart(16L);
        processMap.addVertex(17L);
        processMap.addVertex(13L);
        processMap.addVertex(18L);
        processMap.addVertex((14L));
        processMap.addVertex((29L));
        processMap.addLast((15L));

        addEdge(processMap,(16L), (17L));
        addEdge(processMap,(17L), (18L));
        addEdge(processMap,(17L), (16L));
        addEdge(processMap,(16L), (14L));
        addEdge(processMap,(17L), (29L));
        addEdge(processMap,(29L), (14L));
        addEdge(processMap,(18L), (14L));
        addEdge(processMap,(14L), (15L));


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

        addEdge(processMap,(18L), (19L));
        addEdge(processMap,(19L), (20L));
        addEdge(processMap,(20L), (13L));
        addEdge(processMap,(13L), (14L));
        addEdge(processMap,(14L), (15L));

        return processMap;
    }

    private static ProcessMap createProcessMap5() {
        ProcessMap processMap = new ProcessMap();
        processMap.setArticleId(5L);
        processMap.addStart(18L);
        processMap.addVertex(13L);
        processMap.addVertex((14L));
        processMap.addLast((15L));

        addEdge(processMap,(18L), (13L));
        addEdge(processMap,(13L), (14L));
        addEdge(processMap,(14L), (15L));

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

    private static void addEdge(ProcessMap processMap, Long source, Long target) {
        processMap.addEdge(source, target, fakeStepRepository.getById(source).getSetting().getWorkingSpeed());
    }
}
