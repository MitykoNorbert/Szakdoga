package GameObjects;

import java.util.HashMap;

public class GameObject {
    private int rowPos;
    private int colPos;

    public GameObject(int rowPos, int colPos) {
        this.rowPos = rowPos;
        this.colPos = colPos;
    }

    public void Interact(Character character){

    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }
    public void Update(){

    }
    public HashMap<String, NeedValue> getNeeds() {
        return null;
    }
}
