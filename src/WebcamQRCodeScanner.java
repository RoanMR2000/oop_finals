import com.github.sarxos.webcam.Webcam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

public class WebcamQRCodeScanner {

  private static Webcam webcam = null;
  private static JButton captureButton;
  private static String qrCodeText = null; // Stores the latest decoded QR code
  private static JTextField qrCodeResultTextField; 

  public static void main(String[] args) throws IOException {

    // Initialize webcam (assuming webcam-capture-0.3.12.jar is in the classpath)
    webcam = Webcam.getDefault();
    if (webcam == null) {
      System.out.println("No webcam found!");
      return;
    }

    webcam.setViewSize(new Dimension(640, 480)); // Set webcam resolution (optional)
    webcam.open(); // Open webcam

    // Create a JFrame window
    JFrame frame = new JFrame("QR Code Scanner");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a JLabel to display the webcam feed (optional)
    JLabel webcamLabel = new JLabel();
    webcamLabel.setPreferredSize(new Dimension(640, 480)); // Match webcam resolution

    // Create a button to capture the image
    captureButton = new JButton("Capture");
    captureButton.addActionListener(new CaptureButtonListener());

    // Create a text field to display QR code result
    qrCodeResultTextField = new JTextField();
    qrCodeResultTextField.setEditable(false); // Set text field to read-only

    // Add components to the panel
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout()); // Use BorderLayout for positioning
    panel.add(webcamLabel, BorderLayout.CENTER); // Webcam feed in center
    panel.add(captureButton, BorderLayout.WEST); // Capture button on the left
    panel.add(qrCodeResultTextField, BorderLayout.SOUTH); // QR code result at bottom

    frame.getContentPane().add(panel);

    // Update the webcam image in a separate thread (optional, for smoother display)
    new Thread(() -> {
      while (webcam.isOpen()) {
        BufferedImage image = webcam.getImage();
        webcamLabel.setIcon(new ImageIcon(image));

        // Convert image to ZXing format
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
          // Attempt to decode QR code
          Result result = new MultiFormatReader().decode(bitmap);
          qrCodeText = result.getText(); // Update qrCodeText with the latest decoded value
          qrCodeResultTextField.setText("QR Code: " + qrCodeText); 
        } catch (NotFoundException e) {
          // No QR code found, keep qrCodeText unchanged
        }

        try {
          Thread.sleep(33); // Update image every 33 milliseconds (adjust as needed)
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
        // Capture image (optional, functionality not modified for brevity)
        // ...
      }
    }
  }
}
