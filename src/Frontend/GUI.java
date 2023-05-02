package Frontend;

import Backend.Team;
import Backend.WorldCupSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class containing the base graphical elements for the program as well as the entry point for the program.
 */
public class GUI extends JFrame implements ActionListener {

    private WorldCupSimulator gameSim;
    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JPanel startPanel;
    private JPanel qualifyingPanel;
    private JPanel groupPanel;
    private JPanel knockoutPanel;
    private JButton startButton;
    private JButton qualifyingButton;
    private JButton groupButton;
    private JButton knockoutButton;

    /**
     * Default constructor for GUI.  Calls initGUI to initialize instantiated objects.
     */
    public GUI() {
        //gameSim = new WorldCupSimulator();  WorldCupSimulator doesn't construct right yet

        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout());

        startPanel = new JPanel();
        //qualifyingPanel = new QualifyingPanel((gameSim.getTeams().toArray(new Team[0])));  // change once we get actual Teams
        qualifyingPanel = new QualifyingPanel(new Team[0]);
        groupPanel = new GroupPanel();
        knockoutPanel = new KnockoutPanel();

        startButton = new JButton();
        qualifyingButton = new JButton();
        groupButton = new JButton();
        knockoutButton = new JButton();

        initGUI();
    }

    /**
     * Entry point for code; creates a new GUI object with default constructor.
     * @param args
     */
    public static void main(String[] args) {
        GUI mainGUI = new GUI();
    }

    /**
     * Initializes member fields with values and sets up their parameters to display desired text.  Also adds subpanels
     * to the JFrame's content panel and adds additional components to each of the subpanels to display necessary
     * GUI elements for use of the GUI.  Binds actionListener to buttons and sets card key values for each of the
     * display panels.  Finally sets JFrame parameters to make the window visible and close properly.
     */
    private void initGUI() {
        startButton.setText("Start Simulation");
        qualifyingButton.setText("Qualifying Panel");
        groupButton.setText("Group Panel");
        knockoutButton.setText("Knockout Panel");

        startButton.addActionListener(this);
        qualifyingButton.addActionListener(this);
        groupButton.addActionListener(this);
        knockoutButton.addActionListener(this);

        qualifyingButton.setEnabled(false);
        groupButton.setEnabled(false);
        knockoutButton.setEnabled(false);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);

        startPanel.add(startButton);

        cardPanel.add(startPanel, "start");
        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");

        setSize(1600,900);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-1600)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-900)/2);
        setMinimumSize(new Dimension(1600,900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Sets actions to perform when each of the navigational buttons in the top panel of the GUI is pressed.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel;
        String panelString;

        if (e.getSource() == startButton) {
            panel = qualifyingPanel;
            panelString = "qual";
            qualifyingButton.setEnabled(true);
            groupButton.setEnabled(true);
            knockoutButton.setEnabled(true);
        } else if (e.getSource() == qualifyingButton) {
            panel = qualifyingPanel;
            panelString = "qual";
        } else if (e.getSource() == groupButton) {
            panel = groupPanel;
            panelString = "group";
        } else if (e.getSource() == knockoutButton) {
            panel = knockoutPanel;
            panelString = "knock";
        } else {
            panel = null;
            panelString = "";
        }
        checkIfPanelNeedsInit(panel);
        changeCard(cardPanel, panelString);
    }

    private void checkIfPanelNeedsInit(JPanel panel) {
        StagePanel objectSP;

        if (panel instanceof StagePanel) {
            objectSP = (StagePanel) panel;
            if (!objectSP.checkIfInitialized()) {
                objectSP.initPanel();
            }
        }
    }

    /**
     * Helper function to change the displayed panel.
     * @param cardPanel
     * @param panelString
     */
    private void changeCard(JPanel cardPanel, String panelString) {
        CardLayout cl;

        cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, panelString);
    }


    /*

    + createMainMenuWindow() : JPanel

+ createQualifierStageWindow(Match[], Team[]) : JPanel
 + createGroupStageWindow(Match[]) : JPanel
 + createKnockoutStageWindow(Match[]) : JPanel

     */


}
