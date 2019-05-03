package waster.domain.service.impl;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import waster.domain.entity.ProcessMap;
import waster.domain.service.ProcessMapService;

import java.util.List;

public class ProcessMapServiceImpl implements ProcessMapService {
    @Override
    public List<Long> getShortestPath(ProcessMap processMap) {
        final Long START_VERTEX = -1L;
        final Long END_VERTEX = -2L;
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(processMap.getGraph());
        List<Long> shortestPath = dijkstraShortestPath.getPath(START_VERTEX, END_VERTEX).getVertexList();
        return shortestPath;
    }
}
