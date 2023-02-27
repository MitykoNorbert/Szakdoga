import javax.swing.*;
import java.awt.*;

public class CustomProgressBar extends JPanel {

    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JLabel label;


    public CustomProgressBar(int width, int height, Color barColor) {
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(barColor);

        progressLabel = new JLabel("0%");
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        label = new JLabel();
        add(label, BorderLayout.WEST);

        add(progressBar, BorderLayout.CENTER);
        add(progressLabel, BorderLayout.EAST);
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
        progressLabel.setText(value + "%");
    }
    public void setString(String text) {
        label.setText(text);

    }

    public String getText() {
        return label.getText();

    }
}