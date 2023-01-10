package GameObjects;

public class NeedValue {
    private int value;
    private int maxValue;
    private int punishmentValue;
    private String punishmentStat;
    private int decreaseRate;

    public NeedValue(int value, int maxValue, int punishmentValue, String punishmentStat, int decreaseRate) {
        this.value = value;
        this.maxValue = maxValue;
        this.punishmentValue = punishmentValue;
        this.punishmentStat = punishmentStat;
        this.decreaseRate = decreaseRate;
    }
    public NeedValue(NeedValue copy) {
        this.value = copy.getValue();
        this.maxValue = copy.getMaxValue();
        this.punishmentValue = copy.getPunishmentValue();
        this.punishmentStat = copy.getPunishmentStat();
        this.decreaseRate = copy.getDecreaseRate();
    }

    public NeedValue() {
        this.value = 0;
        this.maxValue = 0;
        this.punishmentValue = 0;
        this.punishmentStat = "unknown";
        this.decreaseRate = 0;
    }

    public int getValue() {
        return value;
    }
    public void Decrease(){
        if (decreaseRate>0&&value>0){
            if(value-decreaseRate<0){
                value=0;
            }else{
                value-=decreaseRate;
            }
        }
    }
    public void DecreaseBy(int n){
        if (n>0&&value>0){
            if(value-n<0){
                value=0;
            }else{
                value-=n;
            }
        }
    }
    public void IncreaseBy(int n){
        value+=n;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getPunishmentValue() {
        return punishmentValue;
    }

    public void setPunishmentValue(int punishmentValue) {
        this.punishmentValue = punishmentValue;
    }

    public String getPunishmentStat() {
        return punishmentStat;
    }

    public void setPunishmentStat(String punishmentStat) {
        this.punishmentStat = punishmentStat;
    }

    public int getDecreaseRate() {
        return decreaseRate;
    }

    public void setDecreaseRate(int decreaseRate) {
        this.decreaseRate = decreaseRate;
    }

    public boolean belowPercent(float percent){
        if(this.value<this.maxValue*percent){
            return true;
        }
        return false;
    }
}
