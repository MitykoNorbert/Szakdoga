import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelSelector extends JFrame {
    private List<String> levelNames;
    private JList<String> levelList;
    private JButton startButton;
    private JLabel statusLabel;
    private JPanel panel;

    public LevelSelector() {
        setTitle("Level Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        add(panel);

        // Load the list of level names
        levelNames = loadLevelNames();

        // Create the level list GUI
        levelList = new JList<>(levelNames.toArray(new String[levelNames.size()]));
        levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane levelListScrollPane = new JScrollPane(levelList);
        panel.add(levelListScrollPane, BorderLayout.CENTER);

        // Create the start button
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLevelName = levelList.getSelectedValue();
                if (selectedLevelName == null) {
                    StartGame();
                } else {
                    copyLevel(selectedLevelName);
                }
            }
        });
        panel.add(startButton, BorderLayout.SOUTH);

        // Create the status label
        statusLabel = new JLabel("Please select a level.");
        panel.add(statusLabel, BorderLayout.NORTH);
    }

    private List<String> loadLevelNames() {
        List<String> names = new ArrayList<>();
        try {
            Path levelsDir = Paths.get("Levels");
            if (Files.isDirectory(levelsDir)) {
                Files.list(levelsDir)
                        .filter(Files::isDirectory)
                        .forEach(levelDir -> names.add(levelDir.getFileName().toString()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return names;
    }

    private void copyLevel(String levelName) {
        // Copy the files from the selected level folder to the game folder
        Path levelDir = Paths.get("Levels", levelName);
        Path gameDir = Paths.get("LoadedLevel");
        if (!gameDir.toFile().exists()) {
            gameDir.toFile().mkdir();
        }

        File[] filesToCopy = levelDir.toFile().listFiles((dir, name) -> name.endsWith(".txt"));
        for (File file : filesToCopy) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                FileOutputStream outputStream = new FileOutputStream(new File(gameDir.toFile(), file.getName()));

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

    public static void main(String[] args) {
        LevelSelector levelSelector = new LevelSelector();
        levelSelector.setVisible(true);
    }
}
