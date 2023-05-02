package Backend;

import java.awt.image.BufferedImage;

public class Team implements Comparable<Team>{
    private String name;
    private String abbv;
    private Region region;
    private BufferedImage flag;
    private int rank;
    private int points;
    private int qualifierPoints;
    private boolean isFavorite;
    public Team(String name, String abreviation, Region region, int rank){
        this.name = name;
        this.abbv = abreviation;
        this.region = region;
        this.rank = rank;
        qualifierPoints = 0;
        isFavorite = false;
        flag = null;
    }


    public String getName() {
        return name;
    }

    public String getAbbv() {
        return abbv;
    }

    public Region getRegion() {
        return region;
    }

    public int getRank() {
        return rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getQualifierPoints() {
        return qualifierPoints;
    }

    public void setQualifierPoints(int qualifierPoints) {
        this.qualifierPoints = qualifierPoints;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public BufferedImage getFlag() {
        return flag;
    }

    @Override
    public int compareTo(Team o) {
        Team t = (Team)o;
        if (this.getRank() > t.getRank()){
            return 1;
        } else if (this.getRank() == t.getRank()) {
            return 0;
        } else if (this.getRank() < t.getRank()) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", abbv='" + abbv + '\'' +
                ", region=" + region +
                ", rank=" + rank +
                ", points=" + points +
                ", qualifierPoints=" + qualifierPoints +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
