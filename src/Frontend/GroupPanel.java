package Frontend;

import javax.swing.*;

public class GroupPanel extends JPanel implements StagePanel {

    public GroupPanel() {
        add(new JLabel("Group Panel"));
    }
    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {

    }
}
