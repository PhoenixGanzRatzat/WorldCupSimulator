package Frontend;

import Backend.Team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JPanel qualifyingPanel;
    private JPanel groupPanel;
    private JPanel knockoutPanel;
    private JButton qualifyingButton;
    private JButton groupButton;
    private JButton knockoutButton;
    //temp attributes for pseudo match generation
    private static List<Team> teams;
    private static final DataLoader_temp dataLoader = new DataLoader_temp();
    //end of temp attributes

    public GUI() {
        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel();

        qualifyingPanel = new QualifyingPanel();
        groupPanel = new GroupPanel();
        knockoutPanel = new KnockoutPanel();

        qualifyingButton = new JButton("Qualifying Panel");
        groupButton = new JButton("Group Panel");
        knockoutButton = new JButton("Knockout Panel");

        initGUI();

    }

    public static void main(String[] args) {

        GUI mainGUI = new GUI();
        teams = dataLoader.loadTeamData();


    }

    private void initGUI() {

        qualifyingButton.addActionListener(this);
        groupButton.addActionListener(this);
        knockoutButton.addActionListener(this);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel);

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
