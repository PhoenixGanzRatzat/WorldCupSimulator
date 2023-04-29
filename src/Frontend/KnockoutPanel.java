package Frontend;

import Backend.Match;

import javax.swing.*;
import java.awt.*;
/**
 * Displays KnockoutStage results either one match at a time or
 * one round at a time, should the user choose to skip forward.
 **/
public class KnockoutPanel extends JPanel implements StagePanel  {
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
        this.setSize(new Dimension(800,600));
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
     * Make flagLabel() less garbage.
     */
    private JPanel knockoutButtons() {
        JPanel buttons = new JPanel (new GridBagLayout());
        return buttons;
    }
    /**
     * Assembles a GridBagLayout-based bracket of labels for the teams competing in each match
     * **/
    private JPanel knockoutBracket() {
        JPanel knock = new JPanel(new GridBagLayout());
        GridBagConstraints out = new GridBagConstraints();

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
        knock.add(flagLabel(), out);
        out.gridy = 2;
        knock.add(flagLabel(), out);
        out.gridy = 4;
        knock.add(flagLabel(), out);
        out.gridy = 5;
        knock.add(flagLabel(), out);
        out.gridy = 8;
        knock.add(flagLabel(), out);
        out.gridy = 9;
        knock.add(flagLabel(), out);
        out.gridy = 11;
        knock.add(flagLabel(), out);
        out.gridy = 12;
        knock.add(flagLabel(), out);

        // 4x left
        out.gridx = 4;
        out.gridy = 3;
        knock.add(flagLabel(), out);
        out.gridy = 4;
        knock.add(flagLabel(), out);
        out.gridy = 9;
        knock.add(flagLabel(), out);
        out.gridy = 10;
        knock.add(flagLabel(), out);

        // semi left
        out.gridx = 6;
        out.gridy = 6;
        knock.add(flagLabel(), out);
        out.gridy = 7;
        knock.add(flagLabel(), out);

        // final left
        out.gridx = 8;
        out.gridy = 6;
        knock.add(flagLabel(), out);

        // winner
        out.gridwidth = 2;
        out.gridx = 9;
        out.gridy = 5;
        knock.add(flagLabel(), out);
        out.gridwidth = 1;

        // final right
        out.gridx = 11;
        out.gridy = 6;
        knock.add(flagLabel(), out);

        // semi right
        out.gridx = 13;
        out.gridy = 6;
        knock.add(flagLabel(), out);
        out.gridy = 7;
        knock.add(flagLabel(), out);

        // 4x right
        out.gridx = 15;
        out.gridy = 3;
        knock.add(flagLabel(), out);
        out.gridy = 4;
        knock.add(flagLabel(), out);
        out.gridy = 9;
        knock.add(flagLabel(), out);
        out.gridy = 10;
        knock.add(flagLabel(), out);

        // 8x right
        out.gridx = 17;
        out.gridy = 1;
        knock.add(flagLabel(), out);
        out.gridy = 2;
        knock.add(flagLabel(), out);
        out.gridy = 4;
        knock.add(flagLabel(), out);
        out.gridy = 5;
        knock.add(flagLabel(), out);
        out.gridy = 8;
        knock.add(flagLabel(), out);
        out.gridy = 9;
        knock.add(flagLabel(), out);
        out.gridy = 11;
        knock.add(flagLabel(), out);
        out.gridy = 12;
        knock.add(flagLabel(), out);

        return knock;
    }
    /**
     * Garbage method full of garbage.
     **/
    private JPanel flagLabel() {
        JPanel cell = new JPanel();

        cell.setBackground(Color.BLACK);
        cell.setForeground(Color.BLACK);

        Dimension box = new Dimension(100, 60);

        cell.setSize(box);
        cell.setPreferredSize(box);
        cell.setMinimumSize(box);
        cell.setMaximumSize(box);

        return cell;
    }
    public void nextMatch() {}
    public void nextRound() {}
    @Override
    public boolean checkIfCompleted() {
        return initialized;
    }
    @Override
    public void initPanel() {
        initialized = true; // last line of method
    }
}
