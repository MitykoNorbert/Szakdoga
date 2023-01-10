package GameObjects;

public class Tile {
    private int row;
    private int col;
    private String ground;
    private boolean walkable;
    private Structure occupiedBy;

    public Tile(int row, int col, String ground, boolean walkable) {
        this.row = row;
        this.col = col;
        this.ground = ground;
        this.walkable = walkable;
        this.occupiedBy = null;
    }

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.ground="def";
        this.walkable=true;
    }

    public Structure getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Structure occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
}
