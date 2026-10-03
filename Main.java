

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point for college project presentation.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            LaundryGUI gui = new LaundryGUI();
            gui.setVisible(true);
        });
    }
}
