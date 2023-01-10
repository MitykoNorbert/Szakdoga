package GameObjects;

import GameObjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Structure extends GameObject {
    private int width;
    private int height;
    private GameMap map;
    private int storageCapacity;
    private HashMap<String, NeedValue> needs;
    private HashMap<String, Integer> provides;
    private HashMap<String, Integer> storage;
    private boolean isHome;
    private byte interactCapacity;
    private ArrayList<Character> inInteractionWith;


    public Structure(int rowPos, int colPos, int width, int height, int storageCapacity, boolean isHome, byte interactCapacity,GameMap map) {
        super(rowPos, colPos);
        this.width = width;
        this.height = height;
        this.storageCapacity = storageCapacity;
        this.isHome = isHome;
        this.interactCapacity = interactCapacity;
        needs = new HashMap<String, NeedValue>();
        provides = new HashMap<String, Integer>();
        storage = new HashMap<String, Integer>();
        this.map=map;
        for (int i = rowPos; i < rowPos+height; i++) {
            for (int j = colPos; j < colPos+width; j++) {
                System.out.println("Row: "+i+", Coloumn:"+j);
                map.getTile(i,j).setOccupiedBy(this);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public HashMap<String, NeedValue> getNeeds() {
        return needs;
    }

    public HashMap<String, Integer> getProvides() {
        return provides;
    }
    public void shallProvide(String name, int amount){
        provides.put(name,amount);
    }

    public HashMap<String, Integer> getStorage() {
        return storage;
    }

    @Override
    public void Interact(Character character){
        //TODO
    }
    public void storageAccess(String intention){

    }
    public void addToStorage(String item, int amount){

    }
    public void takeFromStorage(String item, int amount){

    }
    public int checkStorageFor(String itemName){
        if(storage.containsKey(itemName)){
            return storage.get(itemName);
        }else {
            return 0;
        }
    }
}
