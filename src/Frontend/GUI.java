package Frontend;

import Backend.WorldCupSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static Frontend.StagePanel.*;

/**
 * Main class containing the base graphical elements for the program as well as the entry point for the program.
 */
public class GUI extends JFrame implements ActionListener {

    private WorldCupSimulator gameSim;
    public final static Font TOOL_TIP_FONT = new Font("Default", Font.PLAIN, 12);

    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JPanel startPanel;
    private QualifyingPanel qualifyingPanel;
    private GroupPanel groupPanel;
    private KnockoutPanel knockoutPanel;
    private JButton startButton;
    private JButton qualifyingButton;
    private JButton groupButton;
    private JButton knockoutButton;

    /**
     * Default constructor for GUI.  Calls initGUI to initialize instantiated objects.
     */
    public GUI() {
        gameSim = new WorldCupSimulator();

        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout());
        startPanel = new JPanel(new GridBagLayout());

        qualifyingPanel = new QualifyingPanel();
        groupPanel = new GroupPanel();
        knockoutPanel = new KnockoutPanel();

        startButton = new JButton();
        qualifyingButton = new JButton();
        groupButton = new JButton();
        knockoutButton = new JButton();
    }

    /**
     * Entry point for code; creates a new GUI object with default constructor.
     *
     * @param args
     */
    public static void main(String[] args) {
        GUI mainGUI = new GUI();
        mainGUI.initGUI();
    }

    /**
     * Initializes member fields with values and sets up their parameters to display desired text.  Also adds subpanels
     * to the JFrame's content panel and adds additional components to each of the subpanels to display necessary
     * GUI elements for use of the GUI.  Binds actionListener to buttons and sets card key values for each of the
     * display panels.  Finally, sets JFrame parameters to make the window visible and close properly.
     */
    private void initGUI() {
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        JLabel fifaLogoLabel;
        JLabel subHeaderLabel;

        startButton.setText("Start Simulation");
        qualifyingButton.setText("Qualifying Stage");
        groupButton.setText("Group Stage");
        knockoutButton.setText("Knockout Stage");

        startButton.addActionListener(this);
        qualifyingButton.addActionListener(this);
        groupButton.addActionListener(this);
        knockoutButton.addActionListener(this);

        buttonPanel.setBackground(fifaBlue);
        setButtonLook(startButton, buttonText, buttonBackground);
        setButtonLook(qualifyingButton, buttonText, buttonBackground);
        setButtonLook(groupButton, buttonText, buttonBackground);
        setButtonLook(knockoutButton, buttonText, buttonBackground);
        qualifyingButton.setVisible(false);
        groupButton.setVisible(false);
        knockoutButton.setVisible(false);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);

        layoutConstraints.insets = new Insets(10, 10, 10, 10);
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;

        try {
            fifaLogoLabel = new JLabel(new ImageIcon(ImageIO.read(new File("Assets"+File.separator+"Images"+File.separator+"FIFA_logo.png"))));
        } catch (IOException e) {
            fifaLogoLabel = new JLabel("FIFA");
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        startPanel.add(fifaLogoLabel, layoutConstraints);

        startPanel.setBackground(fifaBlue);

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;

        subHeaderLabel = new JLabel("World Cup 2018 Simulator");
        subHeaderLabel.setFont(new Font ("Arial Black", Font.PLAIN, 48));
        subHeaderLabel.setForeground(buttonText);
        startPanel.add(subHeaderLabel, layoutConstraints);

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 3;

        startPanel.add(startButton, layoutConstraints);

        cardPanel.add(startPanel, "start");
        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");

        setTitle("World Cup Simulator");
        setSize(1600, 900);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 1600) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - 900) / 2);
        setMinimumSize(new Dimension(1024, 768));
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Configures GUI buttons to have a common look and feel
     * @param button
     * @param foreground
     * @param background
     */
    private void setButtonLook(JButton button, Color foreground, Color background) {
        button.setFocusPainted(false);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFont(new Font("Arial Black", Font.PLAIN, 14));
        button.setBorder(new BevelBorder(BevelBorder.RAISED));
        button.setPreferredSize(new Dimension(150, 35));
    }

    /**
     * Sets actions to perform when each of the navigational buttons in the top panel of the GUI is pressed.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String panelString;
        int stage;

        if (e.getSource() == startButton) {
            panelString = "qual";
            qualifyingButton.setVisible(true);
            groupButton.setVisible(true);
            knockoutButton.setVisible(true);
            stage = 1;
        } else if (e.getSource() == qualifyingButton) {
            panelString = "qual";
            stage = 1;
        } else if (e.getSource() == groupButton) {
            panelString = "group";
            stage = 2;
        } else if (e.getSource() == knockoutButton) {
            panelString = "knock";
            stage = 3;
        } else {
            panelString = null;
            stage = 0;
        }
        moveToStage(panelString, stage);
    }

    /**
     * Handles changing cards in the cardPanel, and ensures that panels do not get initialized more than once, and that
     * previous stage panels are complete.
     * @param panelString
     * @param stage
     */
    private void moveToStage(String panelString, int stage) {
        if (stage == 1) {
            checkIfPanelNeedsInit(qualifyingPanel, stage);
            changeCard(cardPanel, panelString);
        } else if (stage == 2) {
            if(qualifyingPanel.checkIfCompleted()) {
                checkIfPanelNeedsInit(groupPanel, stage);
                changeCard(cardPanel, panelString);
            } else {
                JOptionPane.showMessageDialog(this, "Qualifying Stage must be completed before moving to Group Stage!");
            }
        } else if (stage == 3) {
            if(groupPanel.checkIfCompleted()) {
                checkIfPanelNeedsInit(knockoutPanel, stage);
                changeCard(cardPanel, panelString);
            } else {
                JOptionPane.showMessageDialog(this, "Group Stage must be completed before moving to Knockout Stage!");
            }
        }
    }

    /**
     * Checks to see if panel is initialized, and initializes if not.
     * @param panel
     * @param stage
     */
    private void checkIfPanelNeedsInit(StagePanel panel, int stage) {
       if (!panel.checkIfInitialized()){
           panel.initPanel(gameSim.stageMatches(stage), gameSim.getTeams());
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

}
