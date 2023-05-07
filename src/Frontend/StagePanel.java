package Frontend;

import Backend.Match;

import java.awt.*;
import java.util.List;

/**
 * Interface to ensure the various stage panels have necessary functions for GUi to be able to ensure the next stage
 * is ready to be displayed before switching to it visually
 */
public interface StagePanel {
    /**
     * prepares the panel for display
     */
    public void initPanel();

    /**
     * Checks StagePanel state to see if the next stage can be moved onto
     * @return
     */
    public void initPanel(List<Match> matches);
    public boolean checkIfCompleted();

    public boolean checkIfInitialized();

    public Color getBGColor();
}
