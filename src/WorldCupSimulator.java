import Backend.Region;
import Backend.Team;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class WorldCupSimulator {


    private static final String TEAM_DATA_FILE_NAME = "teams.txt";
    private static final String DATA_DELIMITER = ",";
    private static List<Team> teams;

    private static void startProgram() {
        teams = loadTeamData();
    }

    /**
     * Reads team data from the team data file.
     *
     * @return A list of Teams.
     */
    public static List<Team> loadTeamData() {
        List<Team> teams = new ArrayList<>();
        InputStream stream = Objects.requireNonNull(WorldCupSimulator.class.getResourceAsStream(TEAM_DATA_FILE_NAME));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            Region currentRegion = null;

            while ((line = reader.readLine()) != null) {
                /* Skip empty lines. */
                if (line.isEmpty()) {
                    continue;
                }

                line = line.trim();

                /* If the line doesn't contain a comma separating parts of data, then assume
                * the line contains a region name instead and change the current region to that region. */
                if (!line.contains(DATA_DELIMITER)) {
                    currentRegion = Region.valueOf(line);
                    continue;
                }

                /* Construct a new Team from read data and add it to the list to be returned from
                * the method. */
                String[] parts = line.split(DATA_DELIMITER);
                teams.add(new Team(parts[0], parts[1], currentRegion, Integer.parseInt(parts[2])));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return teams;
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void main(String[] args) {
        startProgram();
    }
}