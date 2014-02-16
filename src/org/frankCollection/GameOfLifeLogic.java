package org.frankCollection;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.imageio.ImageIO;

public class GameOfLifeLogic {
	
	final int maxGeneration = 10000;
	
	private int currentGeneration;
	
	private boolean moreGeneration = true;
	
	private boolean[][] currentGenerationArr;
	private boolean[][] nextGenerationArr;	
	
	private double threshold = 1.1;	// percentage of average value in image
	
	
	public GameOfLifeLogic(BufferedImage original){		
		setCurrentGenerationImg(original);	
	}
	
	public GameOfLifeLogic(){		
	}
	
	
	
	public BufferedImage getNextGenerationImg(){
		
		if (currentGenerationArr.length > 0  && currentGeneration < maxGeneration){
			
			if (currentGeneration == maxGeneration - 1)
				moreGeneration = false;
			
			evalNextGeneration();
			return convertArrToImg(nextGenerationArr); 
		}		
		
		
		if (currentGeneration >= maxGeneration - 1)
			moreGeneration = false;
		
		return null;	
	}
	
	public BufferedImage getCurrentGenerationImg(){
		return convertArrToImg(currentGenerationArr);
	}
	
	public void saveBufferedImageToFile(File file){
		
		try {
			ImageIO.write(convertArrToImg(nextGenerationArr), "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BufferedImage convertArrToImg(boolean[][] arr) {
		BufferedImage grayFrame = new BufferedImage(arr[0].length, arr.length,
				BufferedImage.TYPE_BYTE_GRAY);


		WritableRaster raster = grayFrame.getRaster();
		
		for (int x = 0; x < raster.getWidth(); x++){
			for (int y = 0; y < raster.getHeight(); y++){
				
				int l = arr[y][x]? 255 : 0;
				
				raster.setSample(x, y, 0, l);
			}
		}
		
		return grayFrame;
	}
	
	
	public boolean isMoreGeneration() {
		return moreGeneration;
	}

	public void setMoreGeneration(boolean moreGeneration) {
		this.moreGeneration = moreGeneration;
	}

	public void setThreshold(double threshold){
		this.threshold = threshold;
	}
	
	public double getThreshold(){
		return threshold;
	}
	
	public void setCurrentGenerationImg(BufferedImage original) {
		currentGeneration = 0;
		currentGenerationArr = rgbToArray(original, threshold);		
		
		nextGenerationArr = new boolean[original.getHeight()][original.getWidth()];
	}

	private void evalNextGeneration(){		
		
		int col = currentGenerationArr[0].length;
		int row = currentGenerationArr.length;
		
		for (int x = 0; x < row; x++){
			for (int y = 0; y < col; y++){
				
				nextGenerationArr[x][y] = getStatus(x,y);
				
			}
		}
		
		copyNextGeneration();
		
		currentGeneration++;
	}
	
	
	private void copyNextGeneration() {
		
		int col = currentGenerationArr[0].length;
		int row = currentGenerationArr.length;
		
		for (int x = 0; x < row; x++){
			for (int y = 0; y < col; y++){
				
				currentGenerationArr[x][y] = nextGenerationArr[x][y];
				
			}
		}
		
	}

	private boolean getStatus(int x, int y) {
		
		int liveNeighbor = 0;

		int col = currentGenerationArr[0].length - 1;
		int row = currentGenerationArr.length - 1;
		
		int r,c;
		
		for (int i = x-1; i <= x+1; i++){
			for (int j = y-1; j <= y+1; j++){
				
				if (i < 0) {
					r = row;
				}else if (i > row) {
					r = 0;
				}else {
					r = i;
				}				
				
				if (j < 0) {
					c = col;
				}else if (j > col) {
					c = 0;
				}else {
					c = j;
				}		
				
				liveNeighbor += (currentGenerationArr[r][c]? 1 : 0);
			}
		}
		
		if (currentGenerationArr[x][y]){
			if (liveNeighbor == 3 || liveNeighbor == 4)
				return true;
		} else {
			if (liveNeighbor == 3)
				return true;
		}
		
		return false;
		
	}

	private int getAvgIntensity(BufferedImage image){
		int sum = 0;
		int argb;
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				argb = image.getRGB(x, y);
				
				sum += (argb >> 16) & 0xff;
				sum += (argb >> 8) & 0xff;
				sum += argb & 0xff;				
			}
		}
		
		return sum / (3 * image.getWidth() * image.getHeight());
	}


	private boolean[][] rgbToArray(BufferedImage image, double threshold){
		
		int r,g,b,argb;
		
		threshold = threshold * getAvgIntensity(image);
		
		boolean[][] arr = new boolean[image.getHeight()][image.getWidth()];
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				argb = image.getRGB(x, y);
				
				r = (argb >> 16) & 0xff;
				g = (argb >> 8) & 0xff;
				b = argb & 0xff;
				
				arr[y][x] = ((r + g + b) > 3 * threshold);
			}
		}
		
		return arr;
	}
	
	public BufferedImage convertToGray(BufferedImage org){
		
		int r,g,b,l,argb;
		
		BufferedImage grayFrame = new BufferedImage(org.getWidth(), org.getHeight(),
									BufferedImage.TYPE_BYTE_GRAY);
		
		
		WritableRaster raster = grayFrame.getRaster();
		
		for (int x = 0; x < raster.getWidth(); x++){
			for (int y = 0; y < raster.getHeight(); y++){
				argb = org.getRGB(x, y);
				
				r = (argb << 16) & 0xff;
				g = (argb << 8) & 0xff;
				b = argb & 0xff;
				
				l = (int) (.333 * r + .5 * g + .1666 * b);
				
				raster.setSample(x, y, 0, l);
			}
		}
		
		return grayFrame;
	}
}
