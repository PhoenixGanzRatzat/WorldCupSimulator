package Backend;

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
    private static final String TEAM_DATA_FILE_NAME = "../teams.txt";
    private static final String DATA_DELIMITER = ",";

    /**
     * Reads team data from the team data file.
     *
     * @return A list of Teams.
     */
    public static List<Team> loadTeamData() {
        final List<Team> teams = new ArrayList<>();
        final InputStream dataFileStream = WorldCupSimulator.class.getResourceAsStream(TEAM_DATA_FILE_NAME);

        Objects.requireNonNull(dataFileStream);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataFileStream))) {
            final Region[] currentTeamRegion = {null};
            final List<String> fileTextLines = filterOutEmptyLinesThenTrim(reader.lines());

            fileTextLines.forEach(lineText -> {
                if (isLineTextARegionName(lineText)) {
                    currentTeamRegion[0] = Region.valueOf(lineText);
                } else {
                    Team team = createTeamFromLineText(lineText, currentTeamRegion[0]);
                    teams.add(team);
                }
            });
        } catch (IOException e) {
            System.out.println("Error loading team data.");
            e.printStackTrace();
        }

        return teams;
    }

    private static Team createTeamFromLineText(String line, Region region) {
        String[] parts = line.split(DATA_DELIMITER);
        return new Team(parts[0], parts[1], region, Integer.parseInt(parts[2]));
    }

    private static List<String> filterOutEmptyLinesThenTrim(Stream<String> reader) {
        return reader
                .filter(line -> !line.isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static  boolean isLineTextARegionName(String line) {
        return !line.contains(DATA_DELIMITER);
    }
}
