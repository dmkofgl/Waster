package waster.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import waster.domain.helper.GraphConverter;
import waster.domain.repository.FakeStepRepository;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

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
}
