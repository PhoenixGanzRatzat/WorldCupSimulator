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

    public final static Color fifaBlue = new Color(50, 98, 149);
    public final static Color fifaLite = fifaBlue.brighter();
    public final static Color fifaDark = fifaBlue.darker();
    public final static Color buttonText = new Color(213, 226, 216);

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
    @NotNull
    public Color getThemeColor();
}
