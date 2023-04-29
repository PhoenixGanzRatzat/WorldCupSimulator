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

    private List<DayPanel> dayPanels;

    private List<Match> matches;

    public MonthPanel() {
        this(2018, 1, Collections.emptyList());
    }

    public MonthPanel(int year, int monthNum, List<Match> matches) {
        this.setPreferredSize(new Dimension(1000, 800));
        this.setLayout(new GridLayout(5, 7, 10, 10));
        this.dayPanels = new ArrayList<>();

        this.monthStart = java.time.LocalDate.of(year, monthNum, 1);
        int dayOffset = monthStart.getDayOfWeek().getValue() % 7; //getDayOfWeek returns a java.time.DayOfWeek enum where 1 = Monday and 7 = Sunday
        int daysInMonth = monthStart.lengthOfMonth();

        for (int i = 0; i < daysInMonth; i++) {
            if (i < dayOffset) {
                this.add(new JLabel());

            } else {
                LocalDate date = monthStart.withDayOfMonth(i + 1 - dayOffset);
                DayPanel dayPanel = new DayPanel(date);
                dayPanels.add(dayPanel);
                this.add(dayPanel, i);
            }
        }
        for (int i = daysInMonth; i < 35; i++) {
            this.add(new JLabel());
        }
        for (Match match : matches) {
            DayPanel dayPanel = dayPanels.get(match.getDate().getDayOfMonth());
            if (dayPanel != null) {
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
        private java.time.LocalDate date;

        private GridBagConstraints labelConstraints;

        public DayPanel(java.time.LocalDate date) {
            this.setLayout(new GridBagLayout());
            this.setSize(new Dimension(100, 100));
            matches = new ArrayList<>();
            this.date = LocalDate.from(date);

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
                flag &= date.isEqual(match.getDate());
            }
            return flag;
        }

        public void addMatch(Match match) {
            System.out.printf("adding match %s\n", match);
            this.matches.add(match);

            //TODO for every match, make a JLabel with "Team1Abbv | v. | Team2Abbv"
            //add a mouseover to display JPopupMenu
            JLabel label = new JLabel();
            label.setText(String.format("%s | v. | %s", match.getTeam1(), match.getTeam2()));
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
            this.add(label, labelConstraints);
            assert classInv();

        }

        @Override
        protected void paintComponent(Graphics g) {
            System.out.printf("x: %d, y: %d\n", this.getX(), this.getY());
            System.out.printf("width: %d, height: %d\n", getWidth(), getHeight());
            super.paintComponent(g);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
//            g.drawString(String.format("%d/%d/%d\n%d matches", this.date.getMonthValue(), this.date.getDayOfMonth(), this.date.getYear(), matches.size()), 0, 0);

        }

    }
}
