package Backend;

import Frontend.GUI;

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

    private DataLoader() {
    }

    /**
     * Reads team data from the team data file.
     *
     * @return A list of Teams.
     */
    public static ArrayList<Team> loadTeamData() {// TODO: 5/4/2023 Change return type to List
        final ArrayList<Team> teams = new ArrayList<>();
        final InputStream dataFileStream = getDataInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataFileStream))) {
            createTeamsFromLinesOfText(teams, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams;
    }

    private static InputStream getDataInputStream() {
        final InputStream dataFileStream = GUI.class.getResourceAsStream(TEAM_DATA_FILE_NAME);
        return Objects.requireNonNull(dataFileStream);
    }

    private static void createTeamsFromLinesOfText(List<Team> teams, BufferedReader reader) {
        final Region[] currentTeamRegion = {null};
        final List<String> fileLines = cleanFileTextInputBeforeReading(reader.lines());
        fileLines.forEach(lineText -> {
            if (isLineTextARegionName(lineText)) currentTeamRegion[0] = Region.valueOf(lineText);
            else teams.add(createTeamFromTextLine(lineText, currentTeamRegion[0]));
        });
    }

    private static List<String> cleanFileTextInputBeforeReading(Stream<String> fileStream) {
        final Stream<String> nonEmptyFileLines = filterOutEmptyLines(fileStream);
        return trimLinesAndConvertToList(nonEmptyFileLines);
    }

    private static Team createTeamFromTextLine(String line, Region region) {
        String[] parts = line.split(DATA_DELIMITER);
        return new Team(parts[0], parts[1], region, Integer.parseInt(parts[2]));
    }

    private static Stream<String> filterOutEmptyLines(Stream<String> lines) {
        return lines.filter(s -> !s.isEmpty());
    }

    private static List<String> trimLinesAndConvertToList(Stream<String> lines) {
        return lines.map(String::trim).collect(Collectors.toList());
    }

    private static boolean isLineTextARegionName(String line) {
        return !line.contains(DATA_DELIMITER);
    }
}
