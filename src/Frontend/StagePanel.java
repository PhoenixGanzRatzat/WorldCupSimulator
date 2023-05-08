package Frontend;

import Backend.Match;
import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.util.List;

/**
 * Interface to ensure the various stage panels have necessary functions for GUi to be able to ensure the next stage
 * is ready to be displayed before switching to it visually
 */
public interface StagePanel {

    Color fifaBlue = new Color(50, 98, 149);
    Color buttonBackground = new Color(59, 133, 190);
    Color buttonBorder = new Color(203, 223, 239);
    Color buttonText = new Color(232, 246, 255);

    /**
     * prepares the panel for display
     */
    void initPanel();

    /**
     * Checks StagePanel state to see if the next stage can be moved onto
     * @return
     */
    void initPanel(List<Match> matches);
    boolean checkIfCompleted();

    boolean checkIfInitialized();
    @NotNull
    Color getThemeColor();
}
