package GameObjects;

public class InteractingProcess extends Process {
    private final GameObject partner;
    private final String forwhat;

    public InteractingProcess(Character character,int timeRequired, String name, GameObject partner, String forwhat) {
        super(character, timeRequired, name);
        this.partner = partner;
        this.forwhat = forwhat;
    }
    @Override
    public void progressTick() {

    }
    @Override
    protected void Action(){

    }
}
