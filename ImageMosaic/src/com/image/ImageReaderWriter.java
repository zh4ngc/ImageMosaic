package com.image;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReaderWriter {
	private ImageReaderWriter() {
	}
	
	public static final void writeImage(RenderedImage img, String outputFilename, String outputFormat) throws IOException {
		File outputFile = new File(outputFilename + "." + outputFormat);
		ImageIO.write(img, outputFormat, outputFile);
	}
	
	public static final void readImage() {
		
	}
}
