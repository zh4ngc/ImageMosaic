package com.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.debug.Log;
import com.utils.CircularIntDoubleArrayIterator;

public class ImageProcessor {
	private static final int IMAGE_COMPARISON_THRESHOLD = 15;

	private BufferedImage mainImg;
	private BufferedImage[] componentImgs;

	public ImageProcessor() {
		mainImg = null;
		componentImgs = null;
	}

	public final void setMainImage(BufferedImage mainImage) {
		mainImg = mainImage;
	}
	
	public final void setMainImage(File mainImageFile) throws IOException {
		Log.print("Reading main image file...");
		setMainImage(ImageIO.read(mainImageFile));
		Log.print("Finished reading main image file.");
	}
	
	public final BufferedImage getMainImage() {
		return mainImg;
	}

	public final void setComponentImages(BufferedImage[] componentImages) {
		componentImgs = componentImages;
	}
	
	public final void setComponentImages(File[] componentImageFiles) throws IOException {
		Log.print("Reading and resizing component image files...");
		setComponentImages(filesToResizedBufferedImages(componentImageFiles));
		Log.print("Finished reading and resizing files.");
	}

	public final boolean isReady() {
		if (mainImg == null || componentImgs == null
				|| componentImgs.length == 0) {
			return false;
		}
		return true;
	}
	
	private final BufferedImage[] filesToResizedBufferedImages(File[] componentImageFiles) throws IOException {
		final int numComponentPics = componentImageFiles.length;
		BufferedImage[] componentImages = new BufferedImage[numComponentPics];
		
		for(int i = 0; i < numComponentPics; i++) {
			// Turn the component image files into BufferedImages
			Log.print("Reading Image File " + (i + 1) + "/" + numComponentPics + ": " + componentImageFiles[i].getName() + "...");
			BufferedImage componentImg = ImageIO.read(componentImageFiles[i]);
			Log.print("Finished reading image.");
			Log.print("Resizing image...");
			// Store the shrunken image
			componentImages[i] = ImageManipulator.shrinkImage(componentImg, 100, 100);
			Log.print("Finished resizing image.");
		}
			
		return componentImages;
	}

	public BufferedImage process(int numXComponents, int numYComponents)
			throws IllegalArgumentException {
		if (!isReady()) {
			throw new IllegalArgumentException(
					"Need to select a main image and component images.");
		}
		Log.print("Beginning the mosaic building process...");
		Log.print("Creating a mosaic image with " + numXComponents
				+ " X " + numYComponents + " number of components.");
		
		int numComponenetPics = componentImgs.length;

		int[][] componentImgsAvgVals = new int[numComponenetPics][];

		// Get the average RGB values for each of the component images
		for (int i = 0; i < numComponenetPics; i++) {
			Log.print("Calculating RGB Values for Image " + (i + 1) + "/" + numComponenetPics + ".");
			componentImgsAvgVals[i] = ImageManipulator
					.getAverageImgRGBVal(componentImgs[i]);
		}

		// Break up the main image into subcomponents
		int componentWidth = componentImgs[0].getWidth();
		int componentHeight = componentImgs[0].getHeight();

		BufferedImage[][] mainImgComponents = ImageManipulator.splitImage(
				mainImg, numXComponents, numYComponents);

		// Swap the main image subcomponents with that of the component images
		int numMainImgComponentsX = mainImgComponents.length;
		int numMainImgComponentsY = mainImgComponents[0].length;

		CircularIntDoubleArrayIterator itr = new CircularIntDoubleArrayIterator(
				componentImgsAvgVals);
		int lastSavedItrPos = 0;
		for (int x = 0; x < numMainImgComponentsX; x++) {
			for (int y = 0; y < numMainImgComponentsY; y++) {
				Log.print("Finding image for subcomponent (" + x + ", " + y + ").");

				int[] mainImageComponentRGBVals = ImageManipulator
						.getAverageImgRGBVal(mainImgComponents[x][y]);

				int cycle = 1;

				int redDif = 999;
				int greenDif = 999;
				int blueDif = 999;

				while ((cycle < 4)
						&& ((redDif > IMAGE_COMPARISON_THRESHOLD * cycle)
								|| (greenDif > IMAGE_COMPARISON_THRESHOLD
										* cycle) || (blueDif > IMAGE_COMPARISON_THRESHOLD
								* cycle))) {
					int[] componentImgRGBVals = itr.nextIntArray();

					redDif = Math.abs(mainImageComponentRGBVals[0]
							- componentImgRGBVals[0]);
					greenDif = Math.abs(mainImageComponentRGBVals[1]
							- componentImgRGBVals[1]);
					blueDif = Math.abs(mainImageComponentRGBVals[2]
							- componentImgRGBVals[2]);
					if (itr.getNextValidCursorPos() == lastSavedItrPos) {
						cycle++;
					}
				}

				mainImgComponents[x][y] = componentImgs[itr
						.getCurrentCursorPos()];
				lastSavedItrPos = itr.getRandomCursorPos();
			}
		}

		// Rebuild the main image using the component images
		BufferedImage mosaicImage = new BufferedImage(numMainImgComponentsX
				* componentWidth, numMainImgComponentsY * componentHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D mosaicImageGraphics = mosaicImage.createGraphics();

		Log.print("Rebuilding image...");
		
		for (int x = 0; x < numMainImgComponentsX; x++) {
			for (int y = 0; y < numMainImgComponentsY; y++) {
				mosaicImageGraphics.drawImage(mainImgComponents[x][y], x
						* componentWidth, y * componentHeight, null);
			}
		}

		mosaicImageGraphics.dispose();
		
		Log.print("Finished building image.");
		// Return the new mosaic
		return mosaicImage;
	}
}
