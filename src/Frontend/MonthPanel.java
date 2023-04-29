package Frontend;

import Backend.Match;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class MonthPanel extends JPanel {

    private java.time.LocalDate monthStart;

    private List<DayPanel> dayPanels; //TODO: can probably use JPanel::getComponent(int)

    public MonthPanel() {
        this(2018, 1, Collections.emptyList());
    }

    public MonthPanel(int year, int monthNum, List<Match> matches) {
        this.setPreferredSize(new Dimension(1000, 800));
        this.setLayout(new GridLayout(5, 7, 10, 10));
        this.dayPanels = new ArrayList<>();

        setToMonth(year, monthNum);
        setMatchesOnDayPanels(matches);
    }

    private void setToMonth(int year, int monthNum) {
        this.monthStart = java.time.LocalDate.of(year, monthNum, 1);

        //if the first day of the month is not a sunday, we need empty space to pad out the grid cells
        //getDayOfWeek returns a java.time.DayOfWeek enum where 1 = Monday and 7 = Sunday
        //We want sunday to be the leftmost column, so do getDayOfWeek % 7 to find how many empty cells we need to pad
        int dayOffset = monthStart.getDayOfWeek().getValue() % 7;
        int daysInMonth = monthStart.lengthOfMonth();

        this.removeAll(); // make sure MonthPanel has no child components
        for (int i = 0; i < daysInMonth; i++) {
            //when the first day of the month is not a sunday, there will be empty space in the calendar
            if (i < dayOffset) {
                //use a label for empty space
                this.add(new JLabel());
            } else {
                //Grid cells are 0 indexed, but month days aren't. Subtract the offset to get the actual date.
                LocalDate date = monthStart.withDayOfMonth(i + 1 - dayOffset);
                DayPanel dayPanel = new DayPanel(date);
                dayPanels.add(dayPanel);
                this.add(dayPanel, i);
            }
        }
        for (int i = daysInMonth; i < 35; i++) {
            this.add(new JLabel());
        }
    }

    private void setMatchesOnDayPanels(List<Match> matches) {
        for (Match match : matches) {
            DayPanel dayPanel = dayPanels.get(match.getMatchDate().getDayOfMonth());
            if (dayPanel != null) {
                System.out.printf("adding match to day %d\n", match.getMatchDate().getDayOfMonth());
                dayPanel.addMatch(match);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private class DayPanel extends JPanel {
        private List<Match> matches;
        private final java.time.LocalDate date;

        private GridBagConstraints labelConstraints;

        public DayPanel(java.time.LocalDate date) {
            //MAX OF 7 MATCHES PER DAY
            this.setLayout(new GridLayout(8, 1)); //TODO: use gridbaglayout
            this.setSize(new Dimension(100, 100));
            matches = new ArrayList<>();
            this.date = LocalDate.from(date);

            this.add(new JLabel(String.valueOf(date.getDayOfMonth())));

            labelConstraints = new GridBagConstraints();
            labelConstraints.fill = GridBagConstraints.HORIZONTAL;
            labelConstraints.gridx = 0; //position
            labelConstraints.gridy = 0; //position
            labelConstraints.insets = new Insets(5, 5, 5, 5);
            labelConstraints.gridwidth = 1;
            labelConstraints.gridheight = 1;
            labelConstraints.weightx = 0.8;
            labelConstraints.weighty = 0.5;
        }

        private boolean classInv() {
            boolean flag = true;
            for (Match match : matches) {
                flag &= date.isEqual(match.getMatchDate());
            }
            return flag;
        }

        public void addMatch(Match match) {
            System.out.printf("adding match %s\n", match);
            this.matches.add(match);

            //TODO flags on match labels
            //add a mouseover to display JPopupMenu
            JLabel label = new JLabel();
            label.setText(String.format("%s | v. | %s", match.getTeamOne().getAbbv(), match.getTeamTwo().getAbbv()));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    System.out.printf("entered %s\n", label.getText());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    System.out.printf("exited %s\n", label.getText());
                }
            });
            this.add(label);
            assert classInv();

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
//            g.drawString(String.valueOf(date.getDayOfMonth()),0, 15);
        }

    }
}
