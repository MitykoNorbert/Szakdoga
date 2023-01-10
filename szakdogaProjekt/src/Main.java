

import javax.swing.*;
import java.awt.*;

public class Main {


    public static void main(String[] args) {


        System.out.println("hello");
        MyFrame frame = new MyFrame();




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
}
