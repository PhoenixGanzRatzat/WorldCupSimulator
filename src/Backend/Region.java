package Backend;

public enum Region {
    AFC("AFC"),
    CAF("CAF"),
    CONCACAF("CONCACAF"),
    CONMEBOL("CONMEBOL"),
    OFC("OFC"),
    UEFA("UEFA");

    private String name;

    public String getName() {
        return name;
    }

    Region(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
