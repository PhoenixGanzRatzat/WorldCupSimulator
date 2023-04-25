import Backend.Region;
import Backend.Team;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WorldCupSimulator {


    private static final String TEAM_DATA_FILE_NAME = "team_data.txt";
    private static final String DATA_DELIMITER = ",";

    private static void startProgram() {
        loadTeamData();
    }

    /**
     * Reads team data from the team data file.
     *
     * @return A list of Teams.
     */
    public static Collection<Team> loadTeamData() {
        List<Team> teams = new ArrayList<>();
        InputStream stream = Objects.requireNonNull(WorldCupSimulator.class.getResourceAsStream(TEAM_DATA_FILE_NAME));

        /* Read and parse all data from the data file. */
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DATA_DELIMITER);
                // TODO: 4/25/2023 Make sure constructor parameters are in right order.
                teams.add(new Team(parts[0], parts[1], Region.valueOf(parts[2].toUpperCase()), Integer.parseInt(parts[3])));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return teams;
    }

    public static void main(String[] args) {
        startProgram();
    }
}