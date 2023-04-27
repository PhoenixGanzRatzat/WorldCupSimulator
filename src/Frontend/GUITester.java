package Frontend;

import javax.swing.*;

public class GUITester {

    public static void main(String[] args) {

        GUI testGUI = new GUI();

        testGUI.setSize(640,480);
        testGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testGUI.setVisible(true);
    }
}
