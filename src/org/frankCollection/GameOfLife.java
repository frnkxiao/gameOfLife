package org.frankCollection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class GameOfLife extends JPanel 
						implements ActionListener{

	/**
	 * @param args
	 */
	
	private JFileChooser fc;	
	private JButton openButton, saveButton, playButton, pauseButton;	
	private BufferedImage lastGenImage;	
	private ImagePanel imagePanel;	
	private GameOfLifeLogic logic = new GameOfLifeLogic();
	
	public GameOfLife(){
		
		super(new BorderLayout());
		
		if (new File("default.gif").exists()){
			imagePanel = new ImagePanel("default.gif");
		} else {
			imagePanel = new ImagePanel();
		}
		
		imagePanel.repaint();
		
		if (imagePanel.getBufferedImage() != null) {
			logic.setCurrentGenerationImg(imagePanel.getBufferedImage());
		
			imagePanel.setBufferedImage(logic.getCurrentGenerationImg());
		}
		
		// create a file chooser
		fc = new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		openButton = new JButton("Open image...");
		openButton.addActionListener(this);
		
		saveButton = new JButton("Save image...");
		saveButton.addActionListener(this);
		
		playButton = new JButton("Play");
		playButton.addActionListener(this);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);				
		buttonPanel.add(playButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(saveButton);
		
		// add components to panel
		add(buttonPanel, BorderLayout.PAGE_START);
		add(imagePanel, BorderLayout.CENTER);		
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		//Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(GameOfLife.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                imagePanel.setBufferedImageFile(file.getAbsolutePath());
                imagePanel.repaint();   
                
                logic.setCurrentGenerationImg(imagePanel.getBufferedImage());
                
                imagePanel.setBufferedImage(logic.getCurrentGenerationImg());
                imagePanel.repaint();  
            }
            

        //Handle save button action.
        } else if (e.getSource() == saveButton) {
        	if (imagePanel.getBufferedImage() == null) {
			return;
		}
		
            int returnVal = fc.showSaveDialog(GameOfLife.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                logic.saveBufferedImageToFile(file);
                
            } else {
                
            }

        } else if (e.getSource() == playButton) {
        	
        	if (imagePanel.getBufferedImage() == null) {
			return;
		}
        	
        	Thread calNext = new Thread(){
        		public void run(){
        			
        			logic.setMoreGeneration(true);
        			
        			while (logic.isMoreGeneration()){
        				lastGenImage = logic.getNextGenerationImg();
        				
        				if (lastGenImage != null){
        	    			imagePanel.setBufferedImage(lastGenImage);        			
        	    		}
        	            
        	            imagePanel.repaint();
        	            
        				
        				try {
    						Thread.sleep(1000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        			}


        		}
        		
        	};        	
        	
        	calNext.start();        	
        	
        } else if (e.getSource() == pauseButton) {
        	
        	if (imagePanel.getBufferedImage() == null) {
			return;
		}
		
        	logic.setMoreGeneration(false);
        	
        }
	}
	
	
	private static void createAndShowGUI(){
		// create and show game of life window
		JFrame frame = new JFrame("Game of Life");		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add content to window
		frame.add(new GameOfLife());
		
		// display window
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				createAndShowGUI();
			}
			
		});

	}

}
