package Frontend;

import Backend.Match;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Displays KnockoutStage results either one match at a time or
 * one round at a time, should the user choose to skip forward.
 */
public class KnockoutPanel extends JPanel implements StagePanel {
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
        //this.setBackground(new Color(60, 63, 65));
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
        Insets insZero = new Insets(0,0,0,0);
        Insets insCenter = new Insets(20,20,40,20);
        Insets insLeft = new Insets(10, 30, 10, 10);
        Insets insRight = new Insets(10, 10, 10, 30);

        GridBagConstraints bracket = new GridBagConstraints(0,0,1,1,0.2,0.2,10, 0, insCenter,0, 0);

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

        int[][] row = new int[][] { new int[]{1, 2, 4, 5, 8, 9, 11, 12},
                                    new int[]{3, 4, 9, 10},
                                    new int[]{6, 7},
                                    new int[]{5},
                                    new int[]{6}};

        for (int i = 0; i < 8; i++) {
            // first round
            bracket.gridy = row[ROUND_OF_SIXTEEN][i];
            // left
            this.cells[ROUND_OF_SIXTEEN][i] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, LEFT);
            bracket.insets = insLeft;
            bracket.gridx = 2;
            this.add(this.cells[ROUND_OF_SIXTEEN][i], bracket);
            // right
            this.cells[ROUND_OF_SIXTEEN][i+8] = new BracketCell(row[ROUND_OF_SIXTEEN][i], ROUND_OF_SIXTEEN, RIGHT);
            bracket.insets = insRight;
            bracket.gridx = 17;
            this.add(this.cells[ROUND_OF_SIXTEEN][i+8], bracket);
            // second round
            if (i < 4) {
                bracket.gridy = row[QUARTERFINALS][i];
                // left
                this.cells[QUARTERFINALS][i] = new BracketCell(row[QUARTERFINALS][i], QUARTERFINALS, LEFT);
                bracket.insets = insLeft;
                bracket.gridx = 4;
                this.add(this.cells[QUARTERFINALS][i], bracket);
                // right
                this.cells[QUARTERFINALS][i+4] = new BracketCell(row[QUARTERFINALS][i], QUARTERFINALS, RIGHT);
                bracket.insets = insRight;
                bracket.gridx = 15;
                this.add(this.cells[QUARTERFINALS][i+4], bracket);
            }
            // semi-final round
            if (i < 2) {
                bracket.gridy = row[SEMIFINALS][i];
                // left
                this.cells[SEMIFINALS][i] = new BracketCell(row[SEMIFINALS][i], SEMIFINALS, LEFT);
                bracket.insets = insLeft;
                bracket.gridx = 6;
                this.add(this.cells[SEMIFINALS][i], bracket);
                // right
                this.cells[SEMIFINALS][i+2] = new BracketCell(row[SEMIFINALS][i], SEMIFINALS, RIGHT);
                bracket.insets = insRight;
                bracket.gridx = 13;
                this.add(this.cells[SEMIFINALS][i+2], bracket);
            }
            // final + winner
            if (i == 0) {
                // left

                this.cells[FINAL][i] = new BracketCell(row[FINAL][i], FINAL, LEFT);
                bracket.insets = insLeft;
                bracket.gridx = 8;
                bracket.gridy = 6;
                this.add(this.cells[FINAL][i], bracket);
                // right

                this.cells[FINAL][i+1] = new BracketCell(row[FINAL][i], FINAL, RIGHT);
                bracket.insets = insRight;
                bracket.gridx = 11;
                bracket.gridy = 6;
                this.add(this.cells[FINAL][i+1], bracket);

                // winner
                this.cells[WINNER][i] = new BracketCell(row[WINNER][i], WINNER, CENTER);
                bracket.insets = insCenter;
                bracket.gridwidth = 2;
                bracket.gridx = 9;
                bracket.gridy = 5;
                this.add(this.cells[WINNER][i], bracket);
                bracket.gridwidth = 1;
            }
        }
        //remainder of method is spacers between cells
        GridBagConstraints spacer = new GridBagConstraints(0,0,1,1,1,1,10, 1, insZero,0, 0);
        //20x1 vertical spacers (top & bottom margins)
        spacer.gridwidth = 20;
        spacer.gridheight = 1;
        spacer.weightx = 0.2;
        spacer.weighty = 1;
        this.add(createSpacer(), spacer);
        spacer.gridy = 13;
        this.add(createSpacer(), spacer);
        //2x12 horizontal spacers (left & right margins)
        spacer.gridwidth = 2;
        spacer.gridheight = 12;
        spacer.weightx = 1;
        spacer.weighty = 0.2;
        spacer.gridy = 1;
        spacer.gridx = 0;
        this.add(createSpacer(), spacer);
        spacer.gridx = 18;
        this.add(createSpacer(), spacer);
        // 1x12 horizontal spacers, between each column of cells (6x)
        spacer.gridwidth = 1;
        spacer.gridheight = 12;
        spacer.gridx = 3;
        this.add(createSpacer(), spacer);
        spacer.gridx = 5;
        this.add(createSpacer(), spacer);
        spacer.gridx = 7;
        this.add(createSpacer(), spacer);
        spacer.gridx = 12;
        this.add(createSpacer(), spacer);
        spacer.gridx = 14;
        this.add(createSpacer(), spacer);
        spacer.gridx = 16;
        this.add(createSpacer(), spacer);
        // 1x1 vertical spacers, (4x total)
        spacer.weightx = 0.2;
        spacer.weighty = 1;
        spacer.gridheight = 1;
        spacer.gridx = 2;
        spacer.gridy = 3;
        this.add(createSpacer(), spacer);
        spacer.gridy = 10;
        this.add(createSpacer(), spacer);
        spacer.gridx = 17;
        this.add(createSpacer(), spacer);
        spacer.gridy = 3;
        this.add(createSpacer(), spacer);
        // 1x2 vertical spacers, (6x total)
        spacer.gridheight = 2;
        spacer.gridy = 6;
        this.add(createSpacer(), spacer);
        spacer.gridx = 2;
        this.add(createSpacer(), spacer);
        spacer.gridx = 4;
        spacer.gridy = 1;
        this.add(createSpacer(), spacer);
        spacer.gridy = 11;
        this.add(createSpacer(), spacer);
        spacer.gridx = 15;
        this.add(createSpacer(), spacer);
        spacer.gridy = 1;
        this.add(createSpacer(), spacer);
        // 1x4 vertical spacers, center of each quarterfinal column(2x total)
        spacer.gridheight = 4;
        spacer.gridy = 5;
        this.add(createSpacer(), spacer);
        spacer.gridx = 4;
        this.add(createSpacer(), spacer);
        // 1x5 vertical spacers, above and below semifinals' cells (4x total)
        spacer.gridheight = 5;
        spacer.gridx = 6;
        spacer.gridy = 1;
        this.add(createSpacer(), spacer);
        spacer.gridy = 8;
        this.add(createSpacer(), spacer);
        spacer.gridx = 13;
        this.add(createSpacer(), spacer);
        spacer.gridy = 1;
        this.add(createSpacer(), spacer);
        // 4x5 vertical (& horizontal?) spacers, 1 each above and below final's cells including winner (2x)
        spacer.weightx = 1;
        spacer.gridwidth = 4;
        spacer.gridx = 8;
        this.add(createSpacer(), spacer);
        spacer.gridy = 8;
        this.add(createSpacer(), spacer);
    }
    /**
     * Creates an empty, transparent JPanel for use as a spacer.
     */
    private JPanel createSpacer(){
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);

        // uncomment the next line if u wanna see something ~nasty~

        // spacer.setBorder(BorderFactory.createLineBorder(Color.RED, 1, false));

        return spacer;
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
    /**
     * Creates small JPanels with two JLabels next to each other,
     * the first of which having an ImageIcon created by scaling
     * a BufferedImage of a flag with a separate method. Also has
     * accessor methods so initPanel can change things when called.
     */
    private class BracketCell extends JPanel {
        private final String defaultPath = "Assets" + File.separator + "blank.png";
        private GridBagConstraints cellConstraints;
        private JLabel flagLabel, teamLabel;
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

            cellSize = new Dimension(120+this.flagWidth, this.flagHeight);
            this.setMinimumSize(cellSize);
            this.setMaximumSize(cellSize);
            cellConstraints = new GridBagConstraints();

            this.flag = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(defaultPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", defaultPath);
            }

            flagLabel = new JLabel(scaledFlag(this.flagHeight), JLabel.LEFT);
            flagLabel.setOpaque(false);
            this.add(flagLabel, cellConstraints);

            teamLabel = new JLabel("Team Text");
            teamLabel.setForeground(Color.WHITE);
            cellConstraints.insets = new Insets(1,5,1,5);
            this.add(teamLabel, cellConstraints);
            this.setBackground(Color.DARK_GRAY);
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
            this.validate();
        }
        private ImageIcon getFlagIcon() {
            return (ImageIcon)(flagLabel.getIcon());
        }
        private void setFlagIcon(BufferedImage newFlag) {
            this.flag = newFlag;
            this.flagLabel.setIcon(scaledFlag(flagHeight));
            this.revalidate();
        }
        private String getTeamText() {
            return teamLabel.getText();
        }
        private void setTeamLabel(String teamLabel) {this.teamLabel.setText(teamLabel);}
        private ImageIcon scaledFlag(int targetHeight) {
            float scaleFactor = (float)(targetHeight) / (float)(this.flag.getHeight());
            int iconX = (int) (this.flag.getWidth() * scaleFactor);
            int iconY = (int) (this.flag.getHeight() * scaleFactor);
            Image scaledPreviewImage = this.flag.getScaledInstance(iconX, iconY, Image.SCALE_SMOOTH);
            BufferedImage image = new BufferedImage(iconX, iconY, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(scaledPreviewImage, 0, 0, null);
            return new ImageIcon(image);
        }
    }
}
