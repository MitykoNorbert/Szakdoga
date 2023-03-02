package GameObjects;

import GameObjects.Character;
import GameObjects.GameObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameMap {
    private Tile[][] tileMap;
    private ArrayList<GameObject> objects;
    private int rowSize;
    private int colSize;
    private HashMap<String, NeedValue> availableNeeds;

    public Tile[][] getTileMap() {
        return tileMap;
    }

    public void setTileMap(Tile[][] tileMap) {
        this.tileMap = tileMap;
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
    }

    public Tile getTile(int row, int col) {
        if(row>rowSize || col > colSize || row<0 || col<0){
            return null;
        }
        return tileMap[row][col];
    }

    public boolean IsWalkable(int row, int col) {
        //array out of bounds, walkable visszaadása
        if (row < 0 || col < 0 || row >= rowSize || col >= colSize){
            return false;
        }else{
            return getTile(row,col).isWalkable();
        }
    }

    public GameMap(int height, int width) {
        this.rowSize = height;
        this.colSize = width;
        this.objects = new ArrayList<GameObject>();
        this.tileMap = new Tile[height][width];
        this.availableNeeds = new HashMap<String, NeedValue>();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                this.tileMap[i][j] = new Tile(i, j);
                //egyesével kell inicializálni mert máskülönben null
                if (i * j % 100 >= 20 && i * j % 100 < 25) {
                    this.tileMap[i][j].setWalkable(false);
                }
            }
        }

    }

    public void spawnObjects() {
        importCharacters();
        //Structure testhouse = new Structure(15, 16, 7, 5, 0, false, (byte) -1, this);
        //testhouse.shallProvide("Health",4);
        //testhouse.shallProvide("Energy", 3);
        //if provides amount, it should need an interaction for example: "Regaining energy" process,/ if its 0, it should mean there is something in its storage that provides said thing
        //objects.add(testhouse);
        //testhouse.placed();
        importStructures();

    }
    public ArrayList<Structure> getStructures(){
        ArrayList<Structure> structures = new ArrayList<Structure>();
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i) instanceof Structure){
                structures.add((Structure) objects.get(i));
            }
        }
        return structures;
    }

    public void UpdateObjects() {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).Update();
            if (objects.get(i).getNeeds().containsKey("Health")) {
                if (objects.get(i).getNeeds().get("Health").getValue() == 0) {
                    objects.remove(i);
                }
            }

        }
    }

    public void importGameMap() {
        //TODO
    }

    public void importCharacters() {
        //TODO
        System.out.println("Chars importing..");

        try {
            File needslistFile = new File("LoadedLevel/Needs.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] stats = line.split(",");
                String name = stats[0];
                int value = Integer.parseInt(stats[1]);
                int maxValue = Integer.parseInt(stats[2]);
                int punishValue = Integer.parseInt(stats[3]);
                String punishStat = stats[4];
                int decRate = Integer.parseInt(stats[5]);

                //System.out.println("Need name: "+stats[0]);
                //System.out.println("Starting value: "+stats[1]);
                //System.out.println("Max Value: "+stats[2]);
                //System.out.println("Punishment value: "+stats[3]);
                //System.out.println("Punishment on: "+stats[4]);
                //System.out.println("Decrease rate: "+stats[5]);
                //System.out.println(line);
                availableNeeds.put(name, new NeedValue(value, maxValue, punishValue, punishStat, decRate));
                System.out.println("NEW NEEED: " + name + ": " + availableNeeds.get(name).getValue());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading Needs.txt.");
            e.printStackTrace();
        }
        try {
            File needslistFile = new File("LoadedLevel/Characters.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] charStats = line.split(",");
                int row = Integer.parseInt(charStats[0]);
                int col = Integer.parseInt(charStats[1]);
                Character inputChar = new Character(row, col, this);
                for (int i = 2; i < charStats.length; i++) {
                    if (availableNeeds.containsKey(charStats[i])) {
                        NeedValue personalNeed = new NeedValue(availableNeeds.get(charStats[i]));
                        inputChar.getNeeds().put(charStats[i], personalNeed);
                    }
                }
                objects.add(inputChar);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading Characters.txt.");
            e.printStackTrace();
        }
        System.out.println("chars imported");
    }
    public void importStructures() {
        //TODO
        System.out.println("Structures importing..");
        try {
            File needslistFile = new File("LoadedLevel/Structures.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] strucStats = line.split(",");
                int row = Integer.parseInt(strucStats[0]);
                int col = Integer.parseInt(strucStats[1]);
                int width = Integer.parseInt(strucStats[2]);
                int height = Integer.parseInt(strucStats[3]);
                int storageCapacity = Integer.parseInt(strucStats[4]);
                boolean isHome = false;
                if(strucStats[5].equals("home") || strucStats[5].equals("true")){
                    isHome = true;
                }
                byte interactCapacity = Byte.parseByte(strucStats[6]);
                line = myReader.nextLine();
                strucStats = line.split(",");
                Structure inputStruc = new Structure(row, col, width, height, storageCapacity, isHome, interactCapacity,this);
                for (int i = 0; i < strucStats.length; i++) {
                    if (availableNeeds.containsKey(strucStats[i])) {
                        NeedValue personalNeed = new NeedValue(availableNeeds.get(strucStats[i]));
                        inputStruc.getNeeds().put(strucStats[i], personalNeed);
                    }
                }
                line = myReader.nextLine();
                strucStats = line.split(",");
                for (int i = 0; i < strucStats.length; i++) {
                    if (availableNeeds.containsKey(strucStats[i])) {
                        String provision = strucStats[i];
                        inputStruc.getProvides().put(provision,1);
                    }
                }
                objects.add(inputStruc);
                inputStruc.placed();

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading Structures.txt.");
            e.printStackTrace();
        }
        System.out.println("chars imported");
    }
    public void addStructure(int row, int col, int width, int height, int storageCapcaity, boolean isHome, byte interactCapacity){
        Structure structure = new Structure(row,col,width,height,storageCapcaity,isHome,interactCapacity,this);
        objects.add(structure);
        structure.placed();
    }
    public void addStructure(Structure structure){
        objects.add(structure);
        structure.placed();
    }

    public HashMap<String, NeedValue> getAvailableNeeds() {
        return availableNeeds;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public Character charAtPosition(int row, int col){
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i) instanceof Character && objects.get(i).getRowPos()==row && objects.get(i).getColPos()==col){
                return (Character) objects.get(i);
            }
        }
        return null;
    }

}
