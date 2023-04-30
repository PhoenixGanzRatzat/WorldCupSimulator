package Frontend;

/**
 * Interface to ensure the various stage panels have necessary functions for GUi to be able to ensure the next stage
 * is ready to be displayed before switching to it visually
 */
public interface StagePanel {
    /**
     * Checks StagPanel state to see if the next stage can be moved onto
     * @return
     */
    public boolean checkIfCompleted();

    /**
     * prepares the panel for display
     */
    public void initPanel();
}
