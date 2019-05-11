package waster.domain.service.impl;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import waster.domain.entity.ProcessMap;
import waster.domain.service.ProcessMapService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessMapServiceImpl implements ProcessMapService {
    @Override
    public List<Long> getShortestPath(ProcessMap processMap) {
        final Long START_VERTEX = -1L;
        final Long END_VERTEX = -2L;
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(processMap.getGraph());

        return dijkstraShortestPath.getPath(START_VERTEX, END_VERTEX).getVertexList();
    }

    @Override
    public List<List<Long>> getAllPath(ProcessMap processMap) {
        final Long START_VERTEX = -1L;
        final Long END_VERTEX = -2L;
        AllDirectedPaths<Long, DefaultWeightedEdge> allDirectedPaths = new AllDirectedPaths<>(processMap.getGraph());
        List<GraphPath<Long, DefaultWeightedEdge>> graphPaths = allDirectedPaths.getAllPaths(START_VERTEX, END_VERTEX, true, Integer.MAX_VALUE);

        return graphPaths.stream().map(GraphPath::getVertexList).collect(Collectors.toList());
    }

}
