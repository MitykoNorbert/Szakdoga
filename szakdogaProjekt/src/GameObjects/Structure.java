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
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Structure(int rowPos, int colPos, int width, int height, int storageCapacity, boolean isHome, byte interactCapacity, GameMap map) {
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

    }
    public void placed(){
        for (int i = this.getRowPos(); i < this.getRowPos()+height; i++) {
            for (int j = this.getColPos(); j < this.getColPos()+width; j++) {
                System.out.println("Row: "+i+", Coloumn:"+j);
                if(i<map.getRowSize() && j <map.getColSize()){
                    map.getTile(i,j).setOccupiedBy(this);
                }

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

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public boolean isHome() {
        return isHome;
    }

    public byte getInteractCapacity() {
        return interactCapacity;
    }

    public ArrayList<Character> getInInteractionWith() {
        return inInteractionWith;
    }
    public Tile getEntrance(){
        //int horizontalTile=(int) (Math.random()*width)+getColPos();
        //int verticalTile=(int) (Math.random()*height)+getRowPos();
        //return map.getTile(horizontalTile,verticalTile);
        return map.getTile(getRowPos(),getColPos());
    }
    public int getIncreaseAmountFor(String needName){
        return 5;
    }
}
