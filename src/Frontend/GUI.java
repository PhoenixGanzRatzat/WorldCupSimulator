package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private JPanel buttonPanel;
    private JPanel qualifyingPanel;
    private JPanel groupPanel;
    private JPanel knockoutPanel;
    private JButton qualifyingButton;
    private JButton groupButton;
    private JButton knockoutButton;

    public GUI() {
        cardPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel();
        // for testing
        qualifyingPanel = new JPanel();
        groupPanel = new JPanel();
        knockoutPanel = new JPanel();

        qualifyingPanel.add(new JLabel("Qualifying Panel"));
        groupPanel.add(new JLabel("Group Panel"));
        knockoutPanel.add(new JLabel("Knockout Panel"));

        // end testing

        qualifyingButton = new JButton("Qualifying Panel");
        qualifyingButton.addActionListener(this);
        groupButton = new JButton("Group Panel");
        groupButton.addActionListener(this);
        knockoutButton = new JButton("Knockout Panel");
        knockoutButton.addActionListener(this);

        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel);

        buttonPanel.add(qualifyingButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(knockoutButton);

        cardPanel.add(qualifyingPanel, "qual");
        cardPanel.add(groupPanel, "group");
        cardPanel.add(knockoutPanel, "knock");


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String panelString;
        CardLayout cl;
        if (e.getSource() == qualifyingButton) {
            panelString = "qual";
        } else if (e.getSource() == groupButton) {
            panelString = "group";
        } else if (e.getSource() == knockoutButton) {
            panelString = "knock";
        } else {
            panelString = "";
        }

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
