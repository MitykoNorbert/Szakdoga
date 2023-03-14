package GameObjects;

public class ConsumptionProcess extends Process{
    private Item item;
    public ConsumptionProcess(Character character, int timeRequired, String name, String reason, Item item) {
        super(character, timeRequired, name, reason);
        this.item = item;
    }

    @Override
    public void progressTick() {
        super.progressTick();
        if(isCompleted()){
            getCharacter().getNeeds().get(item.getRestores()).IncreaseBy(item.getRestoreValue());
        }

    }
}
