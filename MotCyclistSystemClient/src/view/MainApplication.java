package view;

import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CooperativeForm();
            new MotorcyclistForm();
        });
    }
}