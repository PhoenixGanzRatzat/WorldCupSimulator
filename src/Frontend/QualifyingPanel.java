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
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class QualifyingPanel extends JPanel implements StagePanel {

    //ARIAL BLACK BOLD
    private int curMonth;
    private HashMap<String, BufferedImage> flags;
    private int curYear;
    private List<Team> teams;
    private MonthPanel month;
    private JPanel[] cards;
    private String[] regions = new String[6];
    private JTabbedPane tabPane;
    private boolean initialized;

    /**
    An in progress constructor that is subject to change.
    @param teamIn an array of all teams participating.
     */
    public QualifyingPanel (List<Team> teamIn) {

        teams = teamIn;
        month = new MonthPanel();

        regions[0] = "AFC";
        regions[1] = "CAF";
        regions[2] = "CONCACAF";
        regions[3] = "CONMEBOL";
        regions[4] = "OFC";
        regions[5] = "UEFA";

        try {
            initFlags();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        initialized = false;
    }

    /**
    The default constructor for QualifyingPanel.
    Initializes the regions array with strings.
     */
    public QualifyingPanel () {

        teams = new ArrayList<Team>();
        month = new MonthPanel();

        regions[0] = "AFC";
        regions[1] = "CAF";
        regions[2] = "CONCACAF";
        regions[3] = "CONMEBOL";
        regions[4] = "OFC";
        regions[5] = "UEFA";

        try {
            initFlags();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

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
     * @param matches
     */
    public void initMonthPanel(List<Match> matches) {

        month.setToMonth(curYear, curMonth);
        month.setMatchesOnDayPanels(matches);
        month.setPreferredSize(new Dimension(800, 600));


        JButton forward = new JButton(">");
        JButton backward = new JButton("<");

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


        tabPane.insertTab("Matches by Month", null, genPanel, null, 0);


    }


    /**
    Places the results of the qualifying round into tabs, with the results
    sorted by region. Each tab is set up with a SpringLayout and displays the results
    in columns.
    In the future will take a parameter, likely an array of matches.
     */
    public void fillResults() {
        SpringLayout layout = new SpringLayout();

        for(int i = 1; i < cards.length + 1; i++) {

          JLabel header1 = new JLabel("RANK:");
          JLabel header2 = new JLabel ("TEAM:");
          JLabel header3 = new JLabel("TOTAL POINTS:");

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
            con3.setX(Spring.sum(Spring.constant(200), con2.getConstraint(SpringLayout.EAST)));

            ArrayList<Team> sortedArr = new ArrayList<Team>();

            for(Team team : teams) {
                if(team.getRegion().toString() == newTab.getName()) {
                    //team.setQualifierPoints((int) (Math.random() * 10));
                    if(sortedArr.size() == 0) {
                        sortedArr.add(team);
                    }

                    else {

                        for(int j = 0; j < sortedArr.size(); j++) {

                            if(team.getQualifierPoints() >= sortedArr.get(j).getQualifierPoints()) {
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
                JLabel teamName = new JLabel(team.getName());
                JLabel teamPoints = new JLabel("" + team.getQualifierPoints());
                JLabel teamRank = new JLabel("" + (sortedArr.indexOf(team) + 1));
                ImageIcon teamFlag = new ImageIcon(flags.get(team.getAbbv()).getScaledInstance(40, 24, 1));
                JLabel flagLabel = new JLabel();
                flagLabel.setIcon(teamFlag);

                newTab.add(flagLabel);
                newTab.add(teamName);
                newTab.add(teamPoints);
                newTab.add(teamRank);

                SpringLayout.Constraints cRank = layout.getConstraints(teamRank);
                cRank.setX(Spring.constant(10));
                //sets top of label to 30 (which is less than arbitrary) * its place in the array + 1,
                // which is how far down in the column it is, and all that goes below
                //the appropriate label.
                cRank.setY(Spring.sum(Spring.constant(30 * (sortedArr.indexOf(team) + 1)),
                        con1.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cFlag = layout.getConstraints(flagLabel);
                //sets right edge to align with right edge of the TEAMS: label
                cFlag.setX(con2.getConstraint(SpringLayout.WEST));
                cFlag.setY(Spring.sum(Spring.constant(30 * (sortedArr.indexOf(team) + 1)),
                        con2.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cName = layout.getConstraints(teamName);
                //sets x to have a 5 pixel buffer between team name and flag
                cName.setX(Spring.sum(Spring.constant(5), cFlag.getConstraint(SpringLayout.EAST)));
                cName.setY(Spring.sum(Spring.constant(30 * (sortedArr.indexOf(team) + 1)),
                        con2.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cPoints = layout.getConstraints(teamPoints);
                cPoints.setX(con3.getConstraint(SpringLayout.WEST));
                cPoints.setY(Spring.sum(Spring.constant(30 * (sortedArr.indexOf(team) + 1)),
                        con3.getConstraint(SpringLayout.SOUTH)));


            }



            tabPane.insertTab(newTab.getName(), null, newTab, null, i);
            //tabPane.revalidate();


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
     * Initiates the JTabbedPane before the simulation has started, with a blank calandar
     * tab and temporary region tabs. Will likely never need parameters.
     *
     */
    @Override
    public void initPanel()  {
        curMonth = 1;
        curYear = 2018;
        tabPane =  new JTabbedPane();
        cards = new JPanel[6];

        initMonthPanel(new ArrayList<Match>());

        for(int i = 0; i < 6; i++) {
            JPanel subPanel = new JPanel();
            subPanel.add(new JLabel("Results coming soon for " + regions[i]));
            cards[i] = subPanel;

            tabPane.addTab(regions[i], cards[i]);
        }


        fillResults();

        this.add(tabPane);
        this.setSize(1600, 900);
        initialized = true;

    }

    @Override
    public void initPanel(List<Match> matches) {
        //don't need this? Could use it for initMonthPanel but that would be confusing.
    }

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
            if(curYear < 2016) {
                curMonth = 1;
                curYear++;
            }

            month.setToMonth(curYear, curMonth);
            month.setMatchesOnDayPanels(new ArrayList<Match>()); //backend.getMatchesForYearMonth(
        }
    };

    /**
     * Written by Chris P. and reused here for the same purpose.
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
