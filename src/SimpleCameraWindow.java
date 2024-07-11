import com.github.sarxos.webcam.Webcam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;


public class SimpleCameraWindow {
    private static Webcam webcam = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCameraWindow.class);

    public static void main(String[] args) {
        // Get the default webcam device
        
        webcam = Webcam.getDefault();

        // Create a new JFrame for the camera preview
        JFrame frame = new JFrame("Camera Preview");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close on window closing event

        // Create a JLabel to display the camera feed
        JLabel cameraLabel = new JLabel();

        // Create an exit button to close the application
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Panel for layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(cameraLabel, BorderLayout.CENTER);
        panel.add(exitButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // Start the camera preview in a separate thread for smooth UI updates
        new Thread(() -> {
            try {
                webcam.open(); // Open the webcam without specifying resolution
                while (frame.isVisible()) {
                    cameraLabel.setIcon(new ImageIcon(webcam.getImage()));
                    frame.repaint(); // Trigger UI update
                    Thread.sleep(33); // Update rate (adjust as needed)
                }
            } catch (Exception e) {
                LOGGER.error("Error while capturing camera image:", e);
            } finally {
                webcam.close();
            }
        }).start();
    }
}
