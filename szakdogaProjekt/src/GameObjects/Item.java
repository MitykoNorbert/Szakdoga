package GameObjects;

public class Item {
    private String name,restores;
    private int weight,restoreValue;
    private boolean consumable;
    public Item(String name, String restores, int weight, int restoreValue, boolean consumable) {
        this.name = name;
        this.restores = restores;
        this.weight = weight;
        this.restoreValue = restoreValue;
        this.consumable = consumable;
    }
}
