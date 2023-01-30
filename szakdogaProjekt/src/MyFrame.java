
import GameObjects.Character;
import GameObjects.GameObject;
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
    MyPanel panel;
    private Structure selectedStructure;
    private ArrayList<Structure> availableStructures;
    private ArrayList<Character> availableCharacters;
    private HashMap<Integer, JButton> buttons;
    public MyFrame(){
        // the timer variable must be a javax.swing.Timer
        // TIMER_DELAY is a constant int and = 35;
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });

        panel = new MyPanel();
        availableStructures = new ArrayList<Structure>();
        availableCharacters = new ArrayList<Character>();
        buttons = new HashMap<Integer, JButton>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b=new JButton("Time - ");
        b.setBounds(10,480,95,30);
        b.addActionListener(new ActionListener() {
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


        this.add(b);

        JButton b2=new JButton("Time + ");
        b2.setBounds(150,480,95,30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(TIMER_DELAY<=400){
                    TIMER_DELAY += 20;

                    timer.setDelay( TIMER_DELAY );

                    System.out.println(TIMER_DELAY);
                }

            }
        });
        this.add(b2);



        JButton b3=new JButton("Pause/Play ");
        b3.setBounds(300,480,120,30);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                //System.out.println("ASDASD");
                //panel.getMap().getObjects().get(0).setRowPos(panel.getMap().getObjects().get(0).getRowPos()+1);
                //panel.getMap().UpdateObjects();
                //panel.renderMap();
                //System.out.println(panel.getMap().getObjects().get(0).getRowPos());
                if(timer.isRunning()){
                    timer.stop();
                }else{
                    timer.start();
                }

            }
        });
        this.add(b3);

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(clickStatus.contains("Placing Structure") ){
                    int x = e.getX();
                    int y = e.getY();
                    int row= (int)(double)(y/panel.getTilesize());
                    int col = (int)(double)x/panel.getTilesize();
                    placeSelectedStructure(row,col);

                }
                selectedStructure=null;
                clickStatus="none";
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
                    selectedStructure= new Structure(-1,-1,2,2,300,false,(byte)5,panel.getMap());
                    selectedStructure= availableStructures.get(buttonNumber);
                }
            });
            this.add(addStructure);
        }



        this.add(panel);

        this.setPreferredSize(new Dimension(panel.getDisplaySize()+200,panel.getDisplaySize()+400));


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
                panel.getMap().addStructure(selectedStructure);
            }

        }
    }

    public void gameLoop(){
        panel.getMap().UpdateObjects();
        panel.renderMap();
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
                Structure inputStruc = new Structure(-1, -1, width, height, storageCapacity, isHome, interactCapacity,panel.getMap());
                for (int i = 0; i < strucStats.length; i++) {
                    if (panel.getMap().getAvailableNeeds().containsKey(strucStats[i])) {
                        NeedValue personalNeed = new NeedValue(panel.getMap().getAvailableNeeds().get(strucStats[i]));
                        inputStruc.getNeeds().put(strucStats[i], personalNeed);
                    }
                }
                line = myReader.nextLine();
                strucStats = line.split(",");
                for (int i = 0; i < strucStats.length; i++) {
                    if (panel.getMap().getAvailableNeeds().containsKey(strucStats[i])) {
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
                Character inputChar = new Character(-1, -1, panel.getMap());
                for (int i = 2; i < charStats.length; i++) {
                    if (panel.getMap().getAvailableNeeds().containsKey(charStats[i])) {
                        NeedValue personalNeed = new NeedValue(panel.getMap().getAvailableNeeds().get(charStats[i]));
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
        for (int i = structure.getRowPos(); i < structure.getRowPos()+structure.getHeight(); i++) {
            for (int j = structure.getColPos(); j < structure.getColPos()+structure.getWidth(); j++) {
                System.out.println("Row: "+i+", Coloumn:"+j);
                if(i<panel.getMap().getRowSize() && j <panel.getMap().getColSize()){
                    if(panel.getMap().getTile(i,j).getOccupiedBy()!=null){
                        return false;
                    }

                }

            }
        }
        return true;
    }
}
