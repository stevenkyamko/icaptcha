/**
 * 
 */
package org.smx.captcha.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;

import org.smx.captcha.IBackgroundProducer;

public class BoxedbackgroundProducer implements IBackgroundProducer{
	 private Properties props;
	 public BoxedbackgroundProducer(){
		 props=new Properties();
	 }
	 public BufferedImage addBackground(BufferedImage image) {
		 Graphics2D g2=image.createGraphics();
		 int bacgroundRGB=Integer.parseInt(props.getProperty("background","E3F1FD"), 16);
		 int maxBoxes=Integer.parseInt(props.getProperty("maxboxes","4"));
		 int minBoxes=Integer.parseInt(props.getProperty("minboxes","2"));
		 boolean canIntersect=Boolean.parseBoolean( props.getProperty("intersect","false") );
		 boolean border=Boolean.parseBoolean( props.getProperty("border-visible","true") );
		 int borderColor=Integer.parseInt ( props.getProperty("border-color","000000"),16);
		 
		 
		 int width=image.getWidth();
		 int height=image.getHeight();
	   
		 int randNumberOfBoxes=0;
		 do{
			 randNumberOfBoxes =  new Random().nextInt(maxBoxes);	 
		 }while(randNumberOfBoxes<minBoxes );
			 
		 g2.setColor(new Color(bacgroundRGB));
	     g2.fillRect(0,0, width,height);
	     
	     Rectangle[] rectangler=new Rectangle[randNumberOfBoxes];
	     for(int i=0;i<randNumberOfBoxes;i++)rectangler[i]=new Rectangle();
	 	 
	     Rectangle rect;
	     int index=0;
	     while(randNumberOfBoxes > 0){		    
	    	int x,y,w,h;
	    	x=0;
	    	y=0;
	    	w=0;
	    	h=0;
	    	int iter=0;
	    	boolean intersect=false;
	    	while(true && iter<100){		    
		    	x=new Random().nextInt(width);
	    		y=new Random().nextInt(height);
	    		while(h<=0)
	    			h=new Random().nextInt(height  - y);
	    		while(w<=0)
	    			w=new Random().nextInt(width - x);
	    		
	    		rect=new Rectangle(x,y,w,h);
	    	
	    		if(!canIntersect){//can't intersect
		    		for(int i=0;i<index;i++){
		    			intersect=rect.intersects( rectangler[i] );
		    			if(intersect){break;}
		     		}
	    		}else{
	    			intersect=false;
	    		}
	    		
	 			if(!intersect){
    				rectangler[index]=rect;
    				break;
    			}
	    	}
    		
	    	g2.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
		    g2.fillRect(x, y, w, h);
		    
		    if(border){
		    	g2.setColor(new Color(borderColor));
			    g2.drawRect(x+1, y+1, w-1, h-1);			    
		    }
		    randNumberOfBoxes--;
		    index++;
	     }
	     
		return image;
	}

	public void setProperties(Properties props) {
		this.props=props;			
	}
	
}