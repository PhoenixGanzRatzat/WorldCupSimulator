package Backend;

public enum Region {
    AFC("AFC"),
    CAF("CAF"),
    CONCACAF("CONCACAF"),
    CONMEBOL("CONMEBOL"),
    OFC("OFC"),
    UEFA("UEFA"),
    UNNAMED_REGION("UNNAMED REGIONS");

    private String name;

    Region(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
