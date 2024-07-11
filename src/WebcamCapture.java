import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebcamCapture {

    private static Webcam webcam = null;
    private static JButton captureButton;

    public static void main(String[] args) throws IOException {

        // Initialize webcam (assuming webcam-capture-0.3.12.jar is in the classpath)
        webcam = Webcam.getDefault();
        if (webcam == null) {
            System.out.println("No webcam found!");
            return;
        }

        webcam.setViewSize(new Dimension(640, 480)); // Set webcam resolution (optional)
        webcam.open();  // Open webcam

        // Create a JFrame window
        JFrame frame = new JFrame("Webcam Capture");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JLabel to display the webcam feed (optional)
        JLabel webcamLabel = new JLabel();
        webcamLabel.setPreferredSize(new Dimension(640, 480));  // Match webcam resolution

        // Create a button to capture the image
        captureButton = new JButton("Capture");
        captureButton.addActionListener(new CaptureButtonListener());

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.add(webcamLabel);
        panel.add(captureButton);
        frame.getContentPane().add(panel);

        // Update the webcam image in a separate thread (optional, for smoother display)
        new Thread(() -> {
            while (webcam.isOpen()) {
                BufferedImage image = webcam.getImage();
                webcamLabel.setIcon(new ImageIcon(image));
                try {
                    Thread.sleep(33);  // Update image every 33 milliseconds (adjust as needed)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Set frame size and visibility
        frame.pack();
        frame.setVisible(true);
    }

    static class CaptureButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (webcam.isOpen()) {
                // Capture image
                BufferedImage image = webcam.getImage();

                // Save image to file (modify filename and format as needed)
                try {
                    ImageIO.write(image, "jpg", new File("webcam_capture_sarxos.jpg"));
                    System.out.println("Picture captured!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
