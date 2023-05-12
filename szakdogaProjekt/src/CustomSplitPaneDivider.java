import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class CustomSplitPaneDivider extends BasicSplitPaneDivider {
    private Color foregroundColor;
    private Color backgroundColor;
    private int dividerWidth;

    public CustomSplitPaneDivider(BasicSplitPaneUI ui, Color foregroundColor, Color backgroundColor, int dividerWidth) {
        super(ui);
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.dividerWidth = dividerWidth;
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (orientation == JSplitPane.VERTICAL_SPLIT) {
            // Draw a rectangle for the background color
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getHeight(), getWidth());

            // Draw a line for the divider
            g2.setColor(foregroundColor);
            g2.setStroke(new BasicStroke(dividerWidth));
            int middle = getHeight() / 2;
            g2.drawLine(0, middle, getWidth(), middle);
        } else {
            // Draw a rectangle for the background color
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw a line for the divider
            g2.setColor(foregroundColor);
            g2.setStroke(new BasicStroke(dividerWidth));
            int middle = getWidth() / 2;
            g2.drawLine(middle, 0, middle, getHeight());
        }
    }

}
