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

    private final static Color buttonTextColor = new Color(213, 226, 216);
    private final static Color fifaBG = new Color(50, 98, 149);


    /**
     * Default constructor for GUI.  Calls initGUI to initialize instantiated objects.
     */
    public GUI() {
        gameSim = new WorldCupSimulator();

        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout());

        startPanel = new JPanel(new GridBagLayout());
        gameSim.stageMatches(1); // TODO: return statement of method is ignored
        qualifyingPanel = new QualifyingPanel(gameSim.getTeams());

        // TODO: give group panel the matches/teams
        groupPanel = new GroupPanel(gameSim.stageMatches(2), gameSim.getTeams());

        // TODO: give knockoutpanel the matches/teams
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
    public static void main(String[] args) throws IOException {
        GUI mainGUI = new GUI();
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

        qualifyingButton.setVisible(false);
        groupButton.setVisible(false);
        knockoutButton.setVisible(false);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);

        layoutConstraints.insets = new Insets(10,10,10,10);
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;

        try {
            fifaLogoLabel = new JLabel(new ImageIcon(ImageIO.read(new File("Assets\\Images\\FIFA_logo.png"))));
        } catch (IOException e) {
            fifaLogoLabel = new JLabel("FIFA");
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        startPanel.add(fifaLogoLabel, layoutConstraints);

        startPanel.setBackground(fifaBG);

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;

        subHeaderLabel = new JLabel("World Cup 2018 Simulator");
        subHeaderLabel.setFont(new Font ("Arial Black", Font.PLAIN, 48));
        subHeaderLabel.setForeground(buttonTextColor);
        startPanel.add(subHeaderLabel, layoutConstraints);


        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 3;

        setButtonLook(startButton, buttonTextColor, fifaBG);

        startPanel.add(startButton, layoutConstraints);

        cardPanel.add(startPanel, "start");
        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");

        setTitle("World Cup Simulator");
        //setSize(1600,900);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-1600)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-900)/2);
        //setMinimumSize(new Dimension(1600,900));
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setButtonLook(JButton button, Color foreground, Color background) {
        button.setFocusPainted(false);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFont(new Font ("Arial Black", Font.PLAIN, 14));

    }

    /**
     * Sets actions to perform when each of the navigational buttons in the top panel of the GUI is pressed.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel;
        String panelString;
        Color panelBackground;
        Color buttonBackground;
        boolean makeBGBrighter;

        if (e.getSource() == startButton) {
            panel = qualifyingPanel;
            panelString = "qual";
            makeBGBrighter = false;
            qualifyingButton.setVisible(true);
            groupButton.setVisible(true);
            knockoutButton.setVisible(true);
        } else if (e.getSource() == qualifyingButton) {
            panel = qualifyingPanel;
            panelString = "qual";
            makeBGBrighter = false;
        } else if (e.getSource() == groupButton) {
            panel = groupPanel;
            panelString = "group";
            makeBGBrighter = true;
        } else if (e.getSource() == knockoutButton) {
            panel = knockoutPanel;
            panelString = "knock";
            makeBGBrighter = false;
        } else {
            panel = null;
            panelString = null;
            makeBGBrighter = false;
        }
        if (panel instanceof StagePanel) {
            checkIfPanelNeedsInit(panel);
            panelBackground = ((StagePanel)panel).getThemeColor();
            if  (makeBGBrighter) {
                buttonBackground = panelBackground.brighter();
            } else {
                buttonBackground = panelBackground.darker();
            }
            buttonPanel.setBackground(panelBackground);
            setButtonLook(qualifyingButton, buttonTextColor, buttonBackground);
            setButtonLook(groupButton, buttonTextColor, buttonBackground);
            setButtonLook(knockoutButton, buttonTextColor, buttonBackground);
            changeCard(cardPanel, panelString);
        }
    }

    private void checkIfPanelNeedsInit(JPanel panel) {
        if (panel instanceof StagePanel) {
            StagePanel stage = (StagePanel) panel;
            if (!stage.checkIfInitialized()) {
                if(stage instanceof KnockoutPanel) {
                    stage.initPanel(gameSim.stageMatches(3)); // TODO: hardcoded knockout stgae
                }
                stage.initPanel();
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

}
