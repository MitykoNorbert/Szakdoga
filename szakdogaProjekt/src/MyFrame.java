
import GameObjects.Character;
import GameObjects.NeedValue;
import GameObjects.Structure;

import javax.swing.*;
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
    private JPanel panel2,panel3,panel4;
    public MyFrame(){
        // the timer variable must be a javax.swing.Timer
        // TIMER_DELAY is a constant int and = 35;
        if(!timer.isRunning()){
            timer.start();
        }
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
                UpdatePanel4();
            }
        });
        paused = true;
        tileMapPanel = new MyPanel();
        availableStructures = new ArrayList<Structure>();
        availableCharacters = new ArrayList<Character>();
        buttons = new HashMap<Integer, JButton>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel tilemapContainer = new JPanel(new FlowLayout()); // use a FlowLayout for the tilemap
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();

        // Set background colors for each panel for visualization
        tilemapContainer.setBackground(Color.RED);
        panel2.setBackground(Color.BLUE);
        panel3.setBackground(Color.GREEN);
        panel4.setBackground(Color.YELLOW);

        // Create a JPanel for the tilemap

        tileMapPanel.setBackground(Color.WHITE);

        // Add the tilemapPanel to the tilemapContainer
        tilemapContainer.add(tileMapPanel);

        // Set the layout manager for the frame to GridBagLayout
        this.setLayout(new GridBagLayout());

        // Set constraints for each panel
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.weightx = 0.7;
        c.weighty = 0.7;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST; // add this line to anchor the panel to the top-left corner
        this.add(tilemapContainer, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 0.7;
        c.weighty = 0.3;
        c.fill = GridBagConstraints.BOTH;
        this.add(panel2, c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.3;
        c.weighty = 0.7;
        c.fill = GridBagConstraints.BOTH;
        this.add(panel3, c);

        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.3;
        c.weighty = 0.3;
        c.fill = GridBagConstraints.BOTH;
        this.add(panel4, c);

        JButton timeSpeedButton=new JButton("Time - ");
        timeSpeedButton.setBounds(10,480,95,30);
        timeSpeedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                //System.out.println("ASDASD");
                //panel.getMap().getObjects().get(0).setRowPos(panel.getMap().getObjects().get(0).getRowPos()+1);
                //panel.getMap().UpdateObjects();
                //panel.renderMap();
                //System.out.println(panel.getMap().getObjects().get(0).getRowPos());
                if(TIMER_DELAY>=50){
                    TIMER_DELAY -= 20;
                    timer.setDelay( TIMER_DELAY );
                    System.out.println(TIMER_DELAY);
                }

            }
        });
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JPanel buttonPanel3 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(buttonPanel1,BorderLayout.NORTH);
        panel2.add(buttonPanel2, BorderLayout.NORTH);
        panel2.add(buttonPanel3, BorderLayout.NORTH);

        panel2.setPreferredSize(new Dimension((int)(this.getWidth() * 0.7), (int)(this.getHeight() * 0.3)));
        LoadPanel4();
        buttonPanel1.add(timeSpeedButton);

        JButton timeSlowButton=new JButton("Time + ");
        timeSlowButton.setBounds(150,480,95,30);
        timeSlowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(TIMER_DELAY<=400){
                    TIMER_DELAY += 20;

                    timer.setDelay( TIMER_DELAY );

                    System.out.println(TIMER_DELAY);
                }

            }
        });
        buttonPanel1.add(timeSlowButton);



        JButton timePauseButton=new JButton("Pause/Play ");
        timePauseButton.setBounds(300,480,120,30);
        timePauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                paused=!paused;

            }
        });
        buttonPanel1.add(timePauseButton);

        this.tileMapPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row= (int)(double)(y/ MyFrame.this.tileMapPanel.getTilesize());
                int col = (int)(double)x/ MyFrame.this.tileMapPanel.getTilesize();
                if(clickStatus.contains("Placing Structure") ){
                    placeSelectedStructure(row,col);
                }else{
                    selectedCharacter = tileMapPanel.getMap().charAtPosition(row,col);
                    LoadPanel4();
                }
                selectedStructure=null;
                selectedButton=null;
                clickStatus="none";
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

        LoadAvailableBuildings();
        for (int i = 0; i < availableStructures.size(); i++) {
            JButton addStructure=new JButton(availableStructures.get(i).getName());
            int offsetHorizontal = i%4;
            int offsetVertical =i/4;
            addStructure.setBounds(10+offsetHorizontal*140,540+offsetVertical*50,120,30);
            buttons.put(i,addStructure);
            int buttonNumber=i;
            addStructure.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    clickStatus = "Placing Structure";
                    selectedStructure= availableStructures.get(buttonNumber);
                    selectedButton= buttons.get(buttonNumber);
                }
            });
            buttonPanel2.add(addStructure);
        }


        this.setPreferredSize(new Dimension(this.tileMapPanel.getDisplaySize()+200, this.tileMapPanel.getDisplaySize()+400));


        this.setLocationRelativeTo(null);

        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getPreferredSize().width/2, dim.height/2-this.getPreferredSize().height/2);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

    }
    public void placeSelectedStructure(int row,int col){
        if(selectedStructure!=null){
            selectedStructure.setRowPos(row);
            selectedStructure.setColPos(col);
            if (isPlaceable(selectedStructure)){
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

    public void gameLoop(){
        if(!paused){
            tileMapPanel.getMap().UpdateObjects();
            tileMapPanel.renderMap();
        }

    }
    public void createWindow(){
        JFrame frame = new JFrame("Simple gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel textLabel = new JLabel("this is the label", SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(300,100));
        frame.getContentPane().add(textLabel, BorderLayout.CENTER);
        //display
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void LoadAvailableBuildings(){


        //TODO
        System.out.println("Structures importing..");
        try {
            File needslistFile = new File("AvailableStructures.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] strucStats = line.split(",");

                String name = strucStats[0];
                System.out.println("EZ A NEVEM: "+name);
                int width = Integer.parseInt(strucStats[1]);
                int height = Integer.parseInt(strucStats[2]);
                int storageCapacity = Integer.parseInt(strucStats[3]);
                boolean isHome = false;
                if(strucStats[4].equals("home") || strucStats[4].equals("true")){
                    isHome = true;
                }
                byte interactCapacity = Byte.parseByte(strucStats[5]);
                line = myReader.nextLine();
                strucStats = line.split(",");
                Structure inputStruc = new Structure(-1, -1, width, height, storageCapacity, isHome, interactCapacity, tileMapPanel.getMap());
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
                        inputStruc.getProvides().put(provision,1);
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
        System.out.println("Available structures Imported");
    }
    public void LoadAvailableCharacters(){
        try {
            File needslistFile = new File("Characters.txt");
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
        System.out.println("chars imported");
    }
    public boolean isPlaceable(Structure structure){
        if(structure.getRowPos()+structure.getHeight()> tileMapPanel.getMap().getRowSize() || structure.getColPos()+structure.getWidth() > tileMapPanel.getMap().getColSize()){
            System.out.println("HALO NEM JO");
            return false;
        }
        System.out.println("MIERT FUTOK LE");

        System.out.println(structure.getRowPos()+structure.getHeight()+"Az < mint "+ tileMapPanel.getMap().getRowSize());
        for (int i = structure.getRowPos(); i < structure.getRowPos()+structure.getHeight(); i++) {
            for (int j = structure.getColPos(); j < structure.getColPos()+structure.getWidth(); j++) {
                System.out.println("Row: "+i+", Coloumn:"+j);
                if(i< tileMapPanel.getMap().getRowSize() && j < tileMapPanel.getMap().getColSize()){
                    if(tileMapPanel.getMap().getTile(i,j).getOccupiedBy()!=null){
                        return false;
                    }

                }

            }
        }
        return true;
    }
    public void LoadPanel4(){
        panel4.setPreferredSize(new Dimension(400,400));
        panel4.removeAll();

        if(selectedCharacter==null){
            panel4.repaint();
        }else{
            System.out.println(selectedCharacter);
            for (String need: selectedCharacter.getNeeds().keySet()) {
                CustomProgressBar bar = new CustomProgressBar(300,20, Color.cyan);
                bar.setString(need);
                bar.setProgress((int)(selectedCharacter.getNeeds().get(need).getPercentage()*100));
                panel4.add(bar);
                System.out.println("added "+need);
            }
        }
        panel4.revalidate();
        panel4.repaint();

        //CustomProgressBar health = new CustomProgressBar(300,20,Color.red);
        //health.setProgress(30);
        //panel4.add(health);
    }
    public void UpdatePanel4(){
        if(selectedCharacter!=null){
            for (Component comp:panel4.getComponents()) {
                if(comp instanceof CustomProgressBar){
                    CustomProgressBar bar = (CustomProgressBar) comp;
                    bar.setProgress((int)(selectedCharacter.getNeeds().get(bar.getText()).getPercentage()*100));

                }
            }
        }


    }
}
