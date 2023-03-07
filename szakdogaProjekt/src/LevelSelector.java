import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LevelSelector extends JFrame {
    private JButton startButton;
    private JFileChooser fileChooser;
    private File selectedFolder;
    private JLabel statusLabel;
    private JPanel panel;

    public LevelSelector() {
        setTitle("Level Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);


        panel = new JPanel(new BorderLayout());
        add(panel);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        panel.add(fileChooser, BorderLayout.CENTER);

        startButton = new JButton("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyLevel();
            }
        });
        panel.add(startButton, BorderLayout.SOUTH);


        statusLabel = new JLabel("Please select a level folder.");
        panel.add(statusLabel, BorderLayout.NORTH);


        fileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedFolder = fileChooser.getSelectedFile();
                if (selectedFolder != null && selectedFolder.isDirectory()) {
                    startButton.setEnabled(true);
                    statusLabel.setText("Selected folder: " + selectedFolder.getAbsolutePath());
                } else {
                    startButton.setEnabled(false);
                    statusLabel.setText("Please select a level folder.");
                }
            }
        });
    }

    private void copyLevel() {
        // Copy the files from the selected level folder to the game folder
        File gameFolder = new File("LoadedLevel");
        if (!gameFolder.exists()) {
            gameFolder.mkdir();
        }

        File[] filesToCopy = selectedFolder.listFiles((dir, name) -> name.endsWith(".txt"));
        for (File file : filesToCopy) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                FileOutputStream outputStream = new FileOutputStream(new File(gameFolder, file.getName()));

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Close the level selector and start the game
        dispose();
        StartGame();
    }

    private void StartGame() {
        // The method to start the game goes here
        // ...
        MyFrame frame = new MyFrame();
    }


}
