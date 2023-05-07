package Frontend;

import Backend.Match;


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
    // positional constants
    private static final int CENTER = 0;//soon to be deprecated
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    // colors
    private final static Color canvas = new Color(17, 132, 39);
    private final static Color text = new Color(213, 226, 216);
    private final static Color stroke = new Color(163, 207, 172);

    private boolean initialized;
    private BracketCell[][] cells;
    private Color themeColor;

    /**
     * Default constructor; initializes JPanel with BorderLayout, sets size,
     * initializes matches, initializes init boolean, & calls createWindow()
     */
    public KnockoutPanel() {
        super(new GridBagLayout(), true);
        this.cells = new BracketCell[][]{new BracketCell[16], new BracketCell[8], new BracketCell[4], new BracketCell[4], new BracketCell[2]};
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
        /*Insets insCenter = new Insets(20,20,40,20);
        Insets insLeft = new Insets(10, 30, 10, 10);
        Insets insRight = new Insets(10, 10, 10, 30);*/

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
         *       0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16
         *      _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L
         *   0 |                                                  |
         *   1 |   ■■                                        ■■   |
         *   2 |         ■■                            ■■         |
         *   3 |   ■■                   ■■                   ■■   |
         *   4 |               ■■                ■■               |
         *   5 |   ■■                                        ■■   |
         *   6 |         ■■          ■■    ■■          ■■         |
         *   7 |   ■■                                        ■■   |
         *   8 |                                                  |
         *   9 |   ■■                                        ■■   |
         *  10 |         ■■          ■■    ■■          ■■         |
         *  11 |   ■■                                        ■■   |
         *  12 |               ■■                ■■               |
         *  13 |   ■■                   ■■                   ■■   |
         *  14 |         ■■                            ■■         |
         *  15 |   ■■                                        ■■   |
         *  16 |_L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L|
         *
         *        by Naomi                                 4/28/23
         */

        int[][] row = new int[][]{new int[]{1, 3, 5, 7, 10, 12, 14, 16},
                                  new int[]{2, 6, 11, 15},
                                  new int[]{4, 13},
                                  new int[]{6, 11},
                                  new int[]{3, 14}};

        for (int i = 0; i < 8; i++) {

            //if (i == 0) bracket.anchor = GridBagConstraints.SOUTH;
            //if (i == 7) bracket.anchor = GridBagConstraints.NORTH;
            //if (i%2==0) {
            //    bracket.anchor = GridBagConstraints.SOUTH;
           // } else bracket.anchor = GridBagConstraints.NORTH;
            // first round
            bracket.gridy = row[ROUND_OF_SIXTEEN][i];
            // left
            cells[ROUND_OF_SIXTEEN][i] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, LEFT);
            bracket.gridx = 1;
            this.add(this.cells[ROUND_OF_SIXTEEN][i], bracket);
            // right
            cells[ROUND_OF_SIXTEEN][i+8] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, RIGHT);
            bracket.gridx = 15;
            this.add(this.cells[ROUND_OF_SIXTEEN][i+8], bracket);

            // second round
            if (i < 4) {
                bracket.gridy = row[QUARTERFINAL][i];
                // left
                cells[QUARTERFINAL][i] = new BracketCell(row[QUARTERFINAL][i], QUARTERFINAL, LEFT);
                bracket.gridx = 3;
                this.add(this.cells[QUARTERFINAL][i], bracket);
                // right
                cells[QUARTERFINAL][i+4] = new BracketCell(row[QUARTERFINAL][i], QUARTERFINAL, RIGHT);
                bracket.gridx = 13;
                this.add(this.cells[QUARTERFINAL][i+4], bracket);
            }
            // semi-final round
            if (i < 2) {
                bracket.gridy = row[SEMIFINAL][i];
                // left
                cells[SEMIFINAL][i] = new BracketCell(row[SEMIFINAL][i], SEMIFINAL, LEFT);
                bracket.gridx = 5;
                this.add(this.cells[SEMIFINAL][i], bracket);
                // right
                cells[SEMIFINAL][i+2] = new BracketCell(row[SEMIFINAL][i], SEMIFINAL, RIGHT);
                bracket.gridx = 11;
                this.add(this.cells[SEMIFINAL][i+2], bracket);
            }
            // finals + winner
            if (i == 0) {
                // first place match
                // left
                cells[FINAL][i] = new BracketCell(row[FINAL][i], FINAL, LEFT);
                bracket.gridx = 7;
                bracket.gridy = row[FINAL][i];
                this.add(this.cells[FINAL][i], bracket);
                // right
                cells[FINAL][i+2] = new BracketCell(row[FINAL][i], FINAL, RIGHT);
                bracket.gridx = 9;
                bracket.gridy = row[FINAL][i];
                this.add(this.cells[FINAL][i+2], bracket);
                // winner
                cells[WINNER][i] = new BracketCell(row[WINNER][i], WINNER, LEFT);
                bracket.gridx = 8;
                bracket.gridy = row[WINNER][i];
                this.add(this.cells[WINNER][i], bracket);
                // third place match
                // left
                cells[FINAL][i+1] = new BracketCell(row[FINAL][i+1], FINAL, LEFT);
                bracket.gridx = 7;
                bracket.gridy = row[FINAL][i+1];
                this.add(this.cells[FINAL][i+1], bracket);
                // right
                cells[FINAL][i+3] = new BracketCell(row[FINAL][i+1], FINAL, RIGHT);
                bracket.gridx = 9;
                bracket.gridy = row[FINAL][i+1];
                this.add(this.cells[FINAL][i+3], bracket);
                // winner
                cells[WINNER][i+1] = new BracketCell(row[WINNER][i+1], WINNER, LEFT);
                bracket.gridx = 8;
                bracket.gridy = row[WINNER][i+1];
                this.add(this.cells[WINNER][i+1], bracket);
            }
        }
        for(BracketCell[] column : cells){
            for(BracketCell cell : column){
                cell.getTeamName().addActionListener(this);
                int col = cell.getRound() + 1;
                if(cell.getPosition() == RIGHT){
                    col = 10 - col;
                }
                cell.getTeamName().setActionCommand(col+","+cell.getRow());
            }
        }
        // remainder of method is spacers between cells

        // 1x18 horizontal: four each padding the two groups of four outermost columns of cells (8x total)
        addSpacer(0,0,1,18,1,0);
        addSpacer(2,0,1,18,1,0);
        addSpacer(4,0,1,18,1,0);
        addSpacer(6,0,1,18,1,0);
        addSpacer(10,0,1,18,1,0);
        addSpacer(12,0,1,18,1,0);
        addSpacer(14,0,1,18,1,0);
        addSpacer(16,0,1,18,1,0);
        // 1x2 vertical: two each padding the top and bottom of QUARTERFINAL columns,
        // and one each in the center of the two ROUND_OF_SIXTEEN columns (4x total)
        addSpacer(3,0,1,2,0,1);
        addSpacer(13,0,1,2,0,1);
        addSpacer(3,16,1,2,0,1);
        addSpacer(13,16,1,2,0,1);
        addSpacer(1,8,1,2,0,1);
        addSpacer(15,8,1,2,0,1);
        // 1x1 vertical: padding the rows of both ROUND_OF_SIXTEEN columns except the two center rows (16x total)
        addSpacer(1,0,1,1,0,1);
        addSpacer(15,0,1,1,0,1);
        addSpacer(1,2,1,1,0,0.1);
        addSpacer(15,2,1,1,0,0.1);
        addSpacer(1,4,1,1,0,0.1);
        addSpacer(15,4,1,1,0,0.1);
        addSpacer(1,6,1,1,0,0);
        addSpacer(15,6,1,1,0,0);
        //addSpacer(1,11,1,1,0,0.1); // Removing specifically these two fixed an issue I was having.
        //addSpacer(15,11,1,1,0,0.1); // I have learned nothing from this. I want to scream.
        addSpacer(1,13,1,1,0,0.1);
        addSpacer(15,13,1,1,0,0.1);
        addSpacer(1,15,1,1,0,0.1);
        addSpacer(15,15,1,1,0,0.1);
        addSpacer(1,17,1,1,0,1);
        addSpacer(15,17,1,1,0,1);
        // 1x3 vertical: three each between the four rows of QUARTERFINAL columns (6x total)
        addSpacer(3,3,1,3,0,1);
        addSpacer(13,3,1,3,0,1);

        addSpacer(3,11,1,3,0,1);
        addSpacer(13,11,1,3,0,1);

        // 1x4 vertical: one each between finals and quarterfinals rows (2x total)
        addSpacer(3,7,1,4,0,1);
        addSpacer(7,7,1,4,0,1);
        addSpacer(9,7,1,4,0,1);
        addSpacer(13,7,1,4,0,1);



        // 1x4 vertical: two each padding the top and bottom of SEMIFINAL columns (4x total)
        addSpacer(5,0,1,4,0,1);
        addSpacer(11,0,1,4,0,1);
        addSpacer(5,14,1,4,0,1);
        addSpacer(11,14,1,4,0,1);

        // 1x6 vertical: two each padding the top and bottom of the finals' columns,
        addSpacer(7,0,1,6,0,1);
        addSpacer(9,0,1,6,0,1);
        addSpacer(7,12,1,6,0,1);
        addSpacer(9,12,1,6,0,1);
        // 1x8 vertical: one each at the center of the finals' columns (2x total)
        addSpacer(5,5,1,8,0,1);
        addSpacer(11,5,1,8,0,1);
        //1x2 vertical: one each respectively above and below the winners' rows (2x total)
        addSpacer(8,0,1,3,0,1);
        addSpacer(8,15,1,3,0,1);
        // 1x10 vertical: between the winners' rows and between the finals' columns (1x only)
        addSpacer(8,4,1,10,0,1);
        revalidate();
        repaint();
    }
    /**
     * Creates an empty, transparent JPanel for use as a spacer.
     * Intended to simplify repetitive use of GridBagConstraints by accepting only 6 parameters instead of 11
     *
     */
    private void addSpacer(int gX, int gY, int gW, int gH, double wX, double wY){
        //JPanel spacer = new JPanel(true);
        JComponent spacer = (JComponent) Box.createVerticalStrut(1);

        if(wX!=0) {
            spacer = (JComponent) Box.createHorizontalStrut(1);
        }

        /*//Makes things really ugly, but good for visual troubleshooting
        spacer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1,0,0,1),BorderFactory.createLineBorder(Color.RED, 1, false)));
        spacer.setToolTipText("<html>" + "(" + gX + "," + gY + ")" + "<br>" + ""+ gW + "x" + gH + "</html>");*/

        this.add(spacer, new GridBagConstraints(gX,gY,gW,gH,wX,wY,10,1,(new Insets(0,0,0,0)),0,0));
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
    private ArrayList<Line2D.Double> createStripes(){
        ArrayList<Line2D.Double> stripes = new ArrayList<>();
        double[] X = new double[9];
        int n = -1;
        for (BracketCell[] column : cells) {
            n++;
            for (BracketCell cell : column) {
                Rectangle2D box = cell.getBounds().getBounds2D();
                Point2D origin = new Point2D.Double(box.getCenterX(), box.getCenterY());
                cell.setOrigin(origin);
            }
            X[n] = column[0].getOrigin().getX();
            if(column[0].getPosition()!=CENTER) {
                X[X.length-1-n] = column[column.length-1].getOrigin().getX();
            }
        }
        /*for(int i = 0; i < X.length; i++) System.out.print("x[" + i + "] ");
        System.out.print("\n");
        for(int i = 0; i < X.length; i++) System.out.print(" " + (int)(X[i]) + " ");
        System.out.println("\n");*/

        boolean even;
        double xL, xR, y1, y2, axs;

        for (int i = 0; i < 8; i++) {
            even = i%2==0;
            // first round
            xL = (X[0] + X[1]) / 2;
            xR = (X[7] + X[8]) / 2;
            y1 = cells[ROUND_OF_SIXTEEN][i].getOrigin().getY();
            if (even) {
                // vertical
                y2 = cells[ROUND_OF_SIXTEEN][i + 1].getOrigin().getY();
                stripes.add(new Line2D.Double(xL, y1, xL, y2));
                stripes.add(new Line2D.Double(xR, y1, xR, y2));
            }
            stripes.add(new Line2D.Double(X[0], y1, xL, y1));
            stripes.add(new Line2D.Double(X[8], y1, xR, y1));
            // second round
            if (i<4) {
                //horizontal
                xL = (X[0] + X[1]) / 2;
                xR = (X[7] + X[8]) / 2;
                y1 = cells[QUARTERFINAL][i].getOrigin().getY();
                stripes.add(new Line2D.Double(X[1], y1, xL, y1));
                stripes.add(new Line2D.Double(X[7], y1, xR, y1));
                xL = (X[1] + X[2]) / 2;
                xR = (X[6] + X[7]) / 2;
                y1 = cells[QUARTERFINAL][i].getOrigin().getY();
                y2 = cells[QUARTERFINAL][i + 1].getOrigin().getY();
                // vertical
                if(even) {
                    stripes.add(new Line2D.Double(xL, y1, xL, y2));
                    stripes.add(new Line2D.Double(xR, y1, xR, y2));
                }
                y2 = cells[SEMIFINAL][i].getOrigin().getY();
                stripes.add(new Line2D.Double(X[1], y1, xL, y1));
                stripes.add(new Line2D.Double(X[2], y2, xL, y2));
                stripes.add(new Line2D.Double(X[7], y1, xR, y1));
                stripes.add(new Line2D.Double(X[6], y2, xR, y2));
            }
            // third round
            if (i<2) {
                if(even) {
                    // vertical
                    xL = (X[2] + X[3]) / 2;
                    xR = (X[5] + X[6]) / 2;
                    y1 = cells[SEMIFINAL][i].getOrigin().getY();
                    y2 = cells[SEMIFINAL][i + 1].getOrigin().getY();
                    stripes.add(new Line2D.Double(xL, y1, xL, y2));
                    stripes.add(new Line2D.Double(xR, y1, xR, y2));
                    // finals - horizontal
                    y1 = cells[FINAL][i].getOrigin().getY();
                    stripes.add(new Line2D.Double(xL, y1, xR, y1));
                    y2 = cells[FINAL][i + 1].getOrigin().getY();
                    stripes.add(new Line2D.Double(xL, y2, xR, y2));
                }
                //winners
                if(even) {
                    // vertical
                    axs = X[4];
                    y1 = cells[FINAL][i].getOrigin().getY();
                    y2 = cells[WINNER][i].getOrigin().getY();
                    stripes.add(new Line2D.Double(axs, y1, axs, y2));
                    y1 = cells[FINAL][i + 1].getOrigin().getY();
                    y2 = cells[WINNER][i + 1].getOrigin().getY();
                    stripes.add(new Line2D.Double(axs, y1, axs, y2));
                }
                // horizontal
                xL = X[2];
                xR = (X[2] + X[3]) / 2;
                y1 = cells[SEMIFINAL][i].getOrigin().getY();
                stripes.add(new Line2D.Double(xR, y1, xL, y1));
                y2 = cells[SEMIFINAL][i + 1].getOrigin().getY();
                xL = (X[5] + X[6]) / 2;
                xR = X[6];
                stripes.add(new Line2D.Double(xR, y2, xL, y2));
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
    public void initPanel(List<Match> matches){
        initialized = true;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
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
    private class BracketCell extends JPanel {
        private final String defaultPath = "Assets" + File.separator + "blank.png";
        private String flagPath;
        private Dimension cellSize;
        private GridBagConstraints cellConstraints;
        private Point2D origin;
        private JLabel flagLabel;
        private JButton teamName;
        private boolean hasOrigin;
        private int row, round, position;
        private int imageWidth;
        private int imageHeight;
        private int flagWidth;
        private int flagHeight;
        private BufferedImage flag;
        /**
         * Default constructor, calls secondary constructor with all-zero args.
         * */
        private BracketCell() {
            this(0, 0, 0);
        }
        /**
         * Secondary BracketCell constructor taking only three parameters, calls main constructor with preset height/width for images and flags. Is the main constructor in use by KnockoutPanel
         * */
        private BracketCell(int row, int round, int position) {
            this(row, round, position, 70, 46, 46, 30);
        }
        private BracketCell(int row, int round, int position, int imageWidth, int imageHeight, int flagWidth, int flagHeight) {
            super(new GridBagLayout(), true);
            this.row = row;
            this.round = round;
            this.position = position;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.flagWidth = flagWidth;
            this.flagHeight = flagHeight;
            this.hasOrigin = false;


            cellSize = new Dimension(40+this.flagWidth, this.flagHeight);
            //this.setMinimumSize(cellSize);
            this.setMaximumSize(cellSize);
            //this.setBackground(new Color(35, 117, 51));
            this.setBackground(canvas.darker());

            this.flag = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(defaultPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", defaultPath);
            }

            cellConstraints = new GridBagConstraints();

            // initialize & add teamLabel to mainCell with GridBagConstraints conditional on orientation

            teamName = new JButton("TEAM");
            teamName.setContentAreaFilled(false);
            teamName.setBorderPainted(false);
            teamName.setFocusPainted(false);
            teamName.setForeground(text);
            teamName.setFont(new Font ("Arial Black", Font.PLAIN, 14));
            teamName.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

            if (this.position == LEFT) {
                cellConstraints.gridx = 0;
            } else if (this.position == RIGHT) {
                cellConstraints.gridx = 1;
            } else if (this.position == CENTER) {
                cellConstraints.gridx = 0;
            }
            cellConstraints.anchor = GridBagConstraints.CENTER;
            cellConstraints.weightx = 1;
            //cellConstraints.gridwidth = 2;
            cellConstraints.insets = new Insets(0,0,0,0);

            this.add(teamName, cellConstraints);

            // initialize & add flagLabel to mainCell with GridBagConstraints conditional on orientation

            flagLabel = new JLabel(scaledFlag(this.flagHeight));
            flagLabel.setOpaque(false);

            if (position == LEFT) {
                cellConstraints.gridx = 1;
                cellConstraints.anchor = GridBagConstraints.EAST;
            } else if (position == RIGHT) {
                cellConstraints.gridx = 0;
                cellConstraints.anchor = GridBagConstraints.WEST;
            } else if (position == CENTER) {
                cellConstraints.gridx = 0;
                cellConstraints.gridy = 1;
            }
            cellConstraints.insets = new Insets(0,0,0,0);
            cellConstraints.weightx = 0.2;
            cellConstraints.gridwidth = 1;

            this.add(flagLabel, cellConstraints);

            this.setBorder(BorderFactory.createLineBorder(stroke, 2, true));
        }
        private int getRow() {
            return row;
        }
        private int getPosition() {
            return position;
        }
        private int getRound() {
            return round;
        }
        private boolean hasOrigin() {
            return hasOrigin;
        }
        private Point2D getOrigin(){
            return origin;
        }
        private void setOrigin(Point2D point){
            this.origin = point;
            this.hasOrigin = true;
        }
        private void setFlagIcon(String teamAbbv) {
            flagPath = "Assets" + File.separator + "Images"  + File.separator + "smallFlags" + File.separator + teamAbbv + ".png";
            flag = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(flagPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", flagPath);
            }
            flagLabel.setIcon(scaledFlag(flagHeight));
            this.revalidate();
        }
        private JButton getTeamName() {
            return teamName;
        }
        private void setTeamName(String teamAbbv) {
            this.teamName.setText(teamAbbv);
        }
        private ImageIcon scaledFlag(int targetHeight) {
            float scaleFactor = (float)(targetHeight) / (float)(flag.getHeight());
            int iconX = (int) (flag.getWidth() * scaleFactor);
            int iconY = (int) (flag.getHeight() * scaleFactor);
            Image scaledPreviewImage = flag.getScaledInstance(iconX, iconY, Image.SCALE_SMOOTH);
            BufferedImage image = new BufferedImage(iconX, iconY, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(scaledPreviewImage, 0, 0, null);
            return new ImageIcon(image);
        }
    }
}
