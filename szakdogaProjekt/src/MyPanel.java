import GameObjects.GameMap;
import GameObjects.Structure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MyPanel extends JPanel {
    private int rowsize =30;
    private int colsize =30;
    private int max;
    private int tilesize;
    private int size=600;
    private GameMap map;
    Graphics2D g2D;
    private HashMap<String, BufferedImage> textures;

    MyPanel(){
        this.setPreferredSize(new Dimension(size,size));

        max= colsize;
        if(rowsize > colsize){
            max= rowsize;
        }
        tilesize=size/max;
        map=new GameMap(rowsize, colsize);
        map.spawnObjects();
        textures=texturesLoader();
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


        for (int i = 0; i < rowsize; i++) {
            for (int j = 0; j < colsize; j++) {

                if (!map.getTile(i, j).isWalkable()) {
                    g2D.drawImage(textures.get("water"), j*tilesize, i*tilesize, tilesize, tilesize, null);
                } else {
                    switch (map.getTile(i,j).getGround()){
                        case "1":
                            g2D.drawImage(textures.get("dirt"), j*tilesize, i*tilesize, tilesize, tilesize, null);
                            break;
                        default:
                            g2D.drawImage(textures.get("grass"), j*tilesize, i*tilesize, tilesize, tilesize, null);
                    }


                }

            }
        }

    }
    public HashMap<String, BufferedImage> texturesLoader(){
        HashMap<String, BufferedImage> textures = new HashMap<>();
        try {
            BufferedImage blueTexture = ImageIO.read(new File("textures/water.png"));
            textures.put("water",blueTexture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedImage greenTexture = ImageIO.read(new File("textures/grass.png"));
            textures.put("grass",greenTexture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedImage texture = ImageIO.read(new File("textures/bricks.png"));
            textures.put("bricks",texture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedImage texture = ImageIO.read(new File("textures/character.png"));
            textures.put("character",texture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedImage texture = ImageIO.read(new File("textures/dirt.png"));
            textures.put("dirt",texture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return textures;
    }
    public void renderObjects(){
        //System.out.println(map.getObjects().size());
        for (int i = 0; i < map.getObjects().size(); i++) {

            int row=map.getObjects().get(i).getRowPos();
            int col=map.getObjects().get(i).getColPos();
            g2D.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            if(map.getObjects().get(i) instanceof Structure){
                Structure structure = (Structure) map.getObjects().get(i);
                int width= ((Structure) map.getObjects().get(i)).getWidth();
                int height= ((Structure) map.getObjects().get(i)).getHeight();
                g2D.setPaint(new Color(130, 92, 40));
                for (int j = 0; j < structure.getHeight(); j++) {
                    for (int k = 0; k < structure.getWidth(); k++) {
                        g2D.drawImage(textures.get("bricks"), col*tilesize+k*tilesize, row*tilesize+j*tilesize, tilesize, tilesize, null);
                    }
                }
                g2D.fillRect(col*tilesize+tilesize/4, row*tilesize+tilesize/4,width*tilesize-tilesize/2,height*tilesize-tilesize/2);
            }else{
                g2D.setPaint(Color.red);
                //g2D.fillRect(col*tilesize+tilesize/4, row*tilesize+tilesize/4,tilesize/2,tilesize/2);
                g2D.drawImage(textures.get("character"),col*tilesize, row*tilesize, tilesize,tilesize, null);


            }


        }
    }
    public void renderMap(){
        repaint();
    }
}
