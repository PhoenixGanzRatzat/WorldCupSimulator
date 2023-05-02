package Frontend;

import Backend.Match;
import Backend.Team;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class QualifyingPanel extends JPanel implements StagePanel {

    private Match[] matches;
    private Team[] teams;
    private MonthPanel[] months;
    private JPanel stage;
    private JPanel[] cards;
    //ough IDK
    private String[] regions = new String[6];
    private JTabbedPane tabPane;
    private boolean initialized;

    /**
    An in progress constructor that is subject to change.
    @param teamIn an array of all teams participating.
     */
    public QualifyingPanel (Team[] teamIn) {
        //matches = matchIn;
        teams = teamIn;

        months = new MonthPanel[1];
        months[0] = new MonthPanel();

        regions[0] = "AFC";
        regions[1] = "CAF";
        regions[2] = "CONCACAF";
        regions[3] = "CONMEBOL";
        regions[4] = "OFC";
        regions[5] = "UEFA";

        initialized = false;
    }

    /**
    The default constructor for QualifyingPanel.
    Initializes the regions array with strings.
     */
    public QualifyingPanel () {

        //for testing basically
        months = new MonthPanel[1];
        months[0] = new MonthPanel();

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

                newTab.add(teamName);
                newTab.add(teamPoints);
                newTab.add(teamRank);

                SpringLayout.Constraints cRank = layout.getConstraints(teamRank);
                cRank.setX(Spring.constant(10));
                cRank.setY(Spring.sum(Spring.constant(14 * (sortedArr.indexOf(team) + 1)),
                        con1.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cName = layout.getConstraints(teamName);
                cName.setX(con2.getConstraint(SpringLayout.WEST));
                cName.setY(Spring.sum(Spring.constant(14 * (sortedArr.indexOf(team) + 1)),
                        con2.getConstraint(SpringLayout.SOUTH)));

                SpringLayout.Constraints cPoints = layout.getConstraints(teamPoints);
                cPoints.setX(con3.getConstraint(SpringLayout.WEST));
                cPoints.setY(Spring.sum(Spring.constant(14 * (sortedArr.indexOf(team) + 1)),
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
    public void initPanel() {
        tabPane =  new JTabbedPane();
        cards = new JPanel[6];

        //placeholder
        //JPanel month = new JPanel();
        //month.add(new JLabel("This is a calendar"));

        tabPane.addTab("Matches by Month", months[0]);

        for(int i = 0; i < cards.length; i++) {
            JPanel subPanel = new JPanel();
            subPanel.add(new JLabel("Results coming soon for " + regions[i]));
            cards[i] = subPanel;
        }

        for(int i = 0; i < 6; i++) {

            tabPane.addTab(regions[i], cards[i]);
        }

        fillResults();

    }
}
