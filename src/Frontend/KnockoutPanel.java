package Frontend;

import Backend.Match;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Displays KnockoutStage results either one match at a time or
 * one round at a time, should the user choose to skip forward.
 **/
public class KnockoutPanel extends JPanel implements StagePanel {
    /**
     * TEMPORARY
     */
    public static void main(String[] args) {
        KnockoutPanel panel = new KnockoutPanel();
        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // attributes
    Match[] matches;
    int round;
    boolean initialized;

    /**
     * Default constructor; passes empty Match array to main constructor
     **/
    public KnockoutPanel() {
        this(new Match[]{});
    }

    /**
     * Main constructor; initializes JPanel with BorderLayout, sets size,
     * initializes matches, initializes init boolean, & calls createWindow()
     **/
    public KnockoutPanel(Match[] matches) {
        super(new BorderLayout());
        this.setSize(new Dimension(800, 600));
        this.matches = matches;
        this.round = 0;
        this.initialized = false;
        createWindow();
    }

    private void createWindow() {
        this.add(knockoutBracket(), BorderLayout.CENTER);
        this.add(knockoutButtons(), BorderLayout.SOUTH);
    }

    /*TODO:
     * Implement knockoutButtons(), nextMatch(), & nextRound()
     * Make knockoutBracket() draw lines between the labels (it paintComponent time)
     * Ponder the redundancy of createWindow()
     * Make more ugly ASCII art comments
     */
    private JPanel knockoutButtons() {
        JPanel buttons = new JPanel(new GridBagLayout());
        return buttons;
    }

    /**
     * Assembles a GridBagLayout-based bracket of labels for the teams competing in each match
     **/
    private JPanel knockoutBracket() {
        JPanel knock = new JPanel(new GridBagLayout());
        GridBagConstraints out = new GridBagConstraints();

        out.weightx = 0.2;
        out.weighty = 0.2;
        out.insets = new Insets(10, 10, 10, 10);
        out.ipadx = 180;
        out.ipady = 40;

        /*    << Bracket Column Legend with GridBagConstraints coordinates >>
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

        // initialized from left to right:


        // 8x left
        out.gridx = 2;
        out.gridy = 1;
        knock.add(new BracketCell(), out);
        out.gridy = 2;
        knock.add(new BracketCell(), out);
        out.gridy = 4;
        knock.add(new BracketCell(), out);
        out.gridy = 5;
        knock.add(new BracketCell(), out);
        out.gridy = 8;
        knock.add(new BracketCell(), out);
        out.gridy = 9;
        knock.add(new BracketCell(), out);
        out.gridy = 11;
        knock.add(new BracketCell(), out);
        out.gridy = 12;
        knock.add(new BracketCell(), out);

        // 4x left
        out.gridx = 4;
        out.gridy = 3;
        knock.add(new BracketCell(), out);
        out.gridy = 4;
        knock.add(new BracketCell(), out);
        out.gridy = 9;
        knock.add(new BracketCell(), out);
        out.gridy = 10;
        knock.add(new BracketCell(), out);

        // semi left
        out.gridx = 6;
        out.gridy = 6;
        knock.add(new BracketCell(), out);
        out.gridy = 7;
        knock.add(new BracketCell(), out);

        // final left
        out.gridx = 8;
        out.gridy = 6;
        knock.add(new BracketCell(), out);

        // winner
        out.gridwidth = 2;
        out.gridx = 9;
        out.gridy = 5;
        knock.add(new BracketCell(), out);
        out.gridwidth = 1;

        // final right
        out.gridx = 11;
        out.gridy = 6;
        knock.add(new BracketCell(), out);

        // semi right
        out.gridx = 13;
        out.gridy = 6;
        knock.add(new BracketCell(), out);
        out.gridy = 7;
        knock.add(new BracketCell(), out);

        // 4x right
        out.gridx = 15;
        out.gridy = 3;
        knock.add(new BracketCell(), out);
        out.gridy = 4;
        knock.add(new BracketCell(), out);
        out.gridy = 9;
        knock.add(new BracketCell(), out);
        out.gridy = 10;
        knock.add(new BracketCell(), out);

        // 8x right
        out.gridx = 17;
        out.gridy = 1;
        knock.add(new BracketCell(), out);
        out.gridy = 2;
        knock.add(new BracketCell(), out);
        out.gridy = 4;
        knock.add(new BracketCell(), out);
        out.gridy = 5;
        knock.add(new BracketCell(), out);
        out.gridy = 8;
        knock.add(new BracketCell(), out);
        out.gridy = 9;
        knock.add(new BracketCell(), out);
        out.gridy = 11;
        knock.add(new BracketCell(), out);
        out.gridy = 12;
        knock.add(new BracketCell(), out);

        return knock;
    }

    /**
     * Garbage method full of garbage.
     **/

    public void nextMatch() {
    }

    public void nextRound() {
    }

    @Override
    public boolean checkIfCompleted() {
        return initialized;
    }

    @Override
    public void initPanel() {
        initialized = true; // last line of method
    }
    private class BracketCell extends JPanel {
        private GridBagConstraints cell;
        private  Dimension cellSize;
        private JLabel flagLabel, teamLabel;
        private final String defaultPath = "Assets\\blank.png";
        private int imageWidth;
        private int imageHeight;
        private int flagWidth;
        private int flagHeight;
        private BufferedImage flag;
        private BracketCell() {
            super(new GridBagLayout());
            flagWidth = 46;
            flagHeight = 30;
            cellSize = new Dimension(80+flagWidth, flagHeight);
            this.setMinimumSize(cellSize);
            this.setMaximumSize(cellSize);
            imageWidth = 70;
            imageHeight = 46;
            flag = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            try {
                flag = ImageIO.read(new File(defaultPath));
            } catch(IOException e){
                System.out.printf("File not found at \"%s\"\n", defaultPath);
            }
            this.cell = new GridBagConstraints();
            flagLabel = new JLabel(scaledFlag(flagHeight), JLabel.LEFT);
            this.add(flagLabel, cell);
            teamLabel = new JLabel("Team Text");
            cell.insets = new Insets(1,5,1,5);
            this.add(teamLabel, cell);
            this.validate();
        }
        public ImageIcon getFlagIcon() {
            return (ImageIcon)(flagLabel.getIcon());
        }
        public void setFlagIcon(BufferedImage newFlag) {
            this.flag = newFlag;
            this.flagLabel.setIcon(scaledFlag(flagHeight));
            this.revalidate();

        }
        public String getTeamText() {
            return teamLabel.getText();
        }
        public void setTeamLabel(String teamLabel) {this.teamLabel.setText(teamLabel);}
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
