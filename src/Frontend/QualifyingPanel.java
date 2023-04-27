package Frontend;

import javax.swing.*;

public class QualifyingPanel extends JPanel implements StagePanel  {

    public QualifyingPanel() {
        add(new JLabel("Qualifying Panel"));
    }
    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {

    }
}
