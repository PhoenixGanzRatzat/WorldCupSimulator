package Frontend;

import Backend.Team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class containing the base graphical elements for the program as well as the entry point for the program.
 */
public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JPanel qualifyingPanel;
    private JPanel groupPanel;
    private JPanel knockoutPanel;
    private JButton qualifyingButton;
    private JButton groupButton;
    private JButton knockoutButton;

    /**
     * Default constructor for GUI.  Calls initGUI to initialize instantiated objects.
     */
    public GUI() {
        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout());

        qualifyingPanel = new QualifyingPanel();
        groupPanel = new GroupPanel();
        knockoutPanel = new KnockoutPanel();

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
        qualifyingButton.setText("Qualifying Panel");
        groupButton.setText("Group Panel");
        knockoutButton.setText("Knockout Panel");

        qualifyingButton.addActionListener(this);
        groupButton.addActionListener(this);
        knockoutButton.addActionListener(this);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);

        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");

        setSize(640,480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Sets actions to perform when each of the navigational buttons in the top panel of the GUI is pressed.
     * @param e
     */
        @Override
    public void actionPerformed(ActionEvent e) {
        String panelString;

        if (e.getSource() == qualifyingButton) {
            panelString = "qual";
        } else if (e.getSource() == groupButton) {
            panelString = "group";
        } else if (e.getSource() == knockoutButton) {
            panelString = "knock";
        } else {
            panelString = "";
        }
        changeCard(cardPanel, panelString);
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
