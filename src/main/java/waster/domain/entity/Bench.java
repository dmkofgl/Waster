package waster.domain.entity;

public class Bench {
    //номер машины
    private int id;
    private String name;
    private Machine machine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Bench) obj).getId() == this.id;
    }
}
