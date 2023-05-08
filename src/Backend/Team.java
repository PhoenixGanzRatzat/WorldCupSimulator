package Backend;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Team {
    private String name;
    private String abbreviation;
    private Region region;
    private BufferedImage flag;
    private int rank;
    private Map<LocalDate, Integer> dateToPointsMap;
    private boolean isFavorite;

    public Team(String name, String abbreviation, Region region, int rank) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.region = region;
        this.rank = rank;
        dateToPointsMap = new HashMap<>();
        isFavorite = false;
        flag = null;
    }

    public int getMostRecentScore() {
        final Optional<LocalDate> mostRecentDateOptional = dateToPointsMap.keySet().stream().reduce(this::getMostRecentDate);
        return mostRecentDateOptional.isPresent() ? dateToPointsMap.get(mostRecentDateOptional.get()) : 0;
    }

    private LocalDate getMostRecentDate(LocalDate firstDate, LocalDate secondDate) {
        return firstDate.isAfter(secondDate) ? firstDate : secondDate;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Region getRegion() {
        return region;
    }

    public int getRank() {
        return rank;
    }

    public int getScoreOnDate(LocalDate date) {
        return dateToPointsMap.get(date);
    }
    public int getPoints(LocalDate date) {
        LocalDate closestBefore = null;
        int closestDaysBefore = Integer.MAX_VALUE;

        for (LocalDate localDate : pointsToDateMap.keySet()) {
            int daysBefore = localDate.until(date).getDays();
            if (daysBefore < closestDaysBefore) {
                closestDaysBefore = daysBefore;
                closestBefore = localDate;
            }
        }
        if (closestBefore != null) {
            return pointsToDateMap.get(closestBefore);
        }
        return 0;
    }

    public void setPointsOnDate(LocalDate date, int points) {
        this.dateToPointsMap.put(date, points);
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public BufferedImage getFlag() {
        return flag;
    }

    public int compareRanks(Team other) {
        return Integer.compare(rank, other.rank);
    }


    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", abbv='" + abbreviation + '\'' +
                ", region=" + region +
                ", rank=" + rank +
                ", pointsMap=" + dateToPointsMap +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
