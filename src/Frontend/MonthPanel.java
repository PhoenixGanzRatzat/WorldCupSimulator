package Frontend;

import Backend.Match;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthPanel extends JPanel {

    protected static final Color FG_COLOR = QualifyingPanel.ROW2_COLOR;

    private static final GridLayout LAYOUT_FIVE_ROW = new GridLayout(5, 7, 10, 10);
    private static final GridLayout LAYOUT_SIX_ROW = new GridLayout(6, 7, 10, 10);

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM u");

    private JPanel calendarPanel;
    private JPanel monthLabelPanel;
    private JLabel monthLabel;
    private List<DayPanel> dayPanels;

    private java.time.LocalDate monthStart;


    /**
     * Default Constructor
     */
    public MonthPanel() {
        this(2018, 1, Collections.emptyList());
    }

    /**
     * @param year     to initialize the MonthPanel to
     * @param monthNum to initialize the MonthPanel to
     * @param matches  to add to the DayPanels
     */
    public MonthPanel(int year, int monthNum, List<Match> matches) {
        ToolTipManager.sharedInstance().setInitialDelay(0);
//        ToolTipManager.sharedInstance().setDismissDelay(600);
        UIManager.put("ToolTip.font", GUI.TOOL_TIP_FONT);


        this.setLayout(new BorderLayout(0, 40));
        this.dayPanels = new ArrayList<>();
        this.monthLabelPanel = new JPanel();
        monthLabelPanel.setLayout(new BoxLayout(monthLabelPanel, BoxLayout.Y_AXIS));
        this.monthLabel = new JLabel();
        monthLabel.setFont(new Font("Default", Font.PLAIN, 24));
        monthLabel.setVerticalAlignment(SwingConstants.CENTER);
        monthLabelPanel.setBackground(FG_COLOR);
        monthLabelPanel.add(monthLabel);
        monthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.calendarPanel = new JPanel();
        calendarPanel.setPreferredSize(new Dimension(1000, 1000));
        calendarPanel.setLayout(LAYOUT_FIVE_ROW);
        this.add(monthLabelPanel, BorderLayout.NORTH);
        this.add(calendarPanel, BorderLayout.CENTER);

        setToMonth(year, monthNum);
        setMatchesOnDayPanels(matches);
    }

    /**
     * This method clears and resets the DayPanels to the month selected
     *
     * @param year
     * @param monthNum
     */
    public void setToMonth(int year, int monthNum) {
        this.monthStart = java.time.LocalDate.of(year, monthNum, 1);
        monthLabel.setText(monthStart.format(dateFormat));

        //if the first day of the month is not a sunday, we need empty space to pad out the grid cells
        //getDayOfWeek returns a java.time.DayOfWeek enum where 1 = Monday and 7 = Sunday
        //We want sunday to be the leftmost column, so do getDayOfWeek % 7 to find how many empty cells we need to pad
        int dayOffset = monthStart.getDayOfWeek().getValue() % 7;


        int daysInMonth = monthStart.lengthOfMonth();

        boolean sixRowsNeeded = daysInMonth + dayOffset > 35;

        if (sixRowsNeeded) {
            calendarPanel.setLayout(LAYOUT_SIX_ROW);
        } else {
            calendarPanel.setLayout(LAYOUT_FIVE_ROW);
        }

        calendarPanel.removeAll(); // make sure MonthPanel has no child components
        dayPanels = new ArrayList<>(); //reset daypanels to be empty
        for (int i = 0; i < daysInMonth + dayOffset; i++) {
            //when the first day of the month is not a sunday, there will be empty space in the calendar
            if (i < dayOffset) {
                //use a label for empty space
                calendarPanel.add(new JLabel());
            } else {
                //Grid cells are 0 indexed, but month days aren't. Subtract the offset to get the actual date.
                LocalDate date = monthStart.withDayOfMonth(i + 1 - dayOffset);
                DayPanel dayPanel = new DayPanel(date);
                dayPanels.add(dayPanel);
                calendarPanel.add(dayPanel, i);
            }
        }
        for (int i = daysInMonth + dayOffset; i < (sixRowsNeeded ? 42 : 35); i++) {
            calendarPanel.add(new JLabel());
        }
    }

    /**
     * This method adds matches to DayPanels
     *
     * @param matches to add
     */
    public void setMatchesOnDayPanels(List<Match> matches) {

        matches.stream().filter( //filter matches to only use matches from this year and month
                match ->
                        match.getMatchDate().getYear() == monthStart.getYear() && match.getMatchDate().getMonth().equals(monthStart.getMonth())
        ).forEach(match -> {
            DayPanel dayPanel = dayPanels.get(match.getMatchDate().getDayOfMonth() - 1);
            if (dayPanel != null) {
                dayPanel.addMatch(match);

                //reset color on panels
                dayPanel.setBackground(FG_COLOR);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (calendarPanel != null) {
            calendarPanel.setBackground(bg);
        }
    }

    /**
     * Inner class representing one day in a MonthPanel
     */
    private class DayPanel extends JPanel {
        private final java.time.LocalDate date;
        private List<Match> matches;
        private GridBagConstraints labelConstraints;

        /**
         * Creates a new DayPanel, and adds a date JLabel to it
         *
         * @param date to initialize the DayPanel to
         */
        public DayPanel(java.time.LocalDate date) {
            //MAX OF 7 MATCHES PER DAY
            this.setLayout(new GridBagLayout());
            this.setSize(new Dimension(100, 100));
            this.setBackground(MonthPanel.FG_COLOR);
            matches = new ArrayList<>();
            this.date = LocalDate.from(date);

            JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
            dayLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, -1, -1));
            labelConstraints = new GridBagConstraints();
            labelConstraints.anchor = GridBagConstraints.NORTHWEST;
            labelConstraints.fill = GridBagConstraints.HORIZONTAL;
            labelConstraints.gridx = 0; //position
            labelConstraints.gridy = 0; //position
            labelConstraints.insets = new Insets(0, 0, 0, 0);
            labelConstraints.gridwidth = 5;
            labelConstraints.gridheight = 1;
            labelConstraints.weightx = 0.8;
            labelConstraints.weighty = 1.0;

            addLabel(dayLabel);

            labelConstraints.weighty = 0; //make match labels go to bottom of screen
            labelConstraints.anchor = GridBagConstraints.SOUTH;
        }

        private void addLabel(JLabel label) {
            this.add(label, labelConstraints);
            labelConstraints.gridy = labelConstraints.gridy + 1;
        }

        private void addLabel(JLabel leftImage, JLabel matchLabel, JLabel rightImage) {
            labelConstraints.gridwidth = 1;
            Component leftStrut = Box.createHorizontalStrut(1);
            labelConstraints.anchor = GridBagConstraints.WEST;
            labelConstraints.weightx = 1;
            labelConstraints.gridx = 0;
            this.add(leftStrut, labelConstraints);
            labelConstraints.weightx = 0;
            labelConstraints.anchor = GridBagConstraints.EAST;
            labelConstraints.gridx = 1;
            this.add(leftImage, labelConstraints);
            labelConstraints.anchor = GridBagConstraints.SOUTH;
            labelConstraints.insets = labelConstraints.gridy == 0 ? new Insets(0, 5, 2, 5) : new Insets(0, 5, 0, 5);
            labelConstraints.gridx = 2;
            this.add(matchLabel, labelConstraints);
            labelConstraints.anchor = GridBagConstraints.WEST;
            labelConstraints.insets = new Insets(0, 0, 0, 0);
            labelConstraints.gridx = 3;
            this.add(rightImage, labelConstraints);
            Component rightStrut = Box.createHorizontalStrut(1);
            labelConstraints.anchor = GridBagConstraints.EAST;
            labelConstraints.weightx = 1;
            labelConstraints.gridx = 4;
            this.add(rightStrut, labelConstraints);
            labelConstraints.gridy = labelConstraints.gridy + 1;
        }

        /**
         * Assert that all matches in the DayPanel are actually on that date
         *
         * @return true if the class invariant is not violated
         */
        private boolean classInv() {
            boolean flag = true;
            for (Match match : matches) {
                flag &= date.isEqual(match.getMatchDate());
            }
            return flag;
        }

        private JLabel loadFlagLabel(String teamAbbv) {
            JLabel flagIcon = new JLabel();
            File imgFile = new File("Assets" + File.separator + "Images" + File.separator + "smallFlags" + File.separator + teamAbbv + ".png");
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(imgFile);
                flagIcon.setIcon(getScaledIconFromImage(bufferedImage));
            } catch (IOException e) {
                System.err.printf("can't find flag for %s\n", teamAbbv);
            }

            return flagIcon;
        }

        private ImageIcon getScaledIconFromImage(Image img) {
            double scaleFactor = 0.25;
            int iconX = (int) (img.getWidth(this) * scaleFactor);
            int iconY = (int) (img.getHeight(this) * scaleFactor);

            Image scaledPreviewImage = img.getScaledInstance(iconX, iconY, Image.SCALE_SMOOTH);
            BufferedImage image = new BufferedImage(iconX, iconY, BufferedImage.TYPE_INT_ARGB);

            image.getGraphics().drawImage(scaledPreviewImage, 0, 0, null);

            return new ImageIcon(image);
        }

        /**
         * Add a match to the DayPanel.
         * This method adds mouselisteners to each Match label to handle the tooltips
         *
         * @param match to add
         */
        private void addMatch(Match match) {
            this.matches.add(match);

            JLabel leftFlag = loadFlagLabel(match.getTeam1().getAbbv());
            leftFlag.setToolTipText(match.getTeam1().getName());
            JLabel rightFlag = loadFlagLabel(match.getTeam2().getAbbv());
            rightFlag.setToolTipText(match.getTeam2().getName());


            //add a mouseover to display JPopupMenu
            JLabel matchLabel = new JLabel(String.format("%s v. %s", match.getTeam1().getAbbv(), match.getTeam2().getAbbv()), JLabel.CENTER);
            matchLabel.setToolTipText(formatMatchToolTip(match));


            addLabel(leftFlag, matchLabel, rightFlag);
            assert classInv();

        }

        private String formatMatchToolTip(Match match) {
            String winnerScore;
            String loserScore;
            if (match.getTeam1Score() > match.getTeam2Score()) {
                winnerScore = String.valueOf(match.getTeam1Score());
                loserScore = String.valueOf(match.getTeam2Score());
            } else if (match.getTeam1Score() < match.getTeam2Score()) {
                winnerScore = String.valueOf(match.getTeam2Score());
                loserScore = String.valueOf(match.getTeam1Score());
            } else {
                winnerScore = String.valueOf(match.getTeam2Score()); // scores are the same for draws
                loserScore = String.valueOf(match.getTeam1Score());
            }
            if (match.getWinner() == null) {
                return String.format(
                        "<html>" +
                                "<p><u><b>" +
                                "%s v. %s" +
                                "</b></u>" +
                                "<br>" +
                                "Result: Draw" +
                                "<br>" +
                                "Score: %s - %s" +
                                "</p></html>",
                        match.getTeam1().getName(), match.getTeam2().getName(), winnerScore, winnerScore);


            } else {
                String winnerDisplayName = match.getWinner().getName();
                return String.format(
                        "<html>" +
                                "<p><u><b>" +
                                "%s v. %s" +
                                "</b></u>" +
                                "<br>" +
                                "Winner: %s" +
                                "<br>" +
                                "Score: %s - %s" +
                                "</p></html>",
                        match.getTeam1().getName(), match.getTeam2().getName(), winnerDisplayName, winnerScore, loserScore);

            }
        }
    }
}
