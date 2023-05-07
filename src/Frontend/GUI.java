package Frontend;

import Backend.WorldCupSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Main class containing the base graphical elements for the program as well as the entry point for the program.
 */
public class GUI extends JFrame implements ActionListener {

    private WorldCupSimulator gameSim;
    public final static Font TOOL_TIP_FONT = new Font("Default", Font.PLAIN, 12);

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
    private JButton nextRoundButton;

    private final static Color text = new Color(213, 226, 216);
    private final static Color fifaBG = new Color(50, 98, 149);


    /**
     * Default constructor for GUI.  Calls initGUI to initialize instantiated objects.
     */
    public GUI() throws IOException {
        gameSim = new WorldCupSimulator();

        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout());

        startPanel = new JPanel(new GridBagLayout());
        gameSim.stageMatches(1); // TODO: return statement(List<Match>) of method is not used
        qualifyingPanel = new QualifyingPanel(gameSim.getTeams());

        // qualifying stage is completed at startup so teams and matches for group panel are available on startup
        groupPanel = new GroupPanel(gameSim.stageMatches(2), gameSim.getTeams());

        // gets updated after group stage
        knockoutPanel = new KnockoutPanel();

        startButton = new JButton();
        qualifyingButton = new JButton();
        groupButton = new JButton();
        knockoutButton = new JButton();
        nextRoundButton = new JButton();

        initGUI();
    }

    /**
     * Entry point for code; creates a new GUI object with default constructor.
     * @param args
     */
    public static void main(String[] args) throws IOException {
        GUI mainGUI = new GUI();
    }

    /**
     * Initializes member fields with values and sets up their parameters to display desired text.  Also adds subpanels
     * to the JFrame's content panel and adds additional components to each of the subpanels to display necessary
     * GUI elements for use of the GUI.  Binds actionListener to buttons and sets card key values for each of the
     * display panels.  Finally sets JFrame parameters to make the window visible and close properly.
     */
    private void initGUI() throws IOException {
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        JLabel fifaLogoLabel = new JLabel(new ImageIcon(ImageIO.read(new File("Assets\\Images\\FIFA_logo.png"))));
        JLabel subHeaderLabel = new JLabel();

        startButton.setText("Start Simulation");
        qualifyingButton.setText("Qualifying Panel");
        groupButton.setText("Group Panel");
        knockoutButton.setText("Knockout Panel");
        nextRoundButton.setText("Next Round");

        startButton.addActionListener(this);
        qualifyingButton.addActionListener(this);
        groupButton.addActionListener(this);
        knockoutButton.addActionListener(this);
        nextRoundButton.addActionListener(this);

        qualifyingButton.setVisible(false);
        groupButton.setVisible(false);
        knockoutButton.setVisible(false);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);
        buttonPanel.add(nextRoundButton);

        layoutConstraints.insets = new Insets(10,10,10,10);
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        startPanel.add(fifaLogoLabel, layoutConstraints);

        startPanel.setBackground(fifaBG);


        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;

        subHeaderLabel = new JLabel("World Cup 2018 Simulator");
        subHeaderLabel.setFont(new Font ("Arial Black", Font.PLAIN, 48));
        subHeaderLabel.setForeground(text);
        startPanel.add(subHeaderLabel, layoutConstraints);


        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 3;

        startButton.setFocusPainted(false);
        startButton.setForeground(text);
        startButton.setBackground(fifaBG);
        startButton.setFont(new Font ("Arial Black", Font.PLAIN, 14));

        startPanel.add(startButton, layoutConstraints);

        cardPanel.add(startPanel, "start");
        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");

        setTitle("World Cup Simulator");
        // setSize(1600,900);
        // setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-1600)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-900)/2);
        // setMinimumSize(new Dimension(1600,900));
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Sets actions to perform when each of the navigational buttons in the top panel of the GUI is pressed.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            checkIfPanelNeedsInit(qualifyingPanel);
            qualifyingButton.setVisible(true);
            groupButton.setVisible(true);
            knockoutButton.setVisible(true);
            changeCard(cardPanel, "qual");
        } else if (e.getSource() == qualifyingButton) {
            checkIfPanelNeedsInit(qualifyingPanel);
            changeCard(cardPanel, "qual");
        } else if (e.getSource() == groupButton) {
            checkIfPanelNeedsInit(groupPanel);
            changeCard(cardPanel, "group");
        } else if (e.getSource() == knockoutButton) {
            checkIfPanelNeedsInit(knockoutPanel);
            changeCard(cardPanel, "knock");
        } else if (e.getSource() == nextRoundButton) {
            if (cardPanel.getComponent(2).isVisible()) {
                ((GroupPanel) groupPanel).simulateRound();
            } else if (cardPanel.getComponent(3).isVisible()) {
                ((KnockoutPanel) knockoutPanel).nextRound();
            }

        }
    }

    private void checkIfPanelNeedsInit(JPanel panel) {
        // initialize new panels
        if( ! (((StagePanel) panel).checkIfInitialized()) ) {
            ((StagePanel) panel).initPanel();
        }

        // when group panel is complete then load teams and matches into knockout panel
        if(((GroupPanel) groupPanel).checkIfCompleted()) {
            gameSim.stageMatches(3);
            ((KnockoutPanel) knockoutPanel).setStage(gameSim.getBrackets());
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
