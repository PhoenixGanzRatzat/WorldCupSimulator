package Frontend;

import javax.swing.*;

public class KnockoutPanel extends JPanel implements StagePanel  {

    public KnockoutPanel() {
        add(new JLabel("Knockout Panel"));
    }
    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {

    }
}
