package Frontend;

import Backend.Match;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class MonthPanel extends JPanel {

    private java.time.LocalDate monthStart;

    private List<DayPanel> dayPanels;

    public MonthPanel(int year, int monthNum) {
        monthStart = java.time.LocalDate.of(year, monthNum, 1);
    }


    private class DayPanel extends JPanel {
        private List<Match> matches;
        private java.time.LocalDate date;

        public DayPanel(java.time.LocalDate date) {
            this.date = LocalDate.from(date);
        }

        private boolean classInv() {
            boolean flag = true;
            for (Match match : matches) {
                flag &= date.isEqual(match.getDate());
            }
            return flag;
        }

        public void addMatch(Match match){
            this.matches.add(match);
            assert classInv();
        }
    }
}
