package Frontend;

import Backend.Match;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The group stage panel
 */
public class GroupPanel extends JPanel implements StagePanel {
    /* SIMULATES */
    // TODO: stand-in for displaying flags
    BufferedImage[] flags = new BufferedImage[211];
    Match[] matches = new Match[48];






    /* __FIELD VARIABLES__ */
    private JTextArea resultsField;

    public static void main(String[] args) {

    }

    public GroupPanel() {
        this.setBackground(new Color(0, 0, 75));
        this.setPreferredSize(new Dimension(1800, 800));
        resultsField = new JTextArea();
    }

    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {
        /* Top bar across window for displaying current round */
        JPanel infoPanel = new JPanel();
        /* Center container for group results and group teams */
        JPanel displayPanel = new JPanel();
        /* Bottom bar across window for housing functions */
        JPanel functionPanel = new JPanel();

        /* which teams are in which group - Panels */
        JPanel[] groupsDisplayPanel = new JPanel[8];
        for(int i = 0; i < groupsDisplayPanel.length; i++) {
            groupsDisplayPanel[i] = createGroupPanel(i);
        }

        /* results sidepane - displays score and outcome between each match in the group */
        JPanel resultsPanel = new JPanel();
        JLabel resultsLabel = new JLabel("Group Results");
        resultsPanel.add(resultsLabel);
        resultsPanel.add(resultsField);


    }

    /**
     * Creates the panel that holds the { win, loss, tie, points } data for each group
     * @param groupNumber
     * @return
     */
    private JPanel createGroupPanel(int groupNumber) {
        String groupLetter = String.valueOf((char)(groupNumber + 65)); // '65' = 'A' // TODO: maybe a String

        JPanel base = new JPanel(new GridLayout(5,1,2,2));
        JLabel[] flags = new JLabel[4];
        JLabel titlePane = new JLabel();
        JLabel[] countryNames = new JLabel[4];
        JLabel[] rowPanes = new JLabel[4];
        JLabel groupLabel = new JLabel(groupLetter);

        // The top of the panel has the group letter displayed
        base.add(groupLabel);

        /*
            __ Title pane __
            Position - Country Name - Wins - Draws - Losses - Points
        */
        JLabel[] titles = new JLabel[6];
        titles[0] = new JLabel("Position");
        titles[1] = new JLabel("Country");
        titles[2] = new JLabel("Wins");
        titles[3] = new JLabel("Draws");
        titles[4] = new JLabel("Losses");
        titles[5] = new JLabel("Points");
        for(JLabel l : titles) {
            titlePane.add(l);
        }

        // Group panel components
        for(int i = 0; i < 4; i++) {
            // initialize
            flags[i] = new JLabel();
            countryNames[i] = new JLabel();
            rowPanes[i] = new JLabel();
            // compose
            rowPanes[i].add(flags[i]);
            rowPanes[i].add(countryNames[i]);
            base.add(rowPanes[i]);
        }

        return base;
    }
}
