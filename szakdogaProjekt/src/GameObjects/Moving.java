package GameObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class Moving extends Process {
    private final int row;
    private final int col;
    private int cRow;
    private int cCol;
    GameMap map;
    private HashMap<Tile, Integer> beenThere;

    public Moving(Character character, int timeRequired, String name, int row, int col, String reason) {
        super(character, timeRequired, name, reason);
        this.row = row;
        this.col = col;
        this.map = getCharacter().map;
        this.beenThere = new HashMap<Tile, Integer>();
        cRow = getCharacter().getRowPos();
        cCol = getCharacter().getColPos();

    }

    @Override
    public void progressTick() {
        super.progressTick();
        incrementBeenThere();
        cRow = getCharacter().getRowPos();
        cCol = getCharacter().getColPos();
        Action();
        if (row == getCharacter().getRowPos() && col == getCharacter().getColPos()) {
            setTimeRequired(0);
            setCompleted(true);
           //System.out.println("completed: "+isCompleted());
        } else {

        }

    }

    @Override
    protected void Action() {
        super.Action();
        Tile step = chooseStep();
        if (step != null) {
            getCharacter().MoveTo(step.getRow(), step.getCol());
        }


    }

    public void incrementBeenThere() {
        if (beenThere.get(map.getTile(cRow, cCol)) == null) {
            beenThere.put(map.getTile(cRow, cCol), 0);
        } else {
            beenThere.put(map.getTile(cRow, cCol), beenThere.get(map.getTile(cRow, cCol)) + 1);
        }

    }

    public ArrayList<Tile> getNeighbors() {
        ArrayList<Tile> neighbors = new ArrayList<Tile>();

        if (map.IsWalkable(cRow - 1, cCol)) {
            neighbors.add(map.getTile(cRow - 1, cCol));
        }
        if (map.IsWalkable(cRow + 1, cCol)) {
            neighbors.add(map.getTile(cRow + 1, cCol));
        }
        if (map.IsWalkable(cRow, cCol - 1)) {
            neighbors.add(map.getTile(cRow, cCol - 1));
        }
        if (map.IsWalkable(cRow, cCol + 1)) {
            neighbors.add(map.getTile(cRow, cCol + 1));
        }
        return neighbors;
    }

    public Tile chooseStep() {
        //chooses optimal step from available neighbors
        HashMap<Tile, Integer> stepValues = new HashMap<Tile, Integer>();
        for (int i = 0; i < getNeighbors().size(); i++) {
            if (Math.abs(getNeighbors().get(i).getRow() - row) < Math.abs(cRow - row)) {
                stepValues.put(getNeighbors().get(i), -1);
            } else if (Math.abs(getNeighbors().get(i).getRow() - row) == Math.abs(cRow - row)) {
                stepValues.put(getNeighbors().get(i), 0);
            } else {
                stepValues.put(getNeighbors().get(i), 1);
            }
            if (Math.abs(getNeighbors().get(i).getCol() - col) < Math.abs(cCol - col)) {
                stepValues.put(getNeighbors().get(i), stepValues.get(getNeighbors().get(i)) - 1);
            } else if (Math.abs(getNeighbors().get(i).getCol() - col) > Math.abs(cCol - col)) {
                stepValues.put(getNeighbors().get(i), stepValues.get(getNeighbors().get(i)) + 1);
            }
           //System.out.println(getNeighbors().get(i).getRow() + ", " + getNeighbors().get(i).getCol() + "=" + stepValues.get(getNeighbors().get(i)));
        }
        Tile best = getNeighbors().get(0);
        for (int i = 0; i < getNeighbors().size(); i++) {
            if (!beenThere.containsKey(best)) {
                beenThere.put(best, 0);
            }
            if(!beenThere.containsKey(getNeighbors().get(i))){
                beenThere.put(getNeighbors().get(i),0);
            }
            if (stepValues.get(best) + beenThere.get(best) > stepValues.get(getNeighbors().get(i)) + beenThere.get(getNeighbors().get(i))){
                best = getNeighbors().get(i);
            }
        }
        return best;
    }

}
