package GameObjects;


import java.util.ArrayList;
import java.util.HashMap;

public class Character extends GameObject {
    int asd=0; //to be removed
    private int rotation;
    private HashMap<String,NeedValue> needs;
    private HashMap<String,Structure> refillSources;
    private HashMap<String,Integer> inventory;
    ArrayList<Structure> homes;
    GameMap map;
    private Character interactingWithCharacter;
    private Structure interactingWithStructure;
    private Process currentTask;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character(int posX, int posY, GameMap map) {
        super(posX, posY);
        this.rotation=0;
        this.map = map;
        this.needs= new HashMap<String,NeedValue>();
        needs.put("Health",new NeedValue(100,100,0,"unknown",0));
    }

    @Override
    public void Update() {
        NextMove();
        StatCheck();


    }


    public void StatCheck(){
        System.out.println("Start of npc");
        for(String key : needs.keySet()){
            needs.get(key).Decrease();
            if (needs.get(key).getValue()<needs.get(key).getMaxValue()/10){
                if(needs.containsKey(needs.get(key).getPunishmentStat())){
                    int dRate=needs.get(key).getPunishmentValue();
                    //System.out.println(key+"-s punishment is: "+needs.get(key).getPunishmentStat());
                    needs.get(needs.get(key).getPunishmentStat()).DecreaseBy(dRate);
                }

            }
            System.out.println("My "+key+" is: "+needs.get(key).getValue());
        }
        System.out.println("End op npc");
    }

    public void NextMove(){


            if(currentTask==null){
                if(asd==0){
                    currentTask=new MovingProcess(this,200,"Walking",10,27);
                    currentTask.progressTick();
                }
                if(getLowestNeed()!=null){
                    if(needs.get(getLowestNeed()).belowPercent(0.4F)){
                        System.out.println(getLowestNeed()+"is below 40%!!");
                        Tile sourceTile = findOptimalSourceFor(getLowestNeed());
                        currentTask = new MovingProcess(this,200,"Going to get "+getLowestNeed(),sourceTile.getRow() ,sourceTile.getCol());
                        currentTask.progressTick();
                    }
                }




            }else{
                currentTask.progressTick();
                if(currentTask.isCompleted()){
                    boolean justarrived = false;
                    if(currentTask instanceof MovingProcess){
                        justarrived = true;
                    }
                    asd++;
                    currentTask=null;
                    System.out.println("MEGVAN KÉSZ");
                    if (justarrived){
                        InteractStructure();
                    }

                }
                if(currentTask instanceof MovingProcess){

                }
            }

        System.out.println("Current: "+currentTask);
        /*
        StepForward();
        InteractionHandling();
        if(Math.random()>0.9){
            TurnRandom();
        }
        */
        InteractionHandling();

    }
    public void InteractionHandling(){
        if (interactingWithStructure!=null){
            if (map.getTile(this.getRowPos(),this.getColPos()).getOccupiedBy()!=null){
                    for(String key : needs.keySet()){
                        if(needs.get(key).getValue()+5>=needs.get(key).getMaxValue()){
                            needs.get(key).setValue(needs.get(key).getMaxValue());
                        }
                        needs.get(key).IncreaseBy(5);
                        System.out.println(key+" was increased");
                    }
            }else{
                interactingWithStructure=null;
            }
        }else{
            interactingWithStructure=map.getTile(this.getRowPos(),this.getColPos()).getOccupiedBy();
            System.out.println("not interacting");
        }

    }
    private String InteractStructure(){
        if (interactingWithStructure!=null){
           return "Already interacting";
        }
        if(currentTask!=null){
            return "Already have a task";
        }
        //for(String key : needs.keySet()){}
        System.out.println(getRowPos()+", "+getColPos()+" is occupied by "+map.getTile(getRowPos(),getColPos()).getOccupiedBy());
        if(map.getTile(getRowPos(),getColPos()).getOccupiedBy()==null){
            return "There's no structure where i just arrived";
        }
        if (map.getTile(getRowPos(),getColPos()).getOccupiedBy().getProvides().containsKey(getLowestNeed())){
                currentTask = new InteractingProcess(this, 15, "Regaining "+getLowestNeed(),map.getTile(getRowPos(),getColPos()).getOccupiedBy(),getLowestNeed());
            System.out.println("Found source for "+getLowestNeed());
        }



        return "Successfully started interaction";
    }
    public void MoveTo(int row, int col){
        if(row >= 0 && row < map.getRowSize()){
            setRowPos(row);
        }
        if(col >= 0 && col < map.getColSize()){
            setColPos(col);
        }

    }
    public void TurnRandom(){
        this.rotation=(int)(Math.random()*4);
    }
    public void StepForward(){
        int newRow=this.getRowPos();
        int newCol=this.getColPos();
        switch (rotation){
            case 0:
                if(newRow-1>=0 && newRow-1<map.getRowSize() && map.getTile(newRow-1,newCol).isWalkable()){
                    newRow--;
                }else TurnRandom();
            break;
            case 1:
                if(newCol+1>=0 && newCol+1<map.getRowSize() && map.getTile(newRow,newCol+1).isWalkable()){
                    newCol++;
                }else TurnRandom();
                break;
            case 2:
                if(newRow+1>=0 && newRow+1<map.getRowSize() && map.getTile(newRow+1,newCol).isWalkable()){
                    newRow++;
                }else TurnRandom();
                break;
            case 3:
                if(newCol-1>=0 && newCol-1<map.getRowSize() && map.getTile(newRow,newCol-1).isWalkable()){
                    newCol--;
                }else TurnRandom();
                break;
        }
        this.setColPos(newCol);
        this.setRowPos(newRow);

    }
    @Override
    public HashMap<String, NeedValue> getNeeds() {
        return needs;
    }

    public boolean stepUp(){
        if(getRowPos()==0){
            return false;
        }
        if(map.getTile(getRowPos()-1,getColPos()).isWalkable()){
                setRowPos(getRowPos()-1);
                return true;
            }
        return false;
    }
    public boolean stepDown(){
        if(getRowPos()==map.getRowSize()-1){
            return false;
        }
        if(map.getTile(getRowPos()+1,getColPos()).isWalkable()){
            setRowPos(getRowPos()+1);
            return true;
        }
        return false;
    }
    public boolean stepLeft(){
        if(getColPos()==0){
            return false;
        }
        if(map.getTile(getColPos()-1,getColPos()).isWalkable()){
            setRowPos(getColPos()-1);
            return true;
        }
        return false;
    }
    public boolean stepRight(){
        if(getColPos()==map.getColSize()-1){
            return false;
        }
        if(map.getTile(getColPos()+1,getColPos()).isWalkable()){
            setRowPos(getColPos()+1);
            return true;
        }
        return false;
    }

    public ArrayList<Structure> findSourcesFor(String need){
        ArrayList<Structure> sources = new ArrayList<Structure>();
        ArrayList<Structure> structures = map.getStructures();
        for (int i = 0; i < structures.size(); i++) {
            structures.get(i).getProvides().containsKey(need);
            sources.add(structures.get(i));
        }
        return sources;
    }

    public Tile findOptimalSourceFor(ArrayList<Structure> structures,String need){
        int index =  (int) (Math.random() * (structures.size()-1));
        return map.getTile(structures.get(index).getRowPos(),structures.get(index).getColPos());
    }

    public Tile findOptimalSourceFor(String need){
        ArrayList<Structure> structures = findSourcesFor(need);
        int index =  (int) (Math.random() * (structures.size()));
        System.out.println("row:"+structures.get(index).getRowPos()+"col:"+structures.get(index).getColPos());
        return map.getTile(structures.get(index).getRowPos(),structures.get(index).getColPos());
    }

    public String getLowestNeed(){
        float min=1;
        String minName = null;
        for(String key : needs.keySet()){
            if(needs.get(key).getPercentage()<min){
                min=needs.get(key).getPercentage();
                minName=key;
            }
        }
        return minName;
    }
    public ArrayList<String> getPriorityList(){
        //todo
        ArrayList<String> priorityList = new ArrayList<String>();
        return null;
    }



}
