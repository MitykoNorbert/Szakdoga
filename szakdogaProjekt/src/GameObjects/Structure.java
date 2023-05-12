package GameObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class Structure extends GameObject {
    private int width;
    private int height;
    private GameMap map;
    private int storageCapacity;
    private int currentStorage;
    private HashMap<String, NeedValue> needs;
    private HashMap<String, Integer> provides;
    private HashMap<String, Integer> storage;
    private HashMap<String, Item> itemstats;
    private boolean isHome;
    private byte interactCapacity;
    private ArrayList<Character> inInteractionWith;
    private String name;
    private int ageTick;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Structure(int rowPos, int colPos, int width, int height, int storageCapacity, boolean isHome, byte interactCapacity, GameMap map, HashMap<String,Item> items) {
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
        this.itemstats=items;
        this.ageTick=0;
        this.currentStorage=0;
        addProvidedItems();

    }
    public void placed(){
        for (int i = this.getRowPos(); i < this.getRowPos()+height; i++) {
            for (int j = this.getColPos(); j < this.getColPos()+width; j++) {
                //System.out.println("Row: "+i+", Coloumn:"+j);
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
        if(currentStorage+amount*itemstats.get(item).getWeight() <=storageCapacity){
            if(storage.containsKey(item)){
            storage.put(item,storage.get(item)+amount);
        }else{
            storage.put(item,amount);

        }
            currentStorage += itemstats.get(item).getWeight()*amount;
        }


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

    public void GameTick(){
        ageTick++;
        for (String key:provides.keySet()) {
            if(itemstats.containsKey(key)){
                int value=provides.get(key);
                if(ageTick*value/900>=900-value && ageTick*value/900<900+value){
                    addToStorage(key,1);
                }
            }

        }

    }
    private void addProvidedItems(){
        for (String key:provides.keySet()) {
            if(itemstats.containsKey(key)){
                if(!provides.containsKey(itemstats.get(key).getRestores())){
                    provides.put(itemstats.get(key).getRestores(),0);
                }
            }
        }
    }
}
