import GameObjects.GameMap;
import GameObjects.Structure;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    private int x=30;
    private int y=30;
    private int max;
    private int tilesize;
    private int size=500;
    private GameMap map;
    Graphics2D g2D;

    MyPanel(){
        this.setPreferredSize(new Dimension(size,size));

        max=y;
        if(x>y){
            max=x;
        }
        tilesize=size/max;
        map=new GameMap(x,y);
        int max=y;
        if (x>max){
            max=x;
        }
        map.spawnObjects();
    }
    public int getTilesize(){
        return tilesize;
    }
    public int getDisplaySize() {
        return size;
    }

    public void paint(Graphics g){
        
        g2D = (Graphics2D) g;
        //g2D.drawLine(0,0,500,500);
        tilesize=size/max;
        renderGround();
        renderObjects();
        //System.out.println("a paint has happened");
    }

    public GameMap getMap() {
        return map;
    }

    public void renderGround(){

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {

                if(!map.getTile(i,j).isWalkable()){
                    g2D.setPaint(Color.blue);
                }else{
                    g2D.setPaint(Color.green);
                }
                if(map.getTile(i,j).getOccupiedBy()!=null){
                    //g2D.setPaint(Color.orange);
                }
                g2D.fillRect(j*tilesize,i*tilesize,tilesize,tilesize);

            }
        }

    }
    public void renderObjects(){
        //System.out.println(map.getObjects().size());
        for (int i = 0; i < map.getObjects().size(); i++) {

            int row=map.getObjects().get(i).getRowPos();
            int col=map.getObjects().get(i).getColPos();
            g2D.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            if(map.getObjects().get(i) instanceof Structure){
                int width= ((Structure) map.getObjects().get(i)).getWidth();
                int height= ((Structure) map.getObjects().get(i)).getHeight();
                g2D.setPaint(Color.DARK_GRAY);
                g2D.fillRect(col*tilesize+tilesize/4, row*tilesize+tilesize/4,width*tilesize-tilesize/2,height*tilesize-tilesize/2);
            }else{
                g2D.setPaint(Color.red);
                g2D.fillRect(col*tilesize+tilesize/4, row*tilesize+tilesize/4,tilesize/2,tilesize/2);
            }


        }
    }
    public void renderMap(){
        repaint();
    }
}
