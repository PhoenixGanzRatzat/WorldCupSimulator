package Frontend;

import Backend.Match;
import Backend.Team;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * Creates a display window for the results of all 8 groups in the group stage. As well as the functionality
 * to navigate through the group stage in various ways: match by match in 1 group, all groups, or complete
 * all matches with 1 click.
 * @author Chris Peters
 */
public class GroupPanel extends JPanel implements StagePanel, ActionListener {
    /* __ FIELD VARIABLES __ */
    /* Label at top of GroupPanel window that displays the current round for selected group */
    private JLabel infoPanelRoundNumberLabel;
    /* Label at top of GroupPanel window that display the selected group */
    private JLabel infoPanelGroupLabel;
    /* The current round for each group. GroupNum = index + 1 */
    private int[] currentRound;
    /* All matches played in the group stage */
    private List<Match> matches;
    /* All teams participating in the group stage */
    private List<Team> teams;
    /* retrieve matches played in a group for a given group number */
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    /* retrieve List of teams in a group for a given group number */
    private HashMap<Integer, ArrayList<Team>> groupTeams;
    /* retrieve the group number for a given team */
    private HashMap<Team, Integer> teamGroups;
    /* retrieve a countries flag for a given country-abbreviation */
    private HashMap<String, Image> flags;
    /* The current selected group, used to display data and influence function calls */
    private String selectedGroup;
    /* Displays the matches played up to the current round with Teams, Scores, and results */
    private JPanel resultsPanel;
    /* Container for all the group panels */
    private JPanel groupDisplayPanel;
    /* Tracker for groups that have completed displaying all of their matches*/
    private boolean[] groupsThatAreComplete;
    private boolean initialized;
    private Color themeColor;

    /* TODO:
       Todo:
     */

    /* __ CONSTRUCTORS __ */
    public GroupPanel() {
        this.setLayout(new BorderLayout());
        teamGroups = new HashMap<>();
        groupMatches = new HashMap<>();
        groupTeams = new HashMap<>();
        infoPanelGroupLabel = new JLabel("A");
        infoPanelRoundNumberLabel = new JLabel("0");
        groupMatches.put(1, new ArrayList<>());
        initialized = false;
        currentRound = new int[8];
        groupDisplayPanel = new JPanel();
        selectedGroup = "A";
        groupsThatAreComplete = new boolean[]{false, false, false, false, false, false, false, false};
        resultsPanel = new JPanel();
        flags = new HashMap<>();
        initPanel();
    }

    public GroupPanel(List<Match> matches, List<Team> teams) {
        this();
        this.matches = matches;
        this.teams = teams;
        try {
            initFlags();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createGroups();
        initGroupPanelsWithTeams();
    }


    /* __ FUNCTIONS __ */
    /* GROUP PANEL FUNCTIONS */
    /**
     * Create a panel to hold group data including
     * a select this group button
     * a row of titles for group data
     * { position, country name, win, draw, loss, points }
     * and 4 rows for each country with this data filled in
     *
     * @param groupNumber - used to get group letter
     * @return - constructed empty group panel
     */
    private JPanel createGroupPanel(int groupNumber) {
        // panel that is returned is 'base'
        JPanel base = new JPanel();
        base.setLayout(new GridLayout(6,1));
        //base.setPreferredSize(new Dimension(500, 125));
        //base.setBorder(new LineBorder(Color.BLACK));

        // top button used to select this group
        JPanel groupSelectBTNPanel = new JPanel(new BorderLayout());
        String groupLetter = String.valueOf((char) (groupNumber + 65)); // '65' = 'A' // TODO: maybe a String
        JButton groupSelectBTN = new JButton(groupLetter); // top of each group panel has a 'Select this group' button
        //groupSelectBTN.setPreferredSize(new Dimension(base.getWidth(), 35));
        groupSelectBTN.addActionListener(this);

        // The top row of the group panel is a button with the group letter
        groupSelectBTNPanel.add(groupSelectBTN, BorderLayout.CENTER);
        base.add(groupSelectBTNPanel);

        // Title's that go above each column of data in group panel
        JPanel titlePane = new JPanel();
        titlePane.setLayout(new GridLayout(1, 7, 2, 2));
        //titlePane.setPreferredSize(new Dimension(base.getWidth(), 25));
        titlePane.setBorder(new LineBorder(Color.BLACK));

        /*
            __ Title pane __
            Position - Country Name - flag - Wins - Draws - Losses - Points
        */
        JLabel[] titles = new JLabel[7];
        titles[0] = new JLabel("Position");
        titles[1] = new JLabel("Country");
        titles[2] = new JLabel("Flag");
        titles[3] = new JLabel("Wins");
        titles[4] = new JLabel("Draws");
        titles[5] = new JLabel("Losses");
        titles[6] = new JLabel("Points");
        for (JLabel l : titles) {
            l.setHorizontalAlignment(SwingConstants.CENTER);
            titlePane.add(l);
        }

        // next row for group panel is the title panel
        base.add(titlePane);

        // last is the 4 row panes
        JPanel[] rowPanes = new JPanel[4]; // displays team information
        for (int i = 0; i < 4; i++) {
            // initialize
            rowPanes[i] = new JPanel();
            rowPanes[i].setBorder(new LineBorder(Color.BLACK));
            rowPanes[i].setLayout(new GridLayout(1,7));
            rowPanes[i].setFont(new Font("Arial", Font.BOLD, 10));

            // compose
            for (int j = 0; j < 7; j++) {
                JLabel label;
                if(j == 0) { // { POSITION }
                    label = new JLabel();
                    label.setText(String.valueOf(i + 1)); // display position (1-4)
                }
                if(j == 2) { // { FLAG }
                    label = new JLabel(new ImageIcon());
                    label.setText("");
                } else {
                    label = new JLabel("0");
                }

                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanes[i].add(label);
            }
            base.add(rowPanes[i]);
        }
        return base;
    }
    /**
     * Create 1 empty row-panel for group-panel
     */
    private void createGroupPanelRows() {
        Component[] groupPanels = groupDisplayPanel.getComponents();

        // for each group
        for(int a = 0; a < this.groupTeams.size(); a++) {
            JPanel groupPanel = (JPanel) groupPanels[a];
            Component[] groupRowPanels = groupPanel.getComponents();

            for(int b = 2; b < 6; b++) {
                String countryName = this.groupTeams.get(a+1).get(b-2).getName();
                // component 1 = "Country Name" label
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(1)).setText(countryName);
                // component 2 = "Flags"
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(2)).setIcon(
                        getScaledIcon( this.groupTeams.get(a+1).get(b-2).getAbbv() ) );
                // Component 3-6 = { "Wins" "Draws" "Losses" "Points" }
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(3)).setText("0");
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(4)).setText("0");
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(5)).setText("0");
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(6)).setText("0");
            }
        }
    }

    /**
     * After initialization of class object this method will update panels with the teams in each group
     */
    private void initGroupPanelsWithTeams() {
        // for each group
        for(Integer groupNumber : this.groupTeams.keySet()) {
            // get group panel
            JPanel groupPanel = (JPanel) this.groupDisplayPanel.getComponent(groupNumber-1);
            // for each group row panel, Components: [0] - selection button, [1] title pane
            for (int b = 2; b < groupPanel.getComponents().length; b++) {
                JPanel row = (JPanel) groupPanel.getComponent(b);

                Team team = this.groupTeams.get(groupNumber).get(b-2);
                ((JLabel) row.getComponent(1)).setText(team.getName());
                ((JLabel) row.getComponent(1)).setToolTipText(team.getName());
                ((JLabel) row.getComponent(2)).setIcon(getScaledIcon(team.getAbbv()));

            }
        }

        // for each group panel
        for (Component groupPanel : this.groupDisplayPanel.getComponents()) {
            if (groupPanel instanceof JPanel) {

            }
        }
    }
    /**
     * Retrieves and updates the groupPanel for groupNumber with the results of
     * match.
     * @param groupNumber - The group to update
     * @param match - the match to update group with
     */
    private void updateGroupPanelInfo(int groupNumber, Match match) {
        JPanel groupPanel = (JPanel) groupDisplayPanel.getComponent(groupNumber - 1);

        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        int t1Score = match.getTeam1Score();
        int t2Score = match.getTeam2Score();
        JPanel country1 = getCountriesGroupRowPanel(groupPanel, team1.getName());
        JPanel country2 = getCountriesGroupRowPanel(groupPanel, team2.getName());

        /*  index values = JLabel position in RowPanel component hierarchy
            [0] "Position"
            [1] "Country Name"
            [2] "Flag"
            [3] "Wins"
            [4] "Draws"
            [5] "Losses"
            [6] "Points"

            increment values - [5] "Points" value
            Wins = 3 points
            Draws = 1 point
            losses = 0 points
         */

        if(t1Score > t2Score) {
            updateGroupRowPanelValues(country1, 3, 1); // [3] = wins
            updateGroupRowPanelValues(country1, 6, 3); // [6] = points
            updateGroupRowPanelValues(country2, 5, 1); // [5] = losses
        } else if(t1Score < t2Score) {
            updateGroupRowPanelValues(country2, 3, 1); // [3] = wins
            updateGroupRowPanelValues(country2, 6, 3); // [6] = points
            updateGroupRowPanelValues(country1, 5, 1); // [5] = losses
        } else { // draw
            updateGroupRowPanelValues(country1, 4, 1); // [4] = Draws
            updateGroupRowPanelValues(country1, 6, 1); // [6] = points
            updateGroupRowPanelValues(country2, 4, 1); // [4] = Draws
            updateGroupRowPanelValues(country2, 6, 1); // [6] = points
        }
    }
    /**
     * Retrieves the groupRowPanel of the specified country so that it's group stats can
     * be updated by updateGroupPanelInfo() method.
     * @param groupPanel - Panel containing countries stats
     * @param country - country to find in group
     * @return - countries row panel
     */
    private JPanel getCountriesGroupRowPanel(JPanel groupPanel, String country) {
        for(Component row : groupPanel.getComponents()) {
            if(row instanceof JPanel) {
                if(((JPanel) row).getComponents().length > 1) { // first row contains select group button, this skips that row
                    // compare country name of each row panel until match is found
                    if (((JLabel) ((JPanel) row).getComponent(1)).getText().equals(country)) {
                        return ((JPanel) row);
                    }
                }
            }
        }
        // TODO: possible error, unhandled
        return null;
    }
    /**
     * Extracts the text from the JLabel in GroupRowPanel at index
     * converts text to integer then adds the increment value
     * then sets the text of the same JLabel with the new value.
     * @param rowPanel - panel containing labels to update
     * @param index - of label to update
     * @param increment - value to increment JLabel index value by
     */
    private void updateGroupRowPanelValues(JPanel rowPanel, int index, int increment) {
        String indexStr = ((JLabel) rowPanel.getComponent(index)).getText();
        int value = (Integer.parseInt(indexStr)) + increment;
        ((JLabel) rowPanel.getComponent(index)).setText(String.valueOf(value));
    }

    private void initGroupRowPanel(JPanel rowPanel, int index, String countryName) {
        String indexStr = ((JLabel) rowPanel.getComponent(index)).getText();
    }
    /**
     * Used to ensure that group panels always display row information based on
     * descending position 1 - 4 top to bottom
     */
    private void reorderGroupRowPanelsByPointValue(JPanel groupPanel) {
        Component[] rowPanels = groupPanel.getComponents();
        int numRowPanels = rowPanels.length;

        // extract an array of only the panels that need sorting
        JPanel[] panelsToSort = new JPanel[numRowPanels - 2];
        for (int i = 2; i < numRowPanels; i++) {
            panelsToSort[i - 2] = (JPanel) rowPanels[i];
        }

        // sort those panels by "point" values in descending order
        Arrays.sort(panelsToSort, (p1, p2) -> {
            Integer points1 = Integer.parseInt(((JLabel) p1.getComponent(6)).getText());
            Integer points2 = Integer.parseInt(((JLabel) p2.getComponent(6)).getText());
            int pointComparison = points2.compareTo(points1);
            if (pointComparison == 0) {
                String country1 = ((JLabel) p1.getComponent(1)).getText();
                String country2 = ((JLabel) p2.getComponent(1)).getText();
                return country1.compareTo(country2);
            }
            return pointComparison;
        });

        // remove each panel and add them back in the sorted order
        for (int i = 2; i < numRowPanels; i++) {
            ((JLabel) panelsToSort[i - 2].getComponent(0)).setText(String.valueOf(i - 1));
            panelsToSort[i - 2].setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(2, 2, 2, 2)));
            groupPanel.remove(panelsToSort[i - 2]);
            groupPanel.add(panelsToSort[i - 2], i);
        }

        // refreshes the display with changes
        groupPanel.revalidate();
        groupPanel.repaint();
    }

    /* RESULTS PANEL FUNCTIONS */
    /**
     * This method creates a blank group results row panel that will be used to display
     * match results between teams in a group.
     *
     * @return 1 empty row for the match results pane
     */
    private JPanel createMatchResultRowPanel() {
        JPanel base = new JPanel(new FlowLayout());
        for (int i = 0; i < 7; i++) {
            if(i == 3) {
                base.add(new JLabel("-"));
            } else {
                base.add(new JLabel("x"));
            }
        }
        return base;
    }
    /**
     * This method takes 1 match and translates it into a row of the group results panel.
     * The row contains information about the teams, scores, and results(WIN/LOSS/DRAW)
     *
     * @param match - The match to translate into a row panel
     */
    private void updateMatchResultPanel(Match match, int round) {
        /*  results row-panel layout
            Country1Abbv - Result1 - Score1 "-" Score2 - Result2 - Country2Abbv
        */
        Component[] allRowPanels = this.resultsPanel.getComponents();
        // [0]"Group Results", [1]round 1 match results row panel, [2-5]..., [6]round 6 match results row panel.
        JPanel roundRowPanel = (JPanel) allRowPanels[round];
        Component[] labels = roundRowPanel.getComponents();

        // Initialize
        String name1 = match.getTeam1().getAbbv();
        String name2 = match.getTeam2().getAbbv();
        int score1 = match.getTeam1Score();
        int score2 = match.getTeam2Score();
        String result1;
        String result2;
        if (score1 > score2) {
            result1 = "Win";
            result2 = "Loss";
        } else if (score1 < score2) {
            result1 = "Loss";
            result2 = "Win";
        } else {
            result1 = "Draw";
            result2 = "Draw";
        }

        // Compose
        ((JLabel) labels[0]).setText(name1);
        ((JLabel) labels[1]).setText(result1);
        ((JLabel) labels[2]).setText(String.valueOf(score1));
        ((JLabel) labels[3]).setText("-");
        ((JLabel) labels[4]).setText(String.valueOf(score2));
        ((JLabel) labels[5]).setText(result2);
        ((JLabel) labels[6]).setText(name2);
    }
    /**
     * When switching 'selected group' this method fills in matches from rounds that have
     * already been simulated. Displaying the correct match results for completed matches
     * for the selected group.
     * @param groupNum - selected group to update results side-pane with its matches
     * @param round - the last round simulated by selected group
     */
    private void backPopulateResultsPanel(int groupNum, int round) {
        clearResultsPanel();
        for(int i = 0; i < round; i++) {
            Match match = groupMatches.get(groupNum).get(i);
            updateMatchResultPanel(match, i+1);
        }
    }
    /**
     * clear all currently displayed matches in the results side-pane and
     * set each row to a default set of values
     */
    private void clearResultsPanel() {
        Component[] allRowPanels = this.resultsPanel.getComponents();
        // [0]"Group Results", [1]round 1 match results row panel, [2-5]..., [6]round 6 match results row panel.
        for(int row = 1; row < allRowPanels.length; row++) {
            JPanel roundRowPanel = (JPanel) allRowPanels[row];
            Component[] labels = roundRowPanel.getComponents();

            // Compose
            ((JLabel) labels[0]).setText("x");
            ((JLabel) labels[1]).setText("x");
            ((JLabel) labels[2]).setText("x");
            ((JLabel) labels[3]).setText("-");
            ((JLabel) labels[4]).setText("x");
            ((JLabel) labels[5]).setText("x");
            ((JLabel) labels[6]).setText("x");
        }
    }

    /* CLASS FUNCTIONS */
    /**
     * Import flags of teams involved in the group stage
     * @throws IOException - Image import failed
     */
    private void initFlags() throws IOException {
        for(Team team : this.teams) {
            String abbv = team.getAbbv();
            BufferedImage flag = ImageIO.read(new File("Assets" + File.separator + "Images" + File.separator + "smallFlags" + File.separator +  abbv + ".png"));
            flags.put(abbv, flag);
        }
    }
    /**
     * Called by select group button on top of each group panel. This method takes the matches
     * from the selected group and displays them on the side-pane results panel.
     * @param selectedGroup - use matches from this group
     */
    private void changeSelectedGroup(String selectedGroup) {
        // update info panel with current round
        this.selectedGroup = selectedGroup;
        String text = "Group " + this.selectedGroup + " results";
        ((JLabel) ((JPanel) resultsPanel.getComponent(0)).getComponent(0)).setText(text);

        int groupNum = this.selectedGroup.charAt(0)-64;
        infoPanelRoundNumberLabel.setText(String.valueOf(currentRound[groupNum-1]));
        infoPanelGroupLabel.setText(this.selectedGroup);
        // update results panel up to current round
        backPopulateResultsPanel(groupNum, this.currentRound[groupNum-1]);

        // get next group round function button
        Component[] rootChildren = this.getComponents();
        JPanel functionPanel = (JPanel) rootChildren[2];
        Component[] functionPanelChildren = functionPanel.getComponents();
        JButton nextGroupRoundBTN = (JButton) functionPanelChildren[0];

        // update function button text to include selected group letter
        nextGroupRoundBTN.setText("Next round in group " + this.selectedGroup);
    }
    /**
     * Handles method calls to simulate entire stage. All rounds for all groups will be completed
     * and the UI updated.
     */
    private void simulateStage() {
        for(int i = 0; i < 6; i++) {
            simulateRound();
        }
    }
    /**
     * Handles method calls to simulate one round for all groups. Updates UI.
     */
    private void simulateRound() {
        for(int i = 0; i < this.groupTeams.size(); i++) {
            simulateGroupRound(i+1);
        }
    }
    /**
     * Handles 1 round for a single group, this method calls for those
     * matches to be simulated and updates the UI.
     */
    private void simulateGroupRound(int groupNumber) {
        // If group has rounds remaining
        if(this.currentRound[groupNumber-1] < 6) {
            // get next match in the series
            Match match = groupMatches.get(groupNumber).get(this.currentRound[groupNumber - 1]);
            // update UI with new match data
            updateGroupPanelInfo(groupNumber, match);
            // Only update match results side-pane with matches from selected group
            if(groupNumber == (this.selectedGroup.charAt(0)-64)) {
                // +1 at end creates index of groupRowPanel for that round for method call.
                updateMatchResultPanel(match, this.currentRound[groupNumber - 1] + 1);
            }
            // Rearrange group panel rows based on new point values obtained from 'match'
            reorderGroupRowPanelsByPointValue((JPanel) groupDisplayPanel.getComponent(groupNumber-1));
            // this group is now onto the next round
            this.currentRound[groupNumber-1]++;
            // update info panel with current round
            infoPanelRoundNumberLabel.setText(String.valueOf(this.currentRound[groupNumber-1]));
        }

        // Highlight 1st/2nd place of group panel
        if(this.currentRound[groupNumber-1] == 6) {
            this.groupsThatAreComplete[groupNumber - 1] = true;
            setCompletedGroupsToIndicateComplete(groupNumber - 1);
        }

    }
    /**
     * This method adds visual feedback to the group panel once it's complete to
     * highlight to the user which teams will move on from the group.
     * @param groupNumber - completed group to highlight
     */
    private void setCompletedGroupsToIndicateComplete(int groupNumber) {
        JPanel groupPanel = (JPanel) groupDisplayPanel.getComponent(groupNumber);
        JPanel rowPanel1 = (JPanel) groupPanel.getComponent(2);
        JPanel rowPanel2 = (JPanel) groupPanel.getComponent(3);
        rowPanel1.setBorder(new LineBorder(Color.green, 1));
        rowPanel2.setBorder(new LineBorder(Color.green, 1));
        groupPanel.getComponent(4).setBackground(Color.lightGray);
        groupPanel.getComponent(5).setBackground(Color.lightGray);

    }
    /**
     * Using List of matches, separate each team and the matches they played out into groups of 4 teams
     * and 6 matches. total of 8 groups.
     */
    private void createGroups() {
        // TODO: verify dynamic size groups work (>4 teams)
        /* put teams into groups */
        for (Match match : matches) {
            // get teams
            Team team1 = match.getTeam1();
            Team team2 = match.getTeam2();
            int groupNumber;

            /* Build match, group, and team, associations */
            if (teamGroups.containsKey(team1)) {                    // __ is team1 in a group? __
                groupNumber = teamGroups.get(team1);                // get team1 group number
                groupMatches.get(groupNumber).add(match);           // save match to groupNumber's matches
                if(!groupTeams.get(groupNumber).contains(team2)) {  // if group doesn't contain team 2
                    groupTeams.get(groupNumber).add(team2);         // add team2 to team1's group
                    teamGroups.put(team2, groupNumber);             // associate team2 to group
                }
            } else if (teamGroups.containsKey(team2)) {             // __ is team2 in a group? __
                groupNumber = teamGroups.get(team2);                // get team2 group number
                groupMatches.get(groupNumber).add(match);           // save match to groupNumber's matches
                if(!groupTeams.get(groupNumber).contains(team1)) {  // if group doesn't contain team 2
                    groupTeams.get(groupNumber).add(team1);         // add team1 to team2's group
                    teamGroups.put(team1, groupNumber);             // associate team1 to group
                }
            } else {
                // if < 8 groups then add new group
                if(groupTeams.size() < 8) {
                    int size = groupTeams.size();
                    groupTeams.put((size + 1), new ArrayList<>());
                    groupMatches.put((size + 1), new ArrayList<>());
                }

                for (Integer gn : groupTeams.keySet()) {     // Search each group for empty group
                    if (groupTeams.get(gn).size() == 0) {    // if group is empty
                        groupTeams.get(gn).add(team1);      // add team1 to group
                        teamGroups.put(team1, gn); // associate team1 to group
                        groupTeams.get(gn).add(team2);      // add team2 to group
                        teamGroups.put(team2, gn); // associate team2 to a group
                        groupMatches.get(gn).add(match);    // add match to group
                    }
                }
            }

        }

        // sort the matches chronologically by date
        for(Integer i : groupMatches.keySet()) {
            groupMatches.get(i).sort(Comparator.comparing(Match::getMatchDate));
        }
    }
    /**
     * Using the country abbreviation(@param abbv) this method finds the countries flag
     * from the 'SmallFlags' directory of images and scales it down to one half the current
     * size. Then returns the flag housed within an imageIcon for use by a JLabel to display.
     * @param abbv - country name abbreviation
     * @return - ImageIcon of countries flag
     */
    private ImageIcon getScaledIcon(String abbv) {
        double scaleFactor = 0.5;
        int iconX = (int) (flags.get(abbv).getWidth(this) * scaleFactor);
        int iconY = (int) (flags.get(abbv).getHeight(this) * scaleFactor);

        Image scaledPreviewImage = flags.get(abbv).getScaledInstance(iconX, iconY, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(iconX, iconY, BufferedImage.TYPE_INT_ARGB);

        image.getGraphics().drawImage(scaledPreviewImage, 0, 0, null);

        return new ImageIcon(image);
    }

    /* OVERRIDES */
    /**
     * Returns if the group stage matches have been fully simulated and the group stage
     * is complete.
     * @return state of completion
     */
    @Override
    public boolean checkIfCompleted() {
        boolean finalResult = true;
        for(boolean groupComplete : this.groupsThatAreComplete) {
            if(!groupComplete) {
                finalResult = false;
            }
        }
        return finalResult;
    }

    @Override
    public void initPanel(List<Match> matchList, List<Team> teamList) {
        this.matches = matchList;
        createGroups();
        initPanel();
    }

    /**
     * Create an empty group stage panel that will be filled in as the user interacts with function buttons
     */
    public void initPanel() {
        themeColor = new Color(21, 21, 240);
        Font font = new Font("Arial", Font.BOLD, 30);
        /* Top bar across window for displaying current round */
        JPanel infoPanel = new JPanel(new GridLayout(1,4));
        infoPanel.setBackground(themeColor);
        /* Center container for group results and group teams */
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        displayPanel.setBackground(themeColor);
        /* Bottom bar across window for housing functions */
        JPanel functionPanel = new JPanel();

        /* Information Panel */
        JLabel groupLabel = new JLabel("Group: ");
        groupLabel.setFont(font);
        groupLabel.setForeground(Color.white);
        groupLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        infoPanel.add(groupLabel);

        infoPanelGroupLabel.setFont(font);
        infoPanelGroupLabel.setForeground(Color.white);
        infoPanelGroupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(infoPanelGroupLabel);

        JLabel roundLabel = new JLabel("Round: ");
        roundLabel.setFont(font);
        roundLabel.setForeground(Color.white);
        infoPanel.add(roundLabel);

        infoPanelRoundNumberLabel.setFont(font);
        infoPanelRoundNumberLabel.setForeground(Color.white);
        infoPanel.add(infoPanelRoundNumberLabel);

        /* __ Display Panel */
        // Container for all group panes - displays each teams w/d/l record and points
        groupDisplayPanel.setLayout(new GridLayout(4, 2,5,5));
        groupDisplayPanel.setOpaque(false);
        groupDisplayPanel.setPreferredSize(new Dimension(975, 550));
        for (int i = 0; i < 8; i++) {
            groupDisplayPanel.add(createGroupPanel(i));
        }
        createGroupPanelRows();

        // results side-pane - displays score and outcome between each match in the group
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setPreferredSize(new Dimension(200, 215));
        resultsPanel.setBorder(new LineBorder(Color.black));
        JPanel resultsTitlePane = new JPanel();
        JLabel resultsTitleLabel = new JLabel("Group A Results");
        resultsTitlePane.setBackground(Color.lightGray);
        resultsTitlePane.setBorder(new LineBorder(Color.BLACK));
        resultsTitlePane.add(resultsTitleLabel);
        resultsPanel.add(resultsTitlePane);
        for (int c = 0; c < 6; c++) {
            resultsPanel.add(createMatchResultRowPanel());
            ((JPanel) resultsPanel.getComponent(c+1)).setBorder(new LineBorder(Color.BLACK));
        }
        // compose display panel
        displayPanel.add(groupDisplayPanel, constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(0, 5, 0, 5 );
        displayPanel.add(resultsPanel, constraints);

        /* Function Panel */
        JButton nextRoundSelectedGroupBTN = new JButton("Next round in group " + selectedGroup);
        nextRoundSelectedGroupBTN.addActionListener(this);
        JButton nextRoundAllGroupsBTN = new JButton("Next round for all groups");
        nextRoundAllGroupsBTN.addActionListener(this);
        JButton completeStageBTN = new JButton("Complete Stage");
        completeStageBTN.addActionListener(this);
        // compose function panel
        functionPanel.add(nextRoundSelectedGroupBTN);
        functionPanel.add(nextRoundAllGroupsBTN);
        functionPanel.add(completeStageBTN);

        // compose root panel
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(functionPanel, BorderLayout.SOUTH);

        initialized = true;
    }

    @Override
    public boolean checkIfInitialized() {
        return initialized;
    }

    @Override
    public Color getThemeColor() {
        return themeColor;
    }

    /**
     * Event handler for button clicks
     * @param e - clicked button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = (JButton) e.getSource();
        String text = pressed.getText();

        // This buttons text changes
        if(text.contains("Next round in group")) {
            text = "Simulate group round";
        }

        switch(text) {
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "H":
                changeSelectedGroup(text);
                break;
            case "Simulate group round":
                simulateGroupRound(((int) this.selectedGroup.charAt(0)) - 64);
                break;
            case "Next round for all groups":
                simulateRound();
                break;
            case "Complete Stage":
                simulateStage();
                break;
        }
    }

}
