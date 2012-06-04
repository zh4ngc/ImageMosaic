package com.main;

import java.awt.image.BufferedImage;
import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.debug.Log;
import com.gui.LogGUI;
import com.image.ImageProcessor;

public class ImageMosaic {
	private final ImageProcessor imgProc = new ImageProcessor();
	
	private File mainImageFile;
	private File[] componentImageFiles;
	
	private BufferedImage mosaicImage;

	public ImageMosaic() {
		mainImageFile = null;
		componentImageFiles = null;
		
		mosaicImage = null;
	}
	
	public File getMainImageFile() {
		return mainImageFile;
	}

	public void setMainImageFile(File mainImageFile) {
		this.mainImageFile = mainImageFile;
	}

	public File[] getComponentImageFiles() {
		return componentImageFiles;
	}

	public void setComponentImageFiles(File[] componentImageFiles) {
		this.componentImageFiles = componentImageFiles;
	}
	
	public ImageProcessor getImageProcessor() {
		return imgProc;
	}
	
	public void setMosaicImage(BufferedImage image) {
		mosaicImage = image;
	}
	
	public BufferedImage getMosaicImage() {
		return mosaicImage;
	}
	
	public final void createAndShowGUI() {
		final Display display = new Display();
		Log.print("Created new display");
		
		LogGUI lGUI = new LogGUI(this);
		Log.setLogGUI(lGUI);
		lGUI.open();
		Log.disposeLogGUI();
		
		if (!display.isDisposed()) {
			display.dispose();
			Log.print("Disposed Display.");
		}
	}
	
	public static void main(String args[]) {
		ImageMosaic im = new ImageMosaic();
		im.createAndShowGUI();
	}
	
}