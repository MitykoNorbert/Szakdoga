package GameObjects;

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
    private int deaths;
    private HashMap<String, NeedValue> availableNeeds;
    private HashMap<String,Item> availableItems;
    private HashMap<String, Integer> completeRequirements;
    private int completitionTimer;
    private String gameState;

    public HashMap<String, Item> getAvailableItems() {
        return availableItems;
    }

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
        if (row > rowSize || col > colSize || row < 0 || col < 0) {
            return null;
        }
        return tileMap[row][col];
    }

    public boolean IsWalkable(int row, int col) {
        //array out of bounds, walkable visszaadása
        if (row < 0 || col < 0 || row >= rowSize || col >= colSize) {
            return false;
        } else {
            return getTile(row, col).isWalkable();
        }
    }

    public GameMap(int height, int width) {
        this.rowSize = height;
        this.colSize = width;
        this.objects = new ArrayList<GameObject>();
        this.tileMap = new Tile[height][width];
        this.availableNeeds = new HashMap<String, NeedValue>();
        this.availableItems = new HashMap<String,Item>();
        this.completeRequirements = new HashMap<String,Integer>();
        this.completitionTimer=0;
        this.deaths=0;
        this.gameState="In progress";
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                this.tileMap[i][j] = new Tile(i, j);
                //egyesével kell inicializálni mert máskülönben null
                if (i * j % 100 >= 20 && i * j % 100 < 25) {
                    this.tileMap[i][j].setWalkable(false);
                }
            }
        }
        tileMap=importGameMap();
        loadRequirements();

    }
    public void loadRequirements(){
        try {
            File file = new File("LoadedLevel/CompleteRequirements.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] itemStats = line.split(":");
                String name = itemStats[0];
                int amount = Integer.parseInt(itemStats[1]);
                this.completeRequirements.put(name,amount);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the CompleteRequirements.txt");
            e.printStackTrace();
        }
    }
    public boolean requirementCheck(){
        boolean timerCompleted=true;
        for (String key:completeRequirements.keySet()) {
            if(key.equals("MaximumDeaths")){
                if(completeRequirements.get(key)<deaths){
                    levelFailed();
                    return false;
                }
            }else if(key.equals("MaintainTime")){
                //System.out.println("found timer");
                //System.out.println(completeRequirements.get(key)+" req vs timer:"+completitionTimer);
                if(completeRequirements.get(key)>completitionTimer){
                    //System.out.println(completeRequirements.get(key)+" is less than timer:"+completitionTimer);
                    timerCompleted=false;
                }
            }else if(getAverageLevelOf(key)<completeRequirements.get(key)){
                completitionTimer=0;
                //System.out.println("avg "+key+" is smaller than "+completeRequirements.get(key));
                return false;
            }

        }
        completitionTimer++;
        //System.out.println("COmpletition timer: "+completitionTimer);
        //System.out.println("Timer completed: "+timerCompleted);

        return timerCompleted;
    }
    public void levelFailed(){
        gameState="Failed";
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
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
        importItems();
        Character almaman=null;
        for (int i = 0; i < getCharacters().size(); i++) {
            if(getCharacters().get(i).getNeeds().containsKey("Food")){
                almaman=getCharacters().get(i);
                break;
            }
        }
        almaman.addToInventory(availableItems.get("Apple").Copy());

    }

    public ArrayList<Structure> getStructures() {
        ArrayList<Structure> structures = new ArrayList<Structure>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Structure) {
                structures.add((Structure) objects.get(i));
            }
        }
        return structures;
    }
    public ArrayList<Character> getCharacters() {
        ArrayList<Character> characters = new ArrayList<Character>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Character) {
                characters.add((Character) objects.get(i));
            }
        }
        return characters;
    }


    public void UpdateObjects() {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).Update();
            if (objects.get(i).getNeeds().containsKey("Health")) {
                if (objects.get(i).getNeeds().get("Health").getValue() == 0) {
                    if(objects.get(i) instanceof Character){
                        deaths++;
                    }
                    objects.remove(i);

                }
            }

        }
    }

    public Tile[][]  importGameMap() {
        Tile[][] map = null;
        File f = new File("LoadedLevel/GameMap.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(f);
            String line = myReader.nextLine();
            String[] size = line.split(",");
            int mapWidth = Integer.parseInt(size[0]);
            int mapHeight = Integer.parseInt(size[1]);
            map = new Tile[mapHeight][mapWidth];
            // Read each subsequent line
            for (int i = 0; i < mapHeight; i++) {
                line = myReader.nextLine();
                String[] values = line.split(",");
                for (int j = 0; j < mapWidth; j++) {
                    //map[i][j] = Integer.parseInt(values[j]);
                    map[i][j] = new Tile(i,j,values[j],true);
                    if(Integer.parseInt(values[j])>5){
                        map[i][j].setWalkable(false);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }





        //System.out.println("map imported");
        return map;
    }

    public void importCharacters() {
        //System.out.println("Chars importing..");

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
                //System.out.println("NEW NEEED: " + name + ": " + availableNeeds.get(name).getValue());
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
       //System.out.println("chars imported");
    }

    public void importStructures() {

        //System.out.println("Structures importing..");
        try {
            File needslistFile = new File("LoadedLevel/Structures.txt");
            Scanner myReader = new Scanner(needslistFile);
            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                if(line.equals("-")){
                    line = myReader.nextLine();
                }
                String[] strucStats = line.split(",");
                int row = Integer.parseInt(strucStats[0]);
                int col = Integer.parseInt(strucStats[1]);
                int width = Integer.parseInt(strucStats[2]);
                int height = Integer.parseInt(strucStats[3]);
                int storageCapacity = Integer.parseInt(strucStats[4]);
                boolean isHome = false;
                if (strucStats[5].equals("home") || strucStats[5].equals("true")) {
                    isHome = true;
                }
                byte interactCapacity = Byte.parseByte(strucStats[6]);
                line = myReader.nextLine();
                strucStats = line.split(",");
                Structure inputStruc = new Structure(row, col, width, height, storageCapacity, isHome, interactCapacity, this, availableItems);
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
                        inputStruc.getProvides().put(provision, 1);
                    }
                }
                line= myReader.nextLine();
                if(!line.equals("-")){
                    strucStats = line.split(",") ;
                    for (int i = 0; i < strucStats.length; i+=2) {
                        String item = strucStats[i];
                        int value =Integer.parseInt(strucStats[i+1]);
                        inputStruc.getProvides().put(item,value);
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
        //System.out.println("chars imported");
    }

    public void addStructure(int row, int col, int width, int height, int storageCapcaity, boolean isHome, byte interactCapacity) {
        Structure structure = new Structure(row, col, width, height, storageCapcaity, isHome, interactCapacity, this, availableItems);
        objects.add(structure);
        structure.placed();
    }

    public void addStructure(Structure structure) {
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

    public Character charAtPosition(int row, int col) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Character && objects.get(i).getRowPos() == row && objects.get(i).getColPos() == col) {
                return (Character) objects.get(i);
            }
        }
        return null;
    }

    public int getAverageLevelOf(String need) {
        int total = 0;
        int examined = 0;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getNeeds().containsKey(need)) {
                total += objects.get(i).getNeeds().get(need).getPercentage()*100;
                examined++;
            }
        }
        if(examined==0){
            return 0;
        }
        return total / examined;

    }

    public HashMap<String,Integer> GetAverageStats() {
        HashMap<String, Integer> averages = new HashMap<String, Integer>();
        for (String key : availableNeeds.keySet()) {
            averages.put(key, getAverageLevelOf(key)*100);
        }
        return averages;
    }

    public int getDeaths() {
        return deaths;
    }
    
    public void importItems(){
        try {
            File itemlistFile = new File("LoadedLevel/Items.txt");
            Scanner myReader = new Scanner(itemlistFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] itemStats = line.split(",");
                String name = itemStats[0];
                String restores = itemStats[1];
                int amount = Integer.parseInt(itemStats[2]);
                int weight = Integer.parseInt(itemStats[3]);
                boolean consumable =false;
                if(itemStats[4].equalsIgnoreCase("consumable")){
                    consumable=true;
                }
                int time=0;
                if(itemStats.length>5){
                    time = Integer.parseInt(itemStats[5]);
                }
                Item newItem = new Item(name,restores,weight,amount,consumable,time);
                availableItems.put(name,newItem);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the Items.txt");
            e.printStackTrace();
        }
    }

}

