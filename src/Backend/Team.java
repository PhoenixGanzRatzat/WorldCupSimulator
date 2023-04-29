package Backend;

import java.awt.image.BufferedImage;

public class Team implements Comparable{
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
    public int compareTo(Object o) {
        Team t = (Team)o;
        if (this.getPoints() > t.getPoints()){
            return 1;
        } else if (this.getPoints() == t.getPoints()) {
            return 0;
        } else if (this.getPoints() < t.getPoints()) {
            return -1;
        }
        return 0;
    }
}
