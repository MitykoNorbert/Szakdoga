import java.awt.Color;
import java.awt.Graphics;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class CustomBasicSplitPaneUI extends BasicSplitPaneUI {

    private final Color foregroundColor;
    private final Color backgroundColor;
    private final int dividerWidth;

    public CustomBasicSplitPaneUI(Color foregroundColor, Color backgroundColor, int dividerWidth) {
        super();
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.dividerWidth = dividerWidth;
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new CustomSplitPaneDivider(this, foregroundColor, backgroundColor, dividerWidth);
    }

    @Override
    public void installDefaults() {
        super.installDefaults();
        splitPane.setBackground(backgroundColor);
    }

    @Override
    public void paint(Graphics g, javax.swing.JComponent c) {
        super.paint(g, c);
        splitPane.setBackground(backgroundColor);
    }
}
