package GameObjects;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Character extends GameObject {
    private int rotation;
    private HashMap<String, NeedValue> needs;
    private HashMap<String, Structure> refillSources;
    private HashSet<Item> inventory;
    ArrayList<Structure> homes;
    GameMap map;
    private Character interactingWithCharacter;
    private Structure interactingWithStructure;
    private ArrayList<Process> currentTasks;
    private ArrayList<Item> itemsInUse;
    private String name;
    private final int carryMax = 5000;
    private int carryWeight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character(int posX, int posY, GameMap map) {
        super(posX, posY);
        this.rotation = 0;
        this.map = map;
        this.carryWeight = 0;
        this.needs = new HashMap<String, NeedValue>();
        needs.put("Health", new NeedValue(100, 100, 0,
                "unknown", 0));
        currentTasks = new ArrayList<Process>();
        itemsInUse = new ArrayList<Item>();
        inventory = new HashSet<>();
    }

    @Override
    public void Update() {
        NextMove();
        StatCheck();


    }


    public void StatCheck() {
        for (String key : needs.keySet()) {
            needs.get(key).Decrease();
            if (needs.get(key).getValue() < needs.get(key).getMaxValue() / 10) {
                if (needs.containsKey(needs.get(key).getPunishmentStat())) {
                    int dRate = needs.get(key).getPunishmentValue();
                    needs.get(needs.get(key).getPunishmentStat()).DecreaseBy(dRate);
                }
            }
        }
    }

    public void NextMove() {
        ItemChecks();
        if (currentTasks.isEmpty()) {
            fetchNewTask();
        } else {
            continueTask();
        }
        if (!currentTasks.isEmpty()) {
            //System.out.println("Current: " + currentTasks.get(0));
        }

        InteractionHandling();

    }

    public void ItemChecks() {
        String needed = getLowestPercentageNeed();
        Item chosenItem = null;
        for (Item item : inventory) {
            if (item.getRestores().equals(needed)) {
                if (needs.get(needed).getMaxValue() >= needs.get(needed).getValue() + item.getRestoreValue()) {
                    chosenItem = item;
                    break;
                }
            }
        }
        if (chosenItem != null) {
            useItem(chosenItem);
        }

    }

    public void InteractionHandling() {
        if (interactingWithStructure != null) {
            if (map.getTile(this.getRowPos(), this.getColPos()).getOccupiedBy() != null) {
                if (currentTasks.get(0) instanceof InteractingProcess) {
                    InteractingProcess interaction = ((InteractingProcess) currentTasks.get(0));
                    String key = interaction.getReason();
                    if (interactingWithStructure.getProvides().containsKey(key)) {
                        int amount = interactingWithStructure.getIncreaseAmountFor(key);
                        needs.get(key).IncreaseBy(amount);
                    }
                }

            } else {
                interactingWithStructure = null;
            }
        } else {
            interactingWithStructure = map.getTile(this.getRowPos(), this.getColPos()).getOccupiedBy();
        }

    }

    private String InteractStructure(String reason) {
        if (interactingWithStructure != null) {
            return "Already interacting";
        }
        if (!currentTasks.isEmpty()) {
            return "Already have a task";
        }
        //for(String key : needs.keySet()){}
        //System.out.println(getRowPos() + ", " + getColPos() + " is occupied by " + map.getTile(getRowPos(), getColPos()).getOccupiedBy());
        if (map.getTile(getRowPos(), getColPos()).getOccupiedBy() == null) {
            return "There's no structure where i just arrived";
        }

        if (map.getTile(getRowPos(), getColPos()).getOccupiedBy().getProvides().containsKey(getLowestPercentageNeed())) {
            InteractingProcess newTask = new InteractingProcess(this, 15, "Regaining " + getLowestPercentageNeed(), map.getTile(getRowPos(), getColPos()).getOccupiedBy(), getLowestPercentageNeed());
            currentTasks.add(0, newTask);
            //System.out.println("Found source for " + getLowestPercentageNeed());
        }


        return "Successfully started interaction";
    }

    public void MoveTo(int row, int col) {
        if (row >= 0 && row < map.getRowSize()) {
            setRowPos(row);
        }
        if (col >= 0 && col < map.getColSize()) {
            setColPos(col);
        }

    }

    public void TurnRandom() {
        this.rotation = (int) (Math.random() * 4);
    }

    public void StepForward() {
        int newRow = this.getRowPos();
        int newCol = this.getColPos();
        switch (rotation) {
            case 0:
                if (newRow - 1 >= 0 && newRow - 1 < map.getRowSize() && map.getTile(newRow - 1, newCol).isWalkable()) {
                    newRow--;
                } else TurnRandom();
                break;
            case 1:
                if (newCol + 1 >= 0 && newCol + 1 < map.getRowSize() && map.getTile(newRow, newCol + 1).isWalkable()) {
                    newCol++;
                } else TurnRandom();
                break;
            case 2:
                if (newRow + 1 >= 0 && newRow + 1 < map.getRowSize() && map.getTile(newRow + 1, newCol).isWalkable()) {
                    newRow++;
                } else TurnRandom();
                break;
            case 3:
                if (newCol - 1 >= 0 && newCol - 1 < map.getRowSize() && map.getTile(newRow, newCol - 1).isWalkable()) {
                    newCol--;
                } else TurnRandom();
                break;
        }
        this.setColPos(newCol);
        this.setRowPos(newRow);

    }

    @Override
    public HashMap<String, NeedValue> getNeeds() {
        return needs;
    }

    public boolean stepUp() {
        if (getRowPos() == 0) {
            return false;
        }
        if (map.getTile(getRowPos() - 1, getColPos()).isWalkable()) {
            setRowPos(getRowPos() - 1);
            return true;
        }
        return false;
    }

    public boolean stepDown() {
        if (getRowPos() == map.getRowSize() - 1) {
            return false;
        }
        if (map.getTile(getRowPos() + 1, getColPos()).isWalkable()) {
            setRowPos(getRowPos() + 1);
            return true;
        }
        return false;
    }

    public boolean stepLeft() {
        if (getColPos() == 0) {
            return false;
        }
        if (map.getTile(getColPos() - 1, getColPos()).isWalkable()) {
            setRowPos(getColPos() - 1);
            return true;
        }
        return false;
    }

    public boolean stepRight() {
        if (getColPos() == map.getColSize() - 1) {
            return false;
        }
        if (map.getTile(getColPos() + 1, getColPos()).isWalkable()) {
            setRowPos(getColPos() + 1);
            return true;
        }
        return false;
    }

    public ArrayList<Structure> findSourcesFor(String need) {
        ArrayList<Structure> sources = new ArrayList<Structure>();
        ArrayList<Structure> structures = map.getStructures();
        int all = 0;
        for (int i = 0; i < structures.size(); i++) {
            if (structures.get(i).getProvides().containsKey(need)) {
                sources.add(structures.get(i));
            }

            all++;
        }
        //System.out.println("Amount of Sources for " + need + ":" + sources.size() + ", all:" + all);
        return sources;
    }

    public Tile findOptimalSourceFor(ArrayList<Structure> structures, String need) {
        int index = (int) (Math.random() * (structures.size() - 1));
        return map.getTile(structures.get(index).getRowPos(), structures.get(index).getColPos());
    }

    public Structure findOptimalSourceFor(String need) {

        ArrayList<Structure> structures = findSourcesFor(need);
        Structure closestStructure = null;
        int minimumDistance = map.getColSize() + map.getRowSize();

        for (Structure structure : structures) {
            int distance = DistanceFromMe(structure);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                closestStructure = structure;
            }
        }
        return closestStructure;
    }

    public String getLowestPercentageNeed() {
        float min = 1;
        String minName = null;
        for (String key : needs.keySet()) {
            if (needs.get(key).getPercentage() < min) {
                min = needs.get(key).getPercentage();
                minName = key;
            }
        }
        return minName;
    }

    public ArrayList<String> getPriorityList() {
        //todo
        ArrayList<String> priorityList = new ArrayList<String>();
        return null;
    }

    public Process getCurrentTask() {
        if (currentTasks.isEmpty()) {
            return null;
        }
        return currentTasks.get(0);
    }

    public int DistanceFromMe(GameObject target) {
        int row = target.getRowPos();
        int col = target.getColPos();
        return Math.abs(getRowPos() - row) + Math.abs(getColPos() - col);
    }

    public void fetchNewTask() {
        if (getLowestPercentageNeed() != null) {
            Structure closestStructure = findOptimalSourceFor(getLowestPercentageNeed());
            Tile sourceTile = map.getTile(0, 0);
            if (closestStructure != null) {
                sourceTile = findOptimalSourceFor(getLowestPercentageNeed()).getEntrance();
            }
            String reason = getLowestPercentageNeed();
            MovingProcess newTask = new MovingProcess(this, 200,
                    "Going to get " + reason, sourceTile.getRow(), sourceTile.getCol(), reason);
            currentTasks.add(0, newTask);
            currentTasks.get(0).progressTick();

        }
    }

    public void continueTask() {
        currentTasks.get(0).progressTick();
        if (currentTasks.get(0).isCompleted()) {
            boolean justarrived = false;
            if (currentTasks.get(0) instanceof MovingProcess) {
                justarrived = true;
            }
            String reason = currentTasks.get(0).getReason();
            currentTasks.remove(0);
            interactingWithStructure = null;
            //System.out.println("MEGVAN KÉSZ");
            if (justarrived) {
                InteractStructure(reason);

            }

        }

    }

    public void useItem(Item item) {
        if (item.isConsumable()) {
            currentTasks.add(new ConsumptionProcess(this, item.getConsumeTime(), "Consuming " + item.getName(), item.getRestores(), item));
            inventory.remove(item);
            //System.out.println("I ate the " + item.getName());
        } else {
            if (!itemsInUse.contains(item)) {
                itemsInUse.add(item);
            }

        }
    }

    public boolean addToInventory(Item item) {
        if (carryMax >= carryWeight + item.getWeight()) {
            inventory.add(item);
            carryWeight += item.getWeight();
            return true;
        }
        return false;
    }
}
