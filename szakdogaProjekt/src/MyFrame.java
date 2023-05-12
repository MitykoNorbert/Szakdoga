
import GameObjects.Character;
import GameObjects.NeedValue;
import GameObjects.Structure;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MyFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 0L;
    private int TIMER_DELAY = 300;
    final Timer timer = new Timer(TIMER_DELAY, null);
    private String clickStatus = "none";
    MyPanel tileMapPanel;
    private Structure selectedStructure;
    private Character selectedCharacter;
    private JButton selectedButton;
    private ArrayList<Structure> availableStructures;
    private ArrayList<Character> availableCharacters;
    private HashMap<Integer, JButton> buttons;
    private boolean paused;
    private JPanel panel2, panel3, panel4, tileMapContainer;

    public MyFrame() {
        // the timer variable must be a javax.swing.Timer
        // TIMER_DELAY is a constant int and = 35;
        if (!timer.isRunning()) {
            timer.start();
        }
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
                UpdatePanel4();
                UpdatePanel3();
            }
        });
        paused = true;
        tileMapPanel = new MyPanel();
        availableStructures = new ArrayList<Structure>();
        availableCharacters = new ArrayList<Character>();
        buttons = new HashMap<Integer, JButton>();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// Create the containers for the North and South sides
        JPanel northContainer = new JPanel(new BorderLayout());
        JPanel southContainer = new JPanel(new BorderLayout());

// Create panel2, panel3, and panel4
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        tileMapContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tileMapContainer.add(tileMapPanel);

// Create the progress bars in panel3
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));


        Color backSplit = new Color(65, 40, 10, 252);
        Color frontSplit = new Color(84, 46, 10, 155);


        // Create the split pane between tileMapContainer and panel3
        JSplitPane tileMapSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tileMapContainer, panel3);
        tileMapSplitPane.setResizeWeight(0.8);
        tileMapSplitPane.setUI(new CustomBasicSplitPaneUI(frontSplit, backSplit, 5)); // set the custom UI

// Create the split pane between panel2 and panel4
        JSplitPane panel2SplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel2, panel4);
        panel2SplitPane.setResizeWeight(0.7);
        panel2SplitPane.setUI(new CustomBasicSplitPaneUI(frontSplit, backSplit, 5)); // set the custom UI

// Create the split pane between tileMapSplitPane and panel2SplitPane
        JSplitPane northSouthSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tileMapSplitPane, panel2SplitPane);
        northSouthSplitPane.setResizeWeight(0.8);
        northSouthSplitPane.setUI(new CustomBasicSplitPaneUI(frontSplit, backSplit, 5)); // set the custom UI


// Add the components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(northSouthSplitPane, BorderLayout.CENTER);


        Border compound = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(96, 54, 21), 10, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        compound = BorderFactory.createCompoundBorder(compound, BorderFactory.createLineBorder(new Color(150, 75, 0), 5, true));
        tileMapContainer.setBorder(compound);
        Color bgColor = new Color(138, 92, 36);
        tileMapContainer.setBackground(bgColor);


        JButton timeSpeedButton = new JButton("Time - ");
        timeSpeedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //System.out.println("ASDASD");
                //panel.getMap().getObjects().get(0).setRowPos(panel.getMap().getObjects().get(0).getRowPos()+1);
                //panel.getMap().UpdateObjects();
                //panel.renderMap();
                //System.out.println(panel.getMap().getObjects().get(0).getRowPos());
                if (TIMER_DELAY >= 50) {
                    TIMER_DELAY -= 20;
                    timer.setDelay(TIMER_DELAY);
                    //System.out.println(TIMER_DELAY);
                }

            }
        });
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JPanel buttonPanel3 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(buttonPanel1);
        panel2.add(buttonPanel2);
        panel2.add(buttonPanel3);
        buttonPanel1.add(timeSpeedButton);

        JButton timeSlowButton = new JButton("Time + ");
        timeSlowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TIMER_DELAY <= 400) {
                    TIMER_DELAY += 20;

                    timer.setDelay(TIMER_DELAY);

                    //System.out.println(TIMER_DELAY);
                }

            }
        });
        buttonPanel1.add(timeSlowButton);
        JButton timePauseButton = new JButton("Pause/Play ");
        timePauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                paused = !paused;

            }
        });
        buttonPanel1.add(timePauseButton);

        this.tileMapPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row = (int) (double) (y / MyFrame.this.tileMapPanel.getTilesize());
                int col = (int) (double) x / MyFrame.this.tileMapPanel.getTilesize();
                if (clickStatus.contains("Placing Structure")) {
                    placeSelectedStructure(row, col);
                } else {
                    selectedCharacter = tileMapPanel.getMap().charAtPosition(row, col);
                    LoadPanel4();
                }
                selectedStructure = null;
                selectedButton = null;
                clickStatus = "none";
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //panel4.setBackground(bgColor);
        //panel3.setBackground(bgColor);
        //panel2.setBackground(bgColor);
        //buttonPanel1.setBackground(bgColor);
        //buttonPanel2.setBackground(bgColor);
        //buttonPanel3.setBackground(bgColor);
        LoadAvailableBuildings();
        LoadPanel3();
        LoadPanel4();
        for (int i = 0; i < availableStructures.size(); i++) {
            TileButton addStructure = new TileButton(availableStructures.get(i).getHeight(), availableStructures.get(i).getWidth(), availableStructures.get(i).getName());
            buttons.put(i, addStructure);
            int buttonNumber = i;
            addStructure.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickStatus = "Placing Structure";
                    selectedStructure = availableStructures.get(buttonNumber);
                    selectedButton = buttons.get(buttonNumber);
                }
            });
            buttonPanel2.add(addStructure);
        }


        this.setPreferredSize(new Dimension(this.tileMapPanel.getDisplaySize() + 200, this.tileMapPanel.getDisplaySize() + 400));


        this.setLocationRelativeTo(null);

        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getPreferredSize().width / 2, dim.height / 2 - this.getPreferredSize().height / 2);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

    }

    public void placeSelectedStructure(int row, int col) {
        if (selectedStructure != null) {
            selectedStructure.setRowPos(row);
            selectedStructure.setColPos(col);
            if (isPlaceable(selectedStructure)) {
                tileMapPanel.getMap().addStructure(selectedStructure);
                //availableStructures.remove(selectedStructure);
                selectedButton.setVisible(false);
                getContentPane().remove(selectedButton); // Remove button from JFrame
                revalidate(); // Refresh JFrame to reflect changes
                repaint();
                //buttons.remove(selectedButton);
            }

        }
    }

    public void gameLoop() {
        if (!paused) {
            tileMapPanel.getMap().UpdateObjects();
            tileMapPanel.renderMap();
            if (tileMapPanel.getMap().requirementCheck()) {
                tileMapPanel.getMap().setGameState("Completed");
                System.out.println("YOU WIN!!");
                paused = true;
                JOptionPane.showMessageDialog(this, "Level completed", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                System.exit(0);
            } else if (tileMapPanel.getMap().getGameState().equals("Failed")) {
                System.out.println("LEVEL FAILED");
                paused = true;
                JOptionPane.showMessageDialog(this, "Level Failed", "We're sorry..", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                System.exit(0);
            }
        }

    }

    public void createWindow() {
        JFrame frame = new JFrame("Simple gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel textLabel = new JLabel("this is the label", SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(300, 100));
        frame.getContentPane().add(textLabel, BorderLayout.CENTER);
        //display
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void LoadAvailableBuildings() {


        //System.out.println("Structures importing..");
        try {
            File needslistFile = new File("LoadedLevel/AvailableStructures.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.equals("-")) {
                    line = myReader.nextLine();
                }
                String[] strucStats = line.split(",");

                String name = strucStats[0];
                //System.out.println("EZ A NEVEM: " + name);
                //System.out.println(line);
                int width = Integer.parseInt(strucStats[1]);
                int height = Integer.parseInt(strucStats[2]);
                int storageCapacity = Integer.parseInt(strucStats[3]);
                boolean isHome = false;
                if (strucStats[4].equals("home") || strucStats[4].equals("true")) {
                    isHome = true;
                }
                byte interactCapacity = Byte.parseByte(strucStats[5]);
                line = myReader.nextLine();
                strucStats = line.split(",");
                Structure inputStruc = new Structure(-1, -1, width, height, storageCapacity, isHome, interactCapacity, tileMapPanel.getMap(), tileMapPanel.getMap().getAvailableItems());
                for (int i = 0; i < strucStats.length; i++) {
                    if (tileMapPanel.getMap().getAvailableNeeds().containsKey(strucStats[i])) {
                        NeedValue personalNeed = new NeedValue(tileMapPanel.getMap().getAvailableNeeds().get(strucStats[i]));
                        inputStruc.getNeeds().put(strucStats[i], personalNeed);
                    }
                }
                line = myReader.nextLine();
                strucStats = line.split(",");
                for (int i = 0; i < strucStats.length; i++) {
                    if (tileMapPanel.getMap().getAvailableNeeds().containsKey(strucStats[i])) {
                        String provision = strucStats[i];
                        inputStruc.getProvides().put(provision, 1);
                    }
                }
                line = myReader.nextLine();
                if (!line.equals("-")) {
                    strucStats = line.split(",");
                    for (int i = 0; i < strucStats.length; i += 2) {
                        String item = strucStats[i];
                        int value = Integer.parseInt(strucStats[i + 1]);
                        inputStruc.getProvides().put(item, value);

                    }
                }

                inputStruc.setName(name);
                availableStructures.add(inputStruc);


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred while reading AvailableStructures.txt.");
            e.printStackTrace();
        }
        //System.out.println("Available structures Imported");
    }

    public void LoadAvailableCharacters() {
        try {
            File needslistFile = new File("LoadedLevel/Characters.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] charStats = line.split(",");
                Character inputChar = new Character(-1, -1, tileMapPanel.getMap());
                for (int i = 2; i < charStats.length; i++) {
                    if (tileMapPanel.getMap().getAvailableNeeds().containsKey(charStats[i])) {
                        NeedValue personalNeed = new NeedValue(tileMapPanel.getMap().getAvailableNeeds().get(charStats[i]));
                        inputChar.getNeeds().put(charStats[i], personalNeed);
                    }
                }
                //objects.add(inputChar);


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading Characters.txt.");
            e.printStackTrace();
        }
        //System.out.println("chars imported");
    }

    public boolean isPlaceable(Structure structure) {
        if (structure.getRowPos() + structure.getHeight() > tileMapPanel.getMap().getRowSize() || structure.getColPos() + structure.getWidth() > tileMapPanel.getMap().getColSize()) {

            return false;
        }


        //System.out.println(structure.getRowPos() + structure.getHeight() + "Az < mint " + tileMapPanel.getMap().getRowSize());
        for (int i = structure.getRowPos(); i < structure.getRowPos() + structure.getHeight(); i++) {
            for (int j = structure.getColPos(); j < structure.getColPos() + structure.getWidth(); j++) {
                //System.out.println("Row: " + i + ", Coloumn:" + j);
                if (i < tileMapPanel.getMap().getRowSize() && j < tileMapPanel.getMap().getColSize()) {
                    if (tileMapPanel.getMap().getTile(i, j).getOccupiedBy() != null) {
                        return false;
                    }

                }

            }
        }
        return true;
    }

    public void LoadPanel4() {
        panel4.setPreferredSize(new Dimension(400, 400));
        panel4.removeAll();

        if (selectedCharacter == null) {
            panel4.repaint();
        } else {
            //System.out.println(selectedCharacter);
            for (String need : selectedCharacter.getNeeds().keySet()) {
                CustomProgressBar bar = new CustomProgressBar((int) (panel4.getWidth() * 0.6), 20, Color.cyan);
                bar.setString(need);
                bar.setProgress((int) (selectedCharacter.getNeeds().get(need).getPercentage() * 100));
                bar.setPreferredSize(new Dimension((int) (panel4.getWidth() * 0.6), 15));
                panel4.add(bar);
            }
            JLabel messageLabel = new JLabel("Current task:");
            messageLabel.setFont(new Font("Sans Serif", Font.PLAIN, 18));
            messageLabel.setForeground(Color.BLACK);
            messageLabel.setPreferredSize(new Dimension(panel4.getWidth(), 200));
            panel4.add(messageLabel);
        }
        panel4.revalidate();
        panel4.repaint();
        UpdatePanel4();
    }

    public void UpdatePanel4() {
        if (selectedCharacter != null) {
            for (Component comp : panel4.getComponents()) {
                if (comp instanceof CustomProgressBar) {
                    CustomProgressBar bar = (CustomProgressBar) comp;
                    bar.setProgress((int) (selectedCharacter.getNeeds().get(bar.getText()).getPercentage() * 100));
                    bar.setPreferredSize(new Dimension((int) (panel4.getWidth() * 0.7), 15));
                } else if (comp instanceof JLabel) {
                    comp.setPreferredSize(new Dimension(panel4.getWidth(), 200));
                    if (selectedCharacter.getCurrentTask() != null) {
                        ((JLabel) comp).setText(selectedCharacter.getCurrentTask().getName());
                    } else {
                        ((JLabel) comp).setText("No task");
                    }
                }
            }
        }

        panel4.revalidate();
    }

    public void LoadPanel3() {
        panel3.removeAll();
        for (String need : tileMapPanel.getMap().getAvailableNeeds().keySet()) {
            CustomProgressBar bar = new CustomProgressBar(200, 10, Color.yellow);
            bar.setString(need);
            bar.setPreferredSize(new Dimension(100, 15));
            panel3.add(bar);
        }
        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        messageLabel.setForeground(Color.black);
        messageLabel.setPreferredSize(new Dimension(200, 100));
        panel3.add(messageLabel);

        panel3.revalidate();
        panel3.repaint();
        UpdatePanel3();
    }

    public void UpdatePanel3() {
        for (Component comp : panel3.getComponents()) {
            if (comp instanceof CustomProgressBar) {
                CustomProgressBar bar = (CustomProgressBar) comp;
                bar.setProgress(tileMapPanel.getMap().getAverageLevelOf(bar.getText()));
                bar.setPreferredSize(new Dimension(100, 10));
            } else if (comp instanceof JLabel) {
                ((JLabel) comp).setText("Number of deaths: " + tileMapPanel.getMap().getDeaths());
            }
        }
        //System.out.println(panel3.getComponents());
        panel3.revalidate();
        panel3.repaint();
    }
}
