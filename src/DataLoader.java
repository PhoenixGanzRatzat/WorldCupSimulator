import Backend.Region;
import Backend.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {
    private static final String TEAM_DATA_FILE_NAME = "teams.txt";
    private static final String DATA_DELIMITER = ",";

    /**
     * Reads team data from the team data file.
     *
     * @return A list of Teams.
     */
    public List<Team> loadTeamData() {
        List<Team> teams = new ArrayList<>();
        InputStream stream = Objects.requireNonNull(WorldCupSimulator.class.getResourceAsStream(TEAM_DATA_FILE_NAME));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            final Region[] region = {null};
            List<String> trimmedLines = removeEmptyLinesAndSpaces(reader.lines());
            trimmedLines.forEach(line -> {
                if (isLineARegionName(line)) region[0] = Region.valueOf(line);
                else teams.add(createTeamFromLine(line, region[0]));
            });
        } catch (IOException e) {
            System.out.println("Error loading team data.");
            e.printStackTrace();
        }

        return teams;
    }

    private Team createTeamFromLine(String line, Region region) {
        String[] parts = line.split(DATA_DELIMITER);
        System.out.println(parts[0]+" "+parts[1]+" "+region+" "+Integer.parseInt(parts[2]));
        return new Team(parts[0], parts[1], region, Integer.parseInt(parts[2]));
    }

    private List<String> removeEmptyLinesAndSpaces(Stream<String> reader) {
        return reader.filter(line -> !line.isEmpty()).map(String::trim).collect(Collectors.toList());
    }

    private boolean isLineARegionName(String line) {
        return !line.contains(DATA_DELIMITER);
    }
}
