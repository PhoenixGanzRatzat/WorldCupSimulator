package Frontend;

import Backend.Match;
import Backend.Team;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays KnockoutStage results either one match at a time or
 * one round at a time, should the user choose to skip forward.
 */
public class KnockoutPanel extends JPanel implements StagePanel, ActionListener {
    // round constants
    private static final int ROUND_OF_SIXTEEN = 0;
    private static final int QUARTERFINAL = 1;
    private static final int SEMIFINAL = 2;
    private static final int FINAL = 3;
    private static final int WINNER = 4;
    // colors
    private final static Color canvas = new Color(17, 132, 39);
    private final static Color text = new Color(213, 226, 216);
    private final static Color stroke = new Color(163, 207, 172);

    private boolean initialized;
    private MatchCell[][] cells;
    private List<Match> matches;
    private Color themeColor;

    /**
     * Default constructor; initializes JPanel with BorderLayout, sets size,
     * initializes matches, initializes init boolean, & calls createWindow()
     */
    public KnockoutPanel() {
        super(new GridBagLayout(), true);
        this.cells = new MatchCell[][]{new MatchCell[8], new MatchCell[4], new MatchCell[2], new MatchCell[2], new MatchCell[2]};
        this.setBackground(canvas);
        this.initialized = false;
        themeColor = canvas;
        createWindow();
    }
    /*TODO:
     * Implement initPanel(), nextMatch(), & nextRound()
     * Make those 90 lines of addSpacer() calls into 10 lines of batchSpacers() calls
     * Write a loop in batchSpacers() iterated to the length of the shorter array passed in
     * Bracket layout changes: either turn Matches into two BracketCells stacked or make BracketCell "double up" by default
     */
    /**
     * Assembles a GridBagLayout-based bracket of BracketCell objects and buttons for the teams competing in each match
     */
    private void createWindow() {
        GridBagConstraints bracket = new GridBagConstraints();
        bracket.insets = new Insets(0,0,0,0);
        bracket.weighty = 0;
        bracket.fill = 2;
        bracket.gridwidth = 1;
        bracket.gridheight = 1;
        bracket.anchor = GridBagConstraints.CENTER;
        /*
         *
         *    << Bracket Column Legend with GridBagConstraints coordinates >>
         *
         *       0  1  2  3  4  5  6  7  8  9 10
         *      _L _L _L _L _L _L _L _L _L _L _L
         *   0 |                                |
         *   1 |   ■■                           |
         *   2 |         ■■                     |
         *   3 |   ■■                           |
         *   4 |                                |
         *   5 |   ■■          ■■          ■■   |
         *   6 |         ■■          ■■         |
         *   7 |   ■■                           |
         *   8 |                                |
         *   9 |   ■■                      ■■   |
         *  10 |         ■■          ■■         |
         *  11 |   ■■          ■■               |
         *  12 |                                |
         *  13 |   ■■                           |
         *  14 |         ■■                     |
         *  15 |   ■■                           |
         *  16 |_L _L _L _L _L _L _L _L _L _L _L|
         *
         *        by Naomi                                 4/28/23
         */
        int[][] row = new int[][]{new int[]{1, 3, 5, 7, 10, 12, 14, 16},
                                  new int[]{2, 6, 11, 15},
                                  new int[]{5, 12},
                                  new int[]{6, 11},
                                  new int[]{5, 10}
        };
        for (int i = 0; i < 8; i++) {
            // first round
            cells[ROUND_OF_SIXTEEN][i] = new MatchCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN);
            bracket.gridx = 1;
            bracket.gridy = row[ROUND_OF_SIXTEEN][i];
            this.add(this.cells[ROUND_OF_SIXTEEN][i], bracket);
            if (i < 4) {
                // quarterfinal
                cells[QUARTERFINAL][i] = new MatchCell(row[QUARTERFINAL][i], QUARTERFINAL);
                bracket.gridx = 3;
                bracket.gridy = row[QUARTERFINAL][i];
                this.add(this.cells[QUARTERFINAL][i], bracket);
            }
            if (i < 2) {
                // semifinal
                cells[SEMIFINAL][i] = new MatchCell(row[SEMIFINAL][i], SEMIFINAL);
                bracket.gridx = 5;
                bracket.gridy = row[SEMIFINAL][i];
                this.add(this.cells[SEMIFINAL][i], bracket);
                // final
                cells[FINAL][i] = new MatchCell(row[FINAL][i], FINAL);
                bracket.gridx = 7;
                bracket.gridy = row[FINAL][i];
                this.add(this.cells[FINAL][i], bracket);
                // winner
                cells[WINNER][i] = new MatchCell(row[WINNER][i], WINNER);
                bracket.gridx = 9;
                bracket.gridy = row[WINNER][i];
                this.add(this.cells[WINNER][i], bracket);
            }
        }
        for(MatchCell[] column : cells){
            for(MatchCell cell : column) for(JButton button : cell.getTeamLabel()){
                button.addActionListener(this);
                int col = cell.getRound() + 1;
                button.setActionCommand(col+","+cell.getRow());
                cell.setCellRevealed(false);
            }
        }
        // remainder of method is spacers between cells

        // 1x18 horizontal: four each padding the two groups of four outermost columns of cells (8x total)
        addSpacer(0,0,18,1,0);
        addSpacer(2,0,18,1,0);
        addSpacer(4,0,18,1,0);
        addSpacer(6,0,18,1,0);
        addSpacer(8,0,18,1,0);
        addSpacer(10,0,18,1,0);
        // 1x2 vertical: two each padding the top and bottom of QUARTERFINAL columns,
        // and one each in the center of the two ROUND_OF_SIXTEEN columns (4x total)
        addSpacer(3,0,2,0,1);
        addSpacer(3,16,2,0,1);
        addSpacer(1,8,2,0,1);
        addSpacer(9,6,4,0,1);
        // 1x1 vertical: padding the rows of both ROUND_OF_SIXTEEN columns except the two center rows (16x total)
        addSpacer(1,0,1,0,1);
        addSpacer(1,2,1,0,0.1);
        addSpacer(1,4,1,0,0.1);
        addSpacer(1,6,1,0,0);
        //addSpacer(1,11,1,1,0,0.1); // Removing specifically these two fixed an issue I was having.
        //addSpacer(15,11,1,1,0,0.1); // I have learned nothing from this. I want to scream.
        addSpacer(1,13,1,0,0.1);
        addSpacer(1,15,1,0,0.1);
        addSpacer(1,17,1,0,1);
        // 1x3 vertical: three each between the four rows of QUARTERFINAL columns (6x total)
        addSpacer(3,3,3,0,1);
        addSpacer(3,12,3,0,1);

        // 1x4 vertical: one each between finals and quarterfinals rows (2x total)
        addSpacer(3,7,4,0,1);
        addSpacer(7,7,4,0,1);

        // 1x5 vertical: two each padding the top and bottom of SEMIFINAL columns (4x total)
        addSpacer(5,0,5,0,1);
        addSpacer(5,13,5,0,1);

        // 1x6 vertical: two each padding the top and bottom of the finals' columns,
        addSpacer(7,0,7,0,1);
        addSpacer(9,0,5,0,1);
        addSpacer(7,11,7,0,1);
        addSpacer(9,11,7,0,1);
        // 1x6 vertical: at center of finals column (2x total)
        addSpacer(5,6,6,0,1);

        revalidate();
        repaint();
    }
    /**
     * Creates an empty, transparent JPanel for use as a spacer.
     * Intended to simplify repetitive use of GridBagConstraints by accepting only 6 parameters instead of 11
     *
     */
    private void addSpacer(int gX, int gY, int gH, double wX, double wY){
        //JPanel spacer = new JPanel(true);
        JComponent spacer = (JComponent) Box.createVerticalStrut(1);

        if(wX!=0) {
            spacer = (JComponent) Box.createHorizontalStrut(1);
        }
        /*
        // Makes things really ugly, but good for visual troubleshooting

        spacer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1,0,0,1),BorderFactory.createLineBorder(Color.RED, 1, false)));
        spacer.setToolTipText("<html>" + "(" + gX + "," + gY + ")" + "<br>" + "1x" + gH + "</html>");
        */
        this.add(spacer, new GridBagConstraints(gX,gY,1,gH,wX,wY,10,1,(new Insets(0,0,0,0)),0,0));
    }
    private void batchSpacers(int[] gX, int[] gY, int gW, int gH, boolean isVert){
        if(gY.length<gX.length){
            for(int i = 0; i < gY.length; i++){
                //for(){
                    //addSpacer(gX[])
                //}
            }
        }
    }
    private void addMatch(int round, int i){

    }
    public void nextMatch() {
    }
    public void nextRound() {
    }
    private List<Line2D.Double> createStripes(){
        List<Line2D.Double> stripes = new ArrayList<>();
        double[] columns = new double[5];
        double xL, xR, y1, y2;
        int n = -1;
        for (MatchCell[] column : cells) {
            n++;
            for (MatchCell cell : column) {
                Rectangle2D box = cell.getBounds().getBounds2D();
                Point2D origin = new Point2D.Double(box.getCenterX(), box.getCenterY());
                cell.setCellOrigin(origin);
            }
            columns[n] = column[0].getCellOrigin().getX();
        }
        for (int i = 0; i < 8; i++) {
            // round of sixteen (horizontal)
            xL = columns[0];
            xR = (columns[0] + columns[1]) / 2;
            y1 = cells[ROUND_OF_SIXTEEN][i].getCellOrigin().getY();
            stripes.add(new Line2D.Double(xL, y1, xR, y1));
            if (i%2==0) {
                // round of sixteen (vertical)
                y2 = cells[ROUND_OF_SIXTEEN][i + 1].getCellOrigin().getY();
                stripes.add(new Line2D.Double(xR, y1, xR, y2));
            }
            if (i<4) {
                // quarterfinal (horizontal)
                xL = xR;
                xR = (columns[1] + columns[2]) / 2;
                y1 = cells[QUARTERFINAL][i].getCellOrigin().getY();
                stripes.add(new Line2D.Double(xL, y1, xR, y1));
                if(i%2==0) {
                    // quarterfinal (vertical)
                    y2 = cells[QUARTERFINAL][i+1].getCellOrigin().getY();
                    stripes.add(new Line2D.Double(xR, y1, xR, y2));
                }
            }
            if (i<2) {
                // semifinal (vertical)
                xL = xR;
                xR = (columns[2] + columns[3]) / 2;
                y1 = cells[SEMIFINAL][i].getCellOrigin().getY();
                stripes.add(new Line2D.Double(xL, y1, xR, y1));
                if (i%2==0) {
                    // semifinal (horizontal)
                    y2 = cells[SEMIFINAL][i + 1].getCellOrigin().getY();
                    stripes.add(new Line2D.Double(xR, y1, xR, y2));
                }
                //final (horizontal)
                xL = xR;
                xR = columns[4];
                y1 = cells[FINAL][i].getCellOrigin().getY();
                stripes.add(new Line2D.Double(xL, y1, xR, y1));
                //final (vertical)
                y2 = cells[WINNER][i].getCellOrigin().getY();
                stripes.add(new Line2D.Double(xR, y1, xR, y2));
            }
        }
        return stripes;
    }
    @Override
    public boolean checkIfCompleted() {
        return initialized;
    }
    @Override
    public boolean checkIfInitialized() {
        return initialized;
    }

    @Override
    public Color getThemeColor() {
        return themeColor;
    }

    @Override
    public void initPanel() {
        initialized = true;
    }
    public void initPanel(List<Match> matchList){
        this.matches = new ArrayList<>();
        matches.addAll(matchList);
        matches.add(matches.get(matches.size()-2));
        matches.add(matches.get(matches.size()-2));
        System.out.print("Matches x" + matches.size() + ". ");
        int round = 0;
        int row = 0;
        for(Match m : matches){
            System.out.println(round+","+row);
            MatchCell cell = cells[round][row];
            cell.setMatch(m);
            row++;
            if(row==cells[round].length){
                round++;
                row = 0;
            }
            System.out.println(round);
            if(round==5){
                System.out.print("Too many matches!!");
                break;
            }
        }
        System.out.println();
        initialized = true;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        MatchCell eventCell = (MatchCell)((JButton)(e.getSource())).getParent();

    }
    @Override
    public void paintComponent(Graphics g){
        Graphics2D striper = (Graphics2D) g;
        super.paintComponent(striper);
        striper.setColor(stroke);
        striper.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        if(initialized){
            for(Line2D.Double stripe : createStripes()){
                striper.draw(stripe);
            }
        }
    }

    /**
     * Creates small JPanels with two JLabels next to each other,
     * the first of which having an ImageIcon created by scaling
     * a BufferedImage of a flag with a separate method. Also has
     * accessor methods so initPanel can change things when called.
     */
    private class MatchCell extends JPanel {
        private String[] flagPath, teamName;
        private int[] scoreValue;
        private Match match;
        private Team[] team;
        private JLabel champ;
        private JLabel[] flag, scoreLabel;
        private JButton[] teamLabel;
        private Dimension cellSize;
        private GridBagConstraints cellConstraints;
        private BufferedImage[] source;
        private Point2D cellOrigin;
        private Font cellFont;
        private Color cellText;
        private boolean cellRevealed, origin;
        private int row, round, imageWidth, imageHeight, flagWidth, flagHeight;

        /**
         * Default constructor, calls secondary constructor with all-zero args.
         */
        private MatchCell() {
            this(0, 0);
        }
        /** Secondary BracketCell constructor taking only two parameters, calls
         * main constructor with preset height/width for images and flags.
         * Is the main constructor in use by KnockoutPanel.
         * */
        private MatchCell(int row, int round) {
            this(row, round, 70, 46, 40, 26);
        }
        private MatchCell(int row, int round, int imageWidth, int imageHeight, int flagWidth, int flagHeight) {
            super(new GridBagLayout(), true);
            this.row = row;
            this.round = round;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.flagWidth = flagWidth;
            this.flagHeight = flagHeight;
            this.origin = false;
            this.cellRevealed = true;
            this.cellFont = new Font ("Arial Black", Font.PLAIN, 14);
            this.cellText = text;
            this.champ = new JLabel();
            this.flagPath = new String[isMatch()? 2:1];
            this.scoreValue = new int[isMatch()? 2:1];
            this.teamName = new String[isMatch()? 2:1];
            this.flag = new JLabel[isMatch()? 2:1];
            this.scoreLabel =  new JLabel[isMatch()? 2:1];
            this.teamLabel =  new JButton[isMatch()? 2:1];
            this.source = new BufferedImage[isMatch()? 2:1];
            // set size, background, and border
            this.cellSize = new Dimension(100+this.flagWidth, (this.flagHeight*2)+2);
            this.setMinimumSize(cellSize);
            this.setMaximumSize(cellSize);
            this.setBackground(canvas.darker());
            this.setBorder(BorderFactory.createLineBorder(stroke, 2, true));
            this.cellConstraints = new GridBagConstraints();
            createCell();
        }
        private void createCell(){
            for(int i = 0; i < (isMatch()? 2:1); i++) {
                // initialize flag
                this.flag[i] = new JLabel();
                this.flag[i].setOpaque(false);
                cellConstraints.gridx = 0;
                cellConstraints.gridy = (isMatch() && i == 0) ? 0 : 2;
                cellConstraints.weightx = 0;
                cellConstraints.weighty = 1;
                cellConstraints.anchor = (isMatch() && i==0)? GridBagConstraints.NORTHWEST: GridBagConstraints.SOUTHWEST;
                this.add(flag[i], cellConstraints);
                // initialize & add default team
                teamName[i] = "???";
                teamLabel[i] = new JButton(teamName[i]);
                teamLabel[i].setContentAreaFilled(false);
                teamLabel[i].setBorderPainted(false);
                teamLabel[i].setFocusPainted(false);
                teamLabel[i].setFont(cellFont);
                teamLabel[i].setForeground(cellText);
                teamLabel[i].setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                cellConstraints.gridx = 1;
                cellConstraints.weightx = 1;
                cellConstraints.anchor = GridBagConstraints.CENTER;
                this.add(teamLabel[i], cellConstraints);
                // add separator between team name and score
                JSeparator verticalSeparator = new JSeparator(JSeparator.VERTICAL);
                verticalSeparator.setPreferredSize(new Dimension(1, cellSize.height));
                verticalSeparator.setMinimumSize(new Dimension(1, cellSize.height));
                verticalSeparator.setBackground(stroke);
                cellConstraints.gridx = 2;
                cellConstraints.weightx = 0;
                if(isMatch()) this.add(verticalSeparator, cellConstraints);
                // initialize & add default score
                scoreValue[i] = 0;
                scoreLabel[i] = new JLabel(String.valueOf(scoreValue[i]));
                scoreLabel[i].setOpaque(false);
                scoreLabel[i].setFont(cellFont);
                scoreLabel[i].setForeground(cellText);
                scoreLabel[i].setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                cellConstraints.gridx = 3;
                cellConstraints.weightx = 0.8;
                if(isMatch()) this.add(scoreLabel[i], cellConstraints);
            }
            JSeparator horizontalSeparator = new JSeparator();
            horizontalSeparator.setPreferredSize(new Dimension(cellSize.width, 1));
            horizontalSeparator.setMinimumSize(new Dimension(cellSize.width, 1));
            horizontalSeparator.setBackground(stroke);
            cellConstraints.gridx = 0;
            cellConstraints.gridy = 1;
            cellConstraints.gridwidth = isMatch()? 4:2;
            cellConstraints.weightx = 1;
            cellConstraints.weighty = 0;
            if(!isMatch()) cellConstraints.anchor = GridBagConstraints.SOUTH;
            this.add(horizontalSeparator, cellConstraints);
            if(!isMatch()){
                champ.setText(((row < 8)? "1ST" : "3RD") + " PLACE");
                champ.setFont(cellFont);
                champ.setForeground(cellText);
                cellConstraints.anchor = GridBagConstraints.CENTER;
                cellConstraints.gridx = 0;
                cellConstraints.gridy = 0;
                cellConstraints.gridwidth = 2;
                cellConstraints.weightx = 1;
                cellConstraints.weighty = 1;
                this.add(champ, cellConstraints);
            }
            updateLabels();
            revalidate();
        }
        private int getRow() {
            return row;
        }
        private int getRound() {
            return round;
        }
        private boolean hasOrigin() {
            return origin;
        }
        private Point2D getCellOrigin(){
            return cellOrigin;
        }
        private void setCellOrigin(Point2D point){
            this.cellOrigin = point;
            this.origin = true;
        }
        private JButton[] getTeamLabel() {
            return teamLabel;
        }
        private void setCellRevealed(boolean isRevealed) {
            cellRevealed = isRevealed;
            updateLabels();

        }
        private boolean isCellRevealed(){
            return cellRevealed;
        }
        private boolean isMatch(){
            return (round != WINNER);
        }
        private void setMatch(Match newMatch) {
            this.match = newMatch;
            this.team = isMatch()? new Team[]{match.getTeam1(), match.getTeam2()} : new Team[]{match.getWinner()};
            this.setTeamName(isMatch()? new String[]{team[0].getAbbv(), team[1].getAbbv()} : new String[]{team[0].getAbbv()} );
            this.setScoreValue(isMatch()? new int[]{match.getTeam1Score(), match.getTeam2Score()} : new int[]{team[0].equals(match.getTeam1())? match.getTeam1Score() : match.getTeam2Score()});
            updateLabels();
            //if(round == ROUND_OF_SIXTEEN && row == 1){
                setCellRevealed(true);
            //}
        }
        private void setTeamName(String[] teamAbbv) {
            this.teamName = teamAbbv;
        }
        private void setScoreValue(int[] score){
            this.scoreValue = score;
        }
        private void updateLabels() {
            cellText = isCellRevealed()? text : new Color(0, 0, 0, 0);
            champ.setForeground(cellText);
            for(int i = 0; i < (isMatch()? 2:1); i++) {
                flagPath[i] = "Assets" + File.separator + "Images"  + File.separator + "smallFlags" + File.separator + ((teamName[i].equals("???") || !isCellRevealed())? ("BLANK.png") : (teamName[i] + ".png"));
                flag[i].setIcon(createScaledFlagIcon(i));
                teamLabel[i].setText(teamName[i]);
                scoreLabel[i].setText(String.valueOf(scoreValue[i]));
                teamLabel[i].setForeground(cellText);
                scoreLabel[i].setForeground(cellText);
            }
            this.revalidate();
        }

        private ImageIcon createScaledFlagIcon(int flagIndex) {
            int img = flagIndex;
            this.source[img] = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                source[img] = ImageIO.read(new File(flagPath[img]));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", flagPath[img]);
            }
            float scaleFactor = (float)(flagHeight) / (float)(source[img].getHeight());
            int iconX = (int) (source[img].getWidth() * scaleFactor);
            int iconY = (int) (source[img].getHeight() * scaleFactor);
            Image scaledPreviewImage = source[img].getScaledInstance(iconX, iconY, Image.SCALE_SMOOTH);
            BufferedImage scaledSource = new BufferedImage(iconX, iconY, BufferedImage.TYPE_INT_ARGB);
            scaledSource.getGraphics().drawImage(scaledPreviewImage, 0, 0, null);
            return new ImageIcon(scaledSource);
        }
    }
}
