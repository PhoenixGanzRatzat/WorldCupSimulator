package Frontend;

import Backend.Match;
import Backend.Team;

import javax.swing.*;
import java.awt.*;


public class QualifyingPanel extends JPanel {

    private Match[] matches;
    private Team[] teams;
    private MonthPanel[] months;
    private JPanel stage; //
    private JPanel[] cards;
    //ough IDK
    private String[] regions = new String[6];
    private JTabbedPane tabPane;

    public QualifyingPanel (Team[] teamIn) {
        //matches = matchIn;
        teams = teamIn;

    }
    public QualifyingPanel () {

        regions[0] = "AFC";
        regions[1] = "CAF";
        regions[2] = "CONCACAF";
        regions[3] = "CONMEBOL";
        regions[4] = "OFC";
        regions[5] = "UEFA";

    }



    public JTabbedPane createPanel() {
        tabPane =  new JTabbedPane();
        //placeholder
        JPanel month = new JPanel();
        month.add(new JLabel("This is a calendar"));

        tabPane.addTab("Matches by Month", month);
        JPanel tab;

        for(int i = 0; i < 6; i++) {
            tab = new JPanel();
            JPanel subPanel = new JPanel();
            subPanel.add(new JLabel("These are the " + regions[i] + " results"));
            tab.add(subPanel);
            tabPane.addTab(regions[i], tab);
        }

        return tabPane;

    }

    /*
    Relevant numbers:
    index 0 = Month Panel
    index 1 - 6 = regions array in order
     */
    public void fillResults() {
        SpringLayout layout = new SpringLayout();
        for(int i = 1; i < tabPane.getTabCount() - 1; i++) {
            if(tabPane.getComponentAt(i) instanceof JPanel) {

               JPanel subPanel = (JPanel) tabPane.getComponentAt(i);
               subPanel.setLayout(layout);


           }
        }
    }



}
