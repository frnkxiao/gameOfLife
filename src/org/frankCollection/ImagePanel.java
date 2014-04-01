package org.frankCollection;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;	
	
	// constructors
	public ImagePanel(){
		setPreferredSize(getDisplaySize());
	}
	
	public ImagePanel(String filename){
		image = getBufferedImageFile(filename);
		
		setPreferredSize(getPrefferedSize());
	}
	
	public ImagePanel(BufferedImage image){
		this.image = image;
		
		setPreferredSize(getPrefferedSize());
	}
	
	
	// methods
	
	public void setBufferedImageFile(String filename){
		image = getBufferedImageFile(filename);
		
		setPreferredSize(getPrefferedSize());
	}
	
	public void setBufferedImage(BufferedImage image){
		this.image = image;
		
		setPreferredSize(getPrefferedSize());
	}
	
	public BufferedImage getBufferedImage(){
		return image;
	}
	
	
	private BufferedImage getBufferedImageFile(String filename){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Invalid file name or file type.");
		}
		
		return img;
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		Dimension prefferedSize = getPrefferedSize();
		
		if (getBufferedImage() == null) {
			g.setColor(Color.BLACK);
			g.fillRect(0,0,(int) prefferedSize.getWidth(), (int) prefferedSize.getHeight());
			g.setColor(Color.WHITE);
			g.drawString("Please select a image to continue ...", 60, 90);
		} else {
			g.drawImage(image, 0, 0, (int) prefferedSize.getWidth(), (int) prefferedSize.getHeight(), null);
		}
	}
	
	private Dimension getPrefferedSize(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		int imageWidth;
		int imageHeight;
		if (getBufferedImage() == null) {
			imageWidth = 500;
			imageHeight = 200;			
		} else {
			imageWidth = getBufferedImage().getWidth();
			imageHeight = getBufferedImage().getHeight();
		}
		
		int minWidth = (screenWidth > imageWidth)? imageWidth : screenWidth;
		int minHeight = (screenHeight > imageHeight)? imageHeight : screenHeight;
				
		// width corresponding minHeight while keeping image W/H ratio
		int rWidth = minHeight * imageWidth / imageHeight;
		if (minWidth > rWidth){
			return new Dimension(rWidth, minHeight);
		}else{
			return new Dimension(minWidth, minWidth * imageHeight / imageWidth);
		}		
	}
	
}
