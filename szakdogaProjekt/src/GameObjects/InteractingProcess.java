package GameObjects;

public class InteractingProcess extends Process {
    private final GameObject partner;


    public InteractingProcess(Character character,int timeRequired, String name, GameObject partner, String forWhat) {
        super(character, timeRequired, name, forWhat);
        this.partner = partner;
    }

    @Override
    protected void Action(){

    }

    public GameObject getPartner() {
        return partner;
    }

}
