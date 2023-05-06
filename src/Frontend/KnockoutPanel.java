package Frontend;

import Backend.Match;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.swing.SwingUtilities.convertPoint;

/**
 * Displays KnockoutStage results either one match at a time or
 * one round at a time, should the user choose to skip forward.
 */
public class KnockoutPanel extends JPanel implements StagePanel, ActionListener {
    // attributes
    private static final int ROUND_OF_SIXTEEN = 0;
    private static final int QUARTERFINALS = 1;
    private static final int SEMIFINALS = 2;
    private static final int FINAL = 3;
    private static final int WINNER = 4;

    private static final int CENTER = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    int currentRound;
    boolean initialized;
    Match[] matches;
    BracketCell[][] cells;
    /**
     * Default constructor; initializes JPanel with BorderLayout, sets size,
     * initializes matches, initializes init boolean, & calls createWindow()
     */
    public KnockoutPanel() {
        super(new GridBagLayout());
        this.cells = new BracketCell[][]{new BracketCell[16], new BracketCell[8], new BracketCell[4], new BracketCell[2], new BracketCell[1]};
        //this.setBackground(new Color(197, 197, 197));
        this.currentRound = 0;
        this.initialized = false;
        createWindow();
    }
    /*TODO:
     * Implement nextMatch(), & nextRound()
     * Make knockoutBracket() draw lines between the labels (it paintComponent time)
     * Make those 90 lines of spacer code shorter
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


        /*
         *
         *    << Bracket Column Legend with GridBagConstraints coordinates >>
         *
         *       0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19
         *      _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L
         *   0 |      ↓ 8x left                            8x right ↓      |
         *   1 |      ■■                                           ■■      |
         *   2 |      ■■    ↓ 4x left                4x right ↓    ■■      |
         *   3 |            ■■             winner            ■■            |
         *   4 |      ■■    ■■               ↓               ■■    ■■      |
         *   5 |      ■■     final left ↓  ■■■■■  ↓ final right    ■■      |
         *   6 |                  ■■    ■■       ■■    ■■                  |
         *   7 |                  ■■                   ■■                  |
         *   8 |      ■■          ↑          semi right ↑          ■■      |
         *   9 |      ■■    ■■    semi left                  ■■    ■■      |
         *  10 |            ■■                               ■■            |
         *  11 |      ■■                                           ■■      |
         *  12 |      ■■                                           ■■      |
         *  13 |_L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L _L|
         *
         *        by Naomi                                 4/28/23
         */

        /*int[][] row = new int[][] { new int[]{1, 2, 4, 5, 8, 9, 11, 12},
                                    new int[]{3, 4, 9, 10},
                                    new int[]{6, 7},
                                    new int[]{5},
                                    new int[]{6}};*/
        int[][] row = new int[][] { new int[]{1, 3, 5, 7, 9, 11, 13, 15},
                                    new int[]{2, 6, 10, 14},
                                    new int[]{4, 12},
                                    new int[]{8},
                                    new int[]{5}};

        for (int i = 0; i < 8; i++) {
            bracket.gridwidth = 1;
            bracket.gridheight = 1;
            if (i == 0) bracket.anchor = GridBagConstraints.SOUTH;
            if (i == 7) bracket.anchor = GridBagConstraints.NORTH;
            // first round
            bracket.gridy = row[ROUND_OF_SIXTEEN][i];
            // left
            cells[ROUND_OF_SIXTEEN][i] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, LEFT);
            bracket.gridx = 1;
            this.add(this.cells[ROUND_OF_SIXTEEN][i], bracket);
            // right
            cells[ROUND_OF_SIXTEEN][i+8] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, RIGHT);
            bracket.gridx = 16;
            this.add(this.cells[ROUND_OF_SIXTEEN][i+8], bracket);
            bracket.anchor = GridBagConstraints.CENTER;
            // second round
            if (i < 4) {
                bracket.gridy = row[QUARTERFINALS][i];
                // left
                cells[QUARTERFINALS][i] = new BracketCell(row[QUARTERFINALS][i], QUARTERFINALS, LEFT);
                bracket.gridx = 3;
                this.add(this.cells[QUARTERFINALS][i], bracket);
                // right
                cells[QUARTERFINALS][i+4] = new BracketCell(row[QUARTERFINALS][i], QUARTERFINALS, RIGHT);
                bracket.gridx = 14;
                this.add(this.cells[QUARTERFINALS][i+4], bracket);
            }
            // semi-final round
            if (i < 2) {
                bracket.gridy = row[SEMIFINALS][i];
                // left
                cells[SEMIFINALS][i] = new BracketCell(row[SEMIFINALS][i], SEMIFINALS, LEFT);
                bracket.gridx = 5;
                this.add(this.cells[SEMIFINALS][i], bracket);
                // right
                cells[SEMIFINALS][i+2] = new BracketCell(row[SEMIFINALS][i], SEMIFINALS, RIGHT);
                bracket.gridx = 12;
                this.add(this.cells[SEMIFINALS][i+2], bracket);
            }
            // final + winner
            if (i == 0) {
                // left
                cells[FINAL][i] = new BracketCell(row[FINAL][i], FINAL, LEFT);
                bracket.gridx = 7;
                bracket.gridy = row[FINAL][i];
                this.add(this.cells[FINAL][i], bracket);
                // right
                cells[FINAL][i+1] = new BracketCell(row[FINAL][i], FINAL, RIGHT);
                bracket.gridx = 10;
                bracket.gridy = row[FINAL][i];
                this.add(this.cells[FINAL][i+1], bracket);

                // winner
                cells[WINNER][i] = new BracketCell(row[WINNER][i], WINNER, CENTER);
                bracket.gridwidth = 2;
                bracket.gridheight = 2;
                bracket.gridx = 8;
                bracket.gridy = row[WINNER][i];
                this.add(this.cells[WINNER][i], bracket);
            }
        }

        for(BracketCell[] column : cells){
            for(BracketCell cell : column){
                cell.getTeamLabel().addActionListener(this);
                cell.getTeamLabel().setActionCommand("Row: " + cell.getRow() + "\nRound: " + cell.getRound());
            }
        }
        // remainder of method is spacers between cells

        // 18x1 vertical spacers, as top & bottom margins of the whole bracket (2x total)
        addSpacer(0,0,18,1,true, false);
        addSpacer(0,16,18,1,true, false);
        // 1x15 horizontal spacers, four each padding the two groups of four outermost columns of cells (8x total)
        addSpacer(0,1,1,15,false,true);
        addSpacer(2,1,1,15,false,true);
        addSpacer(4,1,1,15,false,true);
        addSpacer(6,1,1,15,false,true);
        addSpacer(11,1,1,15,false,true);
        addSpacer(13,1,1,15,false,true);
        addSpacer(15,1,1,15,false,true);
        addSpacer(17,1,1,15,false,true);
        // 1x1 vertical spacers, two each at the top & bottom of the quarterfinal columns (4x total)
        addSpacer(3,1,1,1,true,false);
        addSpacer(14,1,1,1,true,false);
        addSpacer(3,15,1,1,true,false);
        addSpacer(14,15,1,1,true,false);
        // 1x1 vertical spacers, between all round-of-sixteen rows (14x total)
        addSpacer(1,2,1,1,true,false);
        addSpacer(16,2,1,1,true,false);
        addSpacer(1,4,1,1,true,false);
        addSpacer(16,4,1,1,true,false);
        addSpacer(1,6,1,1,true,false);
        addSpacer(16,6,1,1,true,false);
        addSpacer(1,8,1,1,true,false);
        addSpacer(16,8,1,1,true,false);
        addSpacer(1,10,1,1,true,false);
        addSpacer(16,10,1,1,true,false);
        addSpacer(1,12,1,1,true,false);
        addSpacer(16,12,1,1,true,false);
        addSpacer(1,14,1,1,true,false);
        addSpacer(16,14,1,1,true,false);
        // 1x3 vertical spacers, three each between the four rows of the quarterfinal columns (8x total)
        addSpacer(3,3,1,3,true,false);
        addSpacer(14,7,1,3,true,false);
        addSpacer(3,8,1,3,true,false);
        addSpacer(14,8,1,3,true,false);
        addSpacer(3,11,1,3,true,false);
        addSpacer(14,11,1,3,true,false);
        // 1x6 vertical spacers, one each between the two rows of the semifinal columns (2x total)
        addSpacer(5,6,1,6,true,false);
        addSpacer(12,6,1,6,true,false);
        // 1x3 vertical spacers, two each at the top & bottom of the semifinal columns (4x total)
        addSpacer(5,1,1,3,true,false);
        addSpacer(12,1,1,3,true,false);
        addSpacer(5,14,1,3,true,false);
        addSpacer(12,14,1,3,true,false);
        // 1x7 vertical spacers, two each above and below the single row of the final columns (4x total)
        addSpacer(7,1,1,7,true,false);
        addSpacer(10,1,1,7,true,false);
        addSpacer(7,10,1,7,true,false);
        addSpacer(10,10,1,7,true,false);
        //2x4 two-way spacer, above the winner's cell (1x only)
        addSpacer(8,1,2,4,true,true);
        // 2x9 two-way spacer, below the winner's cell and between the final columns (1x only)
        addSpacer(8,7,2,9,true,true);
        revalidate();
        repaint();
    }
    /**
     * Creates an empty, transparent JPanel for use as a spacer.
     * Intended to simplify repetitive use of GridBagConstraints by accepting only 6 parameters instead of 11
     *
     */
    private void addSpacer(int gridx, int gridy, int width, int height, boolean isVertical, boolean isHorizontal){
        JPanel spacerPanel = new JPanel();
        spacerPanel.setOpaque(false);

        // uncomment the next line if u wanna see something ~nasty~

        // spacerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1, false));

        double weightx = 0;
        double weighty = 0;
        if(isVertical) weighty = 1;
        if(isHorizontal) weightx = 1;
        Insets insZero = new Insets(0,0,0,0);
        this.add(spacerPanel, new GridBagConstraints(gridx,gridy,width,height,weightx,weighty,10, 1, insZero,0, 0));
    }
    public void nextMatch() {
    }
    public void nextRound() {
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
    public void initPanel() {
        initialized = true; // last line of method
    }

    @Override
    public void initPanel(Match[] m) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        /*g.setColor(Color.GREEN);
        for(int i = 0; i < cells.length; i++){
            System.out.printf("Round %d: %d rows\nOriginal:\tConverted:\n", (i+1), (cells[i].length));
            for(int j = 0; j < cells[i].length; j++){
                System.out.printf("\t(%d, %d)\t", cells[i][j].getX(), cells[i][j].getY());
                Point testPoint = convertPoint(cells[i][j], cells[i][j].getX(), cells[i][j].getY(),this);
                System.out.printf("\t(%f, %f)\n", testPoint.getX(), testPoint.getY());
                g.drawOval((int)testPoint.getX(), (int)testPoint.getY(), 3,3);
            }

        }*/

        //Graphics2D tracer = (Graphics2D) g;
        //tracer.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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
        private GridBagConstraints cellConstraints;
        JPanel mainCell;
        Component innerVertex, outerVertex;
        private JLabel flagLabel;
        private JButton teamLabel;
        private Dimension cellSize;
        private int row, round, position;
        private int imageWidth;
        private int imageHeight;
        private int flagWidth;
        private int flagHeight;
        private BufferedImage flag;
        private BracketCell() {
            this(0, 4, 0);
        }
        private BracketCell(int row, int round, int position) {
            this(row, round, position, 70, 46, 46, 30);
        }
        private BracketCell(int row, int round, int position, int imageWidth, int imageHeight, int flagWidth, int flagHeight) {
            super(new GridBagLayout());
            this.row = row;
            this.round = round;
            this.position = position;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.flagWidth = flagWidth;
            this.flagHeight = flagHeight;

            cellSize = new Dimension(40+this.flagWidth, this.flagHeight);
            this.setMinimumSize(cellSize);
            this.setMaximumSize(cellSize);
            this.setOpaque(false);

            mainCell = new JPanel(new GridBagLayout());
            mainCell.setBackground(Color.DARK_GRAY);


            this.flag = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(defaultPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", defaultPath);
            }

            cellConstraints = new GridBagConstraints();

            // initialize & add teamLabel to mainCell with GridBagConstraints conditional on orientation

            teamLabel = new JButton("TEAM");
            teamLabel.setContentAreaFilled(false);
            teamLabel.setBorderPainted(false);
            teamLabel.setFocusPainted(false);
            teamLabel.setForeground(Color.WHITE);
            teamLabel.setFont(new Font ("Arial", Font.BOLD, 14));

            if (this.position == LEFT) {
                cellConstraints.gridx = 0;
            } else if (this.position == RIGHT) {
                cellConstraints.gridx = 1;
            } else if (this.position == CENTER) {
                cellConstraints.gridx = 0;
            }
            cellConstraints.anchor = GridBagConstraints.CENTER;
            cellConstraints.weightx = 1;
            cellConstraints.gridwidth = 2;
            cellConstraints.insets = new Insets(0,0,0,0);

            mainCell.add(teamLabel, cellConstraints);

            // initialize & add flagLabel to mainCell with GridBagConstraints conditional on orientation

            flagLabel = new JLabel(scaledFlag(this.flagHeight));
            flagLabel.setOpaque(false);

            if (position == LEFT) {
                cellConstraints.gridx = 2;
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


            mainCell.add(flagLabel, cellConstraints);

            if (position == LEFT || position == RIGHT) {
                cellConstraints.gridx = 1;
                cellConstraints.anchor = GridBagConstraints.CENTER;
                cellConstraints.weightx = 1;
            } else if (position == CENTER) {
                cellConstraints.gridx = 0;
                cellConstraints.gridy = 0;
                cellConstraints.weighty = 1;
            }

            mainCell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
            this.add(mainCell, cellConstraints);
            // initialize and add inner- & outerVertex, conditional GridBagConstraints based on position

            innerVertex = Box.createHorizontalStrut(0);
            outerVertex = Box.createHorizontalStrut(0);

            cellConstraints.weightx = 0.01;
            cellConstraints.weighty = 0.01;


            if (position == LEFT) {
                cellConstraints.gridx = 0;
                cellConstraints.anchor = GridBagConstraints.WEST;
                this.add(outerVertex, cellConstraints);
                cellConstraints.gridx = 2;
                cellConstraints.anchor = GridBagConstraints.EAST;
                this.add(innerVertex, cellConstraints);
            } else if (position == RIGHT) {
                cellConstraints.gridx = 0;
                cellConstraints.anchor = GridBagConstraints.WEST;
                this.add(innerVertex, cellConstraints);
                cellConstraints.gridx = 2;
                cellConstraints.anchor = GridBagConstraints.EAST;
                this.add(outerVertex, cellConstraints);
            } else if (position == CENTER) {
                outerVertex = Box.createVerticalStrut(0);
                cellConstraints.gridx = 0;
                cellConstraints.gridy = 1;
                cellConstraints.anchor = GridBagConstraints.CENTER;
                this.add(outerVertex, cellConstraints);
            }

            this.validate();
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
        private Component getInnerVertex(){
            return innerVertex;
        }
        private Component getOuterVertex(){
            return outerVertex;
        }
        /*private ImageIcon getFlagIcon() {
            return (ImageIcon)(flagLabel.getIcon());
        }*/
        private void setFlagIcon(String teamAbbv) {
            flagPath = "Assets" + File.separator + "Images"  + File.separator + "smallFlags" + File.separator + teamAbbv + ".png";
            flag = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(flagPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", defaultPath);
            }
            flagLabel.setIcon(scaledFlag(flagHeight));
            this.revalidate();
        }
        private JButton getTeamLabel() {
            return teamLabel;
        }
        private void setTeamLabel(String teamAbbv) {
            this.teamLabel.setText(teamAbbv);
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
