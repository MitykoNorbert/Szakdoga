
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MyFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 0L;
    private int TIMER_DELAY = 300;
    final Timer timer = new Timer(TIMER_DELAY, null);
    MyPanel panel;
    public MyFrame(){
        // the timer variable must be a javax.swing.Timer
        // TIMER_DELAY is a constant int and = 35;
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });

        panel = new MyPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b=new JButton("Time - ");
        b.setBounds(10,510,95,30);
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
        b2.setBounds(150,510,95,30);
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
        b3.setBounds(300,510,120,30);
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
        this.add(panel);

        this.setPreferredSize(new Dimension(panel.getDisplaySize()+100,panel.getDisplaySize()+100));


        this.setLocationRelativeTo(null);

        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getPreferredSize().width/2, dim.height/2-this.getPreferredSize().height/2);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

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
}
