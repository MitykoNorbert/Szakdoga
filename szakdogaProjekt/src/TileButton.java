import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;

public class TileButton extends JButton {
    private int rows;
    private int cols;
    private String label;

    public TileButton(int rows, int cols, String label) {
        this.rows = rows;
        this.cols = cols;
        this.label = label;
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth() - 10;
        int height = getHeight() - 40;
        int squareSize = Math.min(width / cols, height / rows);
        int x = (getWidth() - (squareSize * cols)) / 2;
        int y = (getHeight() - (squareSize * rows)) / 2;

        g.setColor(Color.BLACK);
        g.drawString(label, 5, 15);

        g.setColor(Color.GRAY);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.drawRect(x + j * squareSize, y + i * squareSize, squareSize, squareSize);
            }
        }
    }
}
