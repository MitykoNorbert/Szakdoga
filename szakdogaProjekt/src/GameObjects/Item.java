package GameObjects;

public class Item {
    private String name,restores;
    private int weight,restoreValue,consumeTime;
    private boolean consumable;

    public Item(String name, String restores, int weight, int restoreValue, boolean consumable, int consumeTime) {
        this.name = name;
        this.restores = restores;
        this.weight = weight;
        this.restoreValue = restoreValue;
        this.consumable = consumable;
        this.consumeTime =consumeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestores() {
        return restores;
    }

    public void setRestores(String restores) {
        this.restores = restores;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRestoreValue() {
        return restoreValue;
    }

    public void setRestoreValue(int restoreValue) {
        this.restoreValue = restoreValue;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public int getConsumeTime() {
        return consumeTime;
    }
    public Item Copy(){
        return new Item(name,restores,weight,restoreValue,consumable,consumeTime);
    }
}
