package com.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.debug.Log;
import com.gui.ErrorMessageGUI;
import com.image.ImageFilter;
import com.image.ImageReaderWriter;

public class FileChooser {
	private final JFileChooser fileChooser = new JFileChooser();
	
	public FileChooser() {
	}
	
	public FileChooser(ImageFilter filter) {
		fileChooser.setFileFilter(filter);
	}
	
	public final void setImageFilter(ImageFilter filter) {
		fileChooser.setFileFilter(filter);
	}
	
	public final File chooseImageFile() {
		File f = null;
		
		fileChooser.setMultiSelectionEnabled(false);
		int returnVal = fileChooser.showOpenDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fileChooser.getSelectedFile();
			Log.print("Main image file chosen: " + f.getName());
		} else {
			Log.print("No file was chosen");
		} 
		
		return f;
	}
	
	public final File[] chooseImageFiles() {
		File[] fs = new File[0];
		
		fileChooser.setMultiSelectionEnabled(true);
		int returnVal = fileChooser.showOpenDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fs = fileChooser.getSelectedFiles();
			for (File f: fs) {
				Log.print("Component image file chosen: " + f.getName()); 
			}
		} else {
			Log.print("No files were chosen");
		} 
		
		return fs;
	}
	
	public final void saveImage(BufferedImage image) {
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(null);
		fileChooser.setSelectedFile(null);
		
		int returnVal = fileChooser.showSaveDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				Log.print("Saving image...");
				File file = fileChooser.getSelectedFile();
				ImageReaderWriter.writeImage(image, file.getAbsolutePath(), "png");
				Log.print("Successfully saved image " + file.getName() + ".");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ErrorMessageGUI errGUI = new ErrorMessageGUI();
				errGUI.setErrorMessage("Error: Couldn't save image.");
				Log.print(e.getMessage());
			}
		} else {
			Log.print("Cancelled saving image. Image was not saved.");
		}
		
		
	}
}
