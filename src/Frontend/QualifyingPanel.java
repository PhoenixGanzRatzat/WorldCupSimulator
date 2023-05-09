package Frontend;
import Backend.Match;
import Backend.Team;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The QualifyingPanel displays all information related to the qualifying round
 * of the world cup. It displays this information using a JTabbedPane, which
 * contains a MonthPanel in one tab and JScrollPanes in the rest. The MonthPanel
 * can be changed to view all matches over time, and the JScrollPanes display
 * Teams in ranked order.
 *
 * @author Dov Zipursky
 */


public class QualifyingPanel extends JPanel implements StagePanel {

    /**
     * Various colors that this class uses
     */
    protected static final Color BG_COLOR = StagePanel.fifaBlue;
    protected static final Color ROW1_COLOR = new Color(179, 201, 230);
    protected static final Color ROW2_COLOR = new Color(198, 215, 236);
    protected static final Color SCROLLPANE_COLOR = new Color(198, 215, 236);

    /**
     * The month and year that are currently shown in the MonthPanel
     */
    private int curMonth;
    private int curYear;

    /**
     * A HashMap of flag images, used to access those images with the
     * team abbreviation
     */
    private HashMap<String, BufferedImage> flags;
    /**
     * A list of all matches played in the qualifying round
     */
    private List<Match> matches;

    /**
     * The date of the first match played
     */
    private LocalDate earliestMatchDate;
    /**
     * The date of the last match played
     */
    private LocalDate latestMatchDate;
    /**
     * A list of all teams participating in the qualifying round
     */
    private List<Team> teams;
    /**
     * The MonthPanel
     */
    private MonthPanel month;
    /**
     * An array used to hold all of the region tabs
     */
    private JPanel[] cards;
    /**
     * An array of regions to be used for sorting and tab titles
     */
    private String[] regions = new String[6];
    /**
     * The JTabbedPane that serves as the base of this class's display
     */
    private JTabbedPane tabPane;
    /**
     * A boolean tracking if this class has had initPanel() called or not
     */
    private boolean initialized;

    /**
    The default constructor for QualifyingPanel.
    Initializes the regions array with strings and creates a MonthPanel.
     */
    public QualifyingPanel () {

        month = new MonthPanel();

        regions[0] = "AFC";
        regions[1] = "CAF";
        regions[2] = "CONCACAF";
        regions[3] = "CONMEBOL";
        regions[4] = "OFC";
        regions[5] = "UEFA";

        initialized = false;

    }

    /**
     * Returns the JTabbedPane that this object uses to display its contents.
     * @return JTabbedPane
     */
    public JTabbedPane getPane() {
        return tabPane;
    }

    /**
     * Takes an array of matches and fills the visible day panels
     * with that matches that took place on those days.
     * Uses a border layout to display the month panel and navigation controls.
     *
     * @param matches a List of matches for the month being shown
     */
    public void initMonthPanel(List<Match> matches) {

        month.setToMonth(curYear, curMonth);
        month.setMatchesOnDayPanels(matches);
        month.setPreferredSize(new Dimension(800, 600));

        JButton forward = new JButton(">");
        JButton backward = new JButton("<");

        forward.setBackground(tabPane.getBackground().darker());
        backward.setBackground(tabPane.getBackground().darker());
        forward.setOpaque(true);
        backward.setOpaque(true);

        forward.addActionListener(listener);
        backward.addActionListener(listener);

        forward.setActionCommand("next");
        backward.setActionCommand("back");


        JPanel genPanel = new JPanel();
        BorderLayout layout = new BorderLayout();
        genPanel.setLayout(layout);

        genPanel.add(forward, BorderLayout.EAST);
        genPanel.add(backward, BorderLayout.WEST);
        genPanel.add(month, BorderLayout.CENTER);
        genPanel.setBackground(BG_COLOR);
        genPanel.setOpaque(true);


        tabPane.insertTab("Matches by Month", null, genPanel, null, 0);


    }


    /**
    Places the results of the qualifying round into tabs, with the results
    sorted by region. Each tab is set up with a SpringLayout and displays the results
    in columns.
     */
    public void fillResults() {
        SpringLayout layout = new SpringLayout();

        for(int i = 1; i < cards.length + 1; i++) {

          JLabel header1 = new JLabel("RANK:");
          JLabel header2 = new JLabel ("TEAM:");
          JLabel header3 = new JLabel("TOTAL POINTS:");

          header1.setFont(new Font("Arial Black", Font.BOLD, 16));
          header2.setFont(new Font("Arial Black", Font.BOLD, 16));
          header3.setFont(new Font("Arial Black", Font.BOLD, 16));

            tabPane.removeTabAt(i);

            JPanel newTab = new JPanel();
            newTab.setLayout(layout);
            newTab.setName(regions[i - 1]);
            newTab.add(header1);
            newTab.add(header2);
            newTab.add(header3);

            //each component needs its own SpringLayout.Constraints
            SpringLayout.Constraints con1 = layout.getConstraints(header1);
            //setX without anything else just sets from 0,0 as normal
            con1.setX(Spring.constant(10));

            SpringLayout.Constraints con2 = layout.getConstraints(header2);
            //SpringSum adds the constant buffer to the East/West/North/South edge of the specified component,
            //in this case header1, using the con1 object to access that
            con2.setX(Spring.sum(Spring.constant(10), con1.getConstraint(SpringLayout.EAST)));

            SpringLayout.Constraints con3 = layout.getConstraints(header3);
            con3.setX(Spring.sum(Spring.constant(Toolkit.getDefaultToolkit().getScreenSize().width/2),
                    con2.getConstraint(SpringLayout.EAST)));


            ArrayList<Team> sortedArr = new ArrayList<Team>();
            LocalDate getPointsFrom = java.time.LocalDate.of(curYear, curMonth, 1);
            getPointsFrom = getPointsFrom.withDayOfMonth(getPointsFrom.lengthOfMonth());

            for(Team team : teams) {
                if(team.getRegion().toString() == newTab.getName()) {
                    if(sortedArr.size() == 0) {
                        sortedArr.add(team);
                    }

                    else {
                        for(int j = 0; j < sortedArr.size(); j++) {

                            if(team.getPoints(getPointsFrom) >= sortedArr.get(j).getPoints(getPointsFrom)) {
                                sortedArr.add(j, team);
                                j = sortedArr.size();

                            }
                        }

                        if(!sortedArr.contains(team)) {
                            sortedArr.add(sortedArr.size(), team);
                        }
                    }
                }
            }

            for(Team team : sortedArr) {

                JPanel tempPanel = new JPanel();
                JLabel teamName = new JLabel(team.getName());
                JLabel teamPoints = new JLabel("" + team.getPoints(getPointsFrom));
                JLabel teamRank = new JLabel("" + (sortedArr.indexOf(team) + 1));
                ImageIcon teamFlag = new ImageIcon(flags.get(team.getAbbv()).getScaledInstance(40, 24, 1));
                JLabel flagLabel = new JLabel();
                flagLabel.setIcon(teamFlag);
                tempPanel.setLayout(layout);

                teamName.setFont(new Font("Arial Black", Font.PLAIN, 12));
                teamPoints.setFont(new Font("Arial Black", Font.PLAIN, 12));
                teamRank.setFont(new Font("Arial Black", Font.PLAIN, 12));

                tempPanel.add(flagLabel);
                tempPanel.add(teamName);
                tempPanel.add(teamPoints);
                tempPanel.add(teamRank);
                newTab.add(tempPanel);

                tempPanel.setPreferredSize(
                        new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 30));
                if(sortedArr.indexOf(team) % 2 == 0) {
                    tempPanel.setBackground(ROW1_COLOR);
                }
                else {
                    tempPanel.setBackground(ROW2_COLOR);
                }
                tempPanel.setOpaque(true);

                SpringLayout.Constraints cTemp = layout.getConstraints(tempPanel);
                cTemp.setY(Spring.sum(Spring.constant(30 * (sortedArr.indexOf(team) + 1)),
                                con1.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cRank = layout.getConstraints(teamRank);
                cRank.setX(Spring.constant(10));


                SpringLayout.Constraints cFlag = layout.getConstraints(flagLabel);
                //sets right edge to align with right edge of the TEAMS: label
                cFlag.setX(con2.getConstraint(SpringLayout.WEST));

                SpringLayout.Constraints cName = layout.getConstraints(teamName);
                //sets x to have a 5 pixel buffer between team name and flag
                cName.setX(Spring.sum(Spring.constant(5), cFlag.getConstraint(SpringLayout.EAST)));

                SpringLayout.Constraints cPoints = layout.getConstraints(teamPoints);
                cPoints.setX(con3.getConstraint(SpringLayout.WEST));



            }

            JScrollPane scroll = new JScrollPane(newTab);
            newTab.setBackground(SCROLLPANE_COLOR);
            scroll.setOpaque(false);
            int height = Math.max(Toolkit.getDefaultToolkit().getScreenSize().height, (30 * sortedArr.size())+ 50);
            newTab.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, height));
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            //theThirdOne.add(scroll, BorderLayout.CENTER);

            tabPane.insertTab(newTab.getName(), null, scroll, null, i);
            tabPane.revalidate();
            scroll.revalidate();


        }
   }

    /**
     * Returns true if the qualifying round has been completed, and false otherwise.
     *
     * @return boolean
     */
    @Override
    public boolean checkIfCompleted() {
        return true;
    }

    /**
     * Returns true if the panel has already been initialized by initPanel(),
     * returns false otherwise.
     * @return boolean
     */
    @Override
    public boolean checkIfInitialized() {
        return initialized;
    }


    /**
     * Initializes attributes necessary to display QualifyingPanel, including teams and matches,
     * all tabs, and the starting date.
     * @param matches a list of all matches in the qualifying round
     * @param teamList a list of all teams in the qualifying round
     */
    @Override
    public void initPanel(List<Match> matches, List<Team> teamList) {
        LocalDate earliest = matches.get(0).getMatchDate();
        LocalDate latest = matches.get(0).getMatchDate();

        for (Match match : matches) {
            if (match.getMatchDate().isBefore(earliest)) earliest = match.getMatchDate();
            if (match.getMatchDate().isAfter(latest)) latest = match.getMatchDate();
        }

        earliestMatchDate = earliest;
        latestMatchDate = latest;

        this.matches = matches;
        this.teams = teamList;
        curMonth = earliestMatchDate.getMonthValue();
        curYear = earliestMatchDate.getYear();
        tabPane =  new JTabbedPane();
        tabPane.setOpaque(true);
        cards = new JPanel[6];
        this.setLayout(new BorderLayout());

        try {
            initFlags();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        initMonthPanel(matches);

        for(int i = 0; i < 6; i++) {
            JPanel subPanel = new JPanel();
            subPanel.add(new JLabel("Results coming soon for " + regions[i]));
            cards[i] = subPanel;

            tabPane.addTab(regions[i], cards[i]);
        }

        tabPane.setForeground(Color.BLACK);
        fillResults();

        this.add(tabPane);
        tabPane.setBackground(ROW1_COLOR);
        //tabPane.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        month.setBackground(BG_COLOR);
        //this.setSize(new Dimension(1600, 900));
        initialized = true;
    }

    /**
     * An ActionListener that I decided would be more convenient than
     * implementing the interface. Handles the buttons that change the months displayed.
     */
    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed (ActionEvent e) {
            String command = e.getActionCommand();
            //System.out.println(command);

            switch(command) {
                case "next": curMonth++;
                break;
                case "back": curMonth--;
                break;
                default: System.out.println("error message");
            }

            if(curMonth > 12) {
                curMonth = 1;
                curYear++;
            }
            if(curMonth < 1 || curYear > 2018) {
                curMonth = 12;
                curYear--;
            }
            if(curYear < 2015) {
                curMonth = 1;
                curYear++;
            }
            if(curYear == earliestMatchDate.getYear() && curMonth < earliestMatchDate.getMonthValue()) {
                curMonth = 3; //earliest match is March 2015
            }

            if(curYear == latestMatchDate.getYear() && curMonth > latestMatchDate.getMonthValue()) {
                curMonth = 6; //latest match is June 2018
            }

            month.setToMonth(curYear, curMonth);
            month.setMatchesOnDayPanels(matches); //backend.getMatchesForYearMonth(
            fillResults();
        }
    };

    /**
     * Written by Chris P. for GroupPanel and reused here for the same purpose.
     * Fills a HashMap with Flag images that are accessed by the
     * relevant team abbreviation.
     * @throws IOException
     */
    private void initFlags() throws IOException {
        flags = new HashMap<String, BufferedImage>();
        for(Team team : this.teams) {
            String abbv = team.getAbbv();
            BufferedImage flag = null;
            try {
                flag = ImageIO.read(new File("Assets" + File.separator + "Images" + File.separator + "smallFlags" + File.separator +  abbv + ".png"));
            } catch (IOException e) {
                throw new IOException("Couldn't load flag for team " + abbv + " (" + team.getName() + ")", e);
            }
            flags.put(abbv, flag);
        }
    }



}
