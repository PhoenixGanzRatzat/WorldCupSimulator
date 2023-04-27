package Frontend;

import Backend.Match;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.TableView;
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
    private JTextField roundNumberTextField;

    public static void main(String[] args) {
        GroupPanel panel = new GroupPanel();
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1800, 800);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public GroupPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 75));
        //this.setPreferredSize(new Dimension(100, 500));
        resultsField = new JTextArea();
        roundNumberTextField = new JTextField("1");
        initPanel();
    }

    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {
        /* Top bar across window for displaying current round */
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.GREEN);
        /* Center container for group results and group teams */
        JPanel displayPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        displayPanel.setBackground(Color.CYAN);
        /* Bottom bar across window for housing functions */
        JPanel functionPanel = new JPanel();
        functionPanel.setBackground(Color.magenta);

        /* Information Panel */
        JLabel infoLabel = new JLabel("Group Stage!  Round #");
        infoPanel.add(infoLabel);
        infoPanel.add(roundNumberTextField);

        /* __ Display Panel */
        // Container for all group panes - displays each teams w/d/l record and points
        JPanel groupDisplayPanel = new JPanel(new GridLayout(4,2,2,2));
        //groupDisplayPanel.setBackground(Color.YELLOW);
        for(int i = 0; i < 8; i++) {
            groupDisplayPanel.add(createGroupPanel(i));
        }
        // results sidepane - displays score and outcome between each match in the group
        JPanel resultsPanel = new JPanel();
        resultsPanel.setPreferredSize(new Dimension(200, 200));
        // TODO: Replace with JTextFields for each match in this format
        String resultsText =
                "A win  2 - 1 Loss B\n" +
                "A Draw 1 - 1 Draw C\n" +
                "A Loss 1 - 2 Win  D\n" +
                "B win  2 - 1 Loss C\n" +
                "B win  2 - 1 Loss D\n" +
                "C win  1 - 0 Loss D\n";
        resultsField.setText(resultsText);
        resultsPanel.add(new JLabel("__Group Results__"));
        resultsPanel.add(resultsField);
        // compose display panel
        displayPanel.add(groupDisplayPanel);
        displayPanel.add(resultsPanel);

        /* Function Panel */
        JButton nextRoundBTN = new JButton("Next Round");
        JButton completeStageBTN = new JButton("Complete Stage");
        functionPanel.add(nextRoundBTN);
        functionPanel.add(completeStageBTN);

        this.add(infoPanel, BorderLayout.NORTH);
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(functionPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the panel that holds the { win, loss, tie, points } data for each group
     * @param groupNumber
     * @return
     */
    private JPanel createGroupPanel(int groupNumber) {
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

        base.setBackground(Color.YELLOW);
        base.setPreferredSize(new Dimension(350, 150));
        base.setBorder(new LineBorder(Color.green));
        String groupLetter = String.valueOf((char)(groupNumber + 65)); // '65' = 'A' // TODO: maybe a String
        JLabel groupLabel = new JLabel(groupLetter);
        JPanel titlePane = new JPanel();
        titlePane.setLayout(new GridLayout(1,6,2,2));
        JPanel[] rowPanes = new JPanel[4];

        groupLabel.setBorder(new LineBorder(Color.green));
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
            l.setHorizontalAlignment(SwingConstants.CENTER);
            titlePane.add(l);
        }
        // next row for group panel is the title panel
        base.add(titlePane);

        // last is the 4 row panes
        for(int i = 0; i < 4; i++) {
            // initialize
            rowPanes[i] = new JPanel();
            rowPanes[i].setLayout(new GridLayout(1,6,2,2));
            rowPanes[i].setFont(new Font("Arial", Font.BOLD,10));

            // compose
            for(int j = 0; j < 6;j++) {
                JLabel label = new JLabel("*");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanes[i].add(label);
            }
            base.add(rowPanes[i]);
        }

        return base;
    }
}
