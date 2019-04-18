package waster.domain.entity;

public class Article {
    private Long id;
    private String coloring;
    private String name;
    private ProcessMap processMap;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColoring() {
        return coloring;
    }

    public void setColoring(String coloring) {
        this.coloring = coloring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProcessMap getProcessMap() {
        return processMap;
    }

    public void setProcessMap(ProcessMap processMap) {
        this.processMap = processMap;
    }
}
