package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import waster.domain.helper.GraphConverter;
import waster.domain.repository.FakeStepRepository;

import javax.persistence.*;
import java.io.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ProcessMap {
    private static FakeStepRepository fakeStepRepository = new FakeStepRepository();
    @Id
    private Long articleId;

    @Lob
    @Convert(converter = GraphConverter.class)
    private Graph<Long, DefaultWeightedEdge> graph;

    public ProcessMap() {

        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        addVertex(-1L);
        addVertex(-2L);
    }
    private void initGraph(){

    }

    public void addStart(Long id) {
        addVertex(-1L);
        addVertex(id);
        addEdge(-1L, id);
    }

    public void addLast(Long id) {
        addVertex(-2L);
        addVertex(id);
        addEdge(id, -2L);
    }

    public void addVertex(Long stepId) {

        graph.addVertex(stepId);
    }

    public void addEdge(Long stepSourceId, Long stepTargetId) {
        graph.addEdge(stepSourceId, stepTargetId);
        graph.setEdgeWeight(stepSourceId, stepTargetId, fakeStepRepository.getById(stepSourceId).getSetting().getWorkingTime());
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public List<Step> getShortestPath() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Step> shortestPath = dijkstraShortestPath.getPath(fakeStepRepository.getById(-1L), fakeStepRepository.getById(-2L)).getVertexList();
        return shortestPath;
    }

}
