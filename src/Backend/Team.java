package Backend;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Team implements Comparable<Team> {
    private String name;
    private String abbv;
    private Region region;
    private BufferedImage flag;
    private int rank;
    private Map<LocalDate, Integer> pointsMap;
    private boolean isFavorite;

    public Team(String name, String abbreviation, Region region, int rank) {
        this.name = name;
        this.abbv = abbreviation;
        this.region = region;
        this.rank = rank;
        pointsMap = new HashMap<>();
        isFavorite = false;
        flag = null;
    }

    public int getMostRecentScore(LocalDate currentDate) {
        LocalDate mostRecentDate = null;
        for (LocalDate date : pointsMap.keySet()) {
            if (date.isBefore(currentDate) && (mostRecentDate == null || date.isAfter(mostRecentDate))) {
                mostRecentDate = date;
            }
        }
        if (mostRecentDate == null) {
            return 0; // No previous matches found
        } else {
            return pointsMap.get(mostRecentDate);
        }
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

    public int getPoints(LocalDate date) {
        return pointsMap.get(date);
    }

    public void setPoints(LocalDate date, int points) {
        this.pointsMap.put(date, points);
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
        Team t = (Team) o;
        if (this.getRank() > t.getRank()) {
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
                ", pointsMap=" + pointsMap +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
