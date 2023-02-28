package GameObjects;

public class Process {
    private int timeRequired;
    private boolean completed;
    private Character character;
    private String name;
    private String reason;

    public Process(Character character, int timeRequired, String name, String reason) {
        this.timeRequired = timeRequired;
        this.completed = false;
        this.character = character;
        this.name = name;
        this.reason = reason;
    }

    public void progressTick(){

        timeRequired--;
        if (timeRequired<=0){
            completed=true;
        }
    }
    protected void Action(){

    }

    public int getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
