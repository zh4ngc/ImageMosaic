package com.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;


import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class ImageManipulator {
	private static final int NUM_COLORS = 3;
	private static final int NUM_RGB_VAL = 26;
	private static final int PIXEL_SAMPLE_SIZE = 500;

	private ImageManipulator() {

	}

	/**
	 * Extracts the red component of a pixel.
	 * 
	 * @param pixel
	 *            an integer pixel
	 * 
	 * @return the red component [0-255] of the pixel.
	 */
	public static int getRed(int pixel) {
		return pixel >> 16 & 0xff;
	}

	/**
	 * Extracts the green component of a pixel.
	 * 
	 * @param pixel
	 *            an integer pixel
	 * 
	 * @return the green component [0-255] of the pixel.
	 */
	public static int getGreen(int pixel) {
		return pixel >> 8 & 0xff;
	}

	/**
	 * Extracts the blue component of a pixel.
	 * 
	 * @param pixel
	 *            an integer pixel
	 * 
	 * @return the blue component [0-255] of the pixel.
	 */
	public static int getBlue(int pixel) {
		return pixel & 0xff;
	}

	public static int[] getAverageImgRGBVal(BufferedImage img) {
		int[][] colorBucket = new int[NUM_COLORS][NUM_RGB_VAL];

		int width = img.getWidth();
		int height = img.getHeight();

		for (int i = 0; i < PIXEL_SAMPLE_SIZE; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);

			colorBucket[0][getRed(img.getRGB(x, y)) / 10] += 1;
			colorBucket[1][getBlue(img.getRGB(x, y)) / 10] += 1;
			colorBucket[2][getGreen(img.getRGB(x, y)) / 10] += 1;
		}

		int[] avgImgRGBVal = new int[3];

		for (int i = 0; i < NUM_COLORS; i++) {
			double approxVal = 0;
			for (int j = 0; j < NUM_RGB_VAL; j++) {

				approxVal += ((double) (j * 10 + 5) * colorBucket[i][j])
						/ PIXEL_SAMPLE_SIZE;
			}
			avgImgRGBVal[i] = (int) approxVal;
		}
		return avgImgRGBVal;
	}

	public static BufferedImage[][] splitImage(BufferedImage image,
			int numXComponents, int numYComponents) {
		BufferedImage[][] imageComponents = new BufferedImage[numXComponents][numYComponents];

		int subComponentWidth = image.getWidth() / numXComponents;
		int subComponentHeight = image.getHeight() / numYComponents;

		for (int x = 0; x < numXComponents; x++) {
			for (int y = 0; y < numYComponents; y++) {
				imageComponents[x][y] = image.getSubimage(
						x * subComponentWidth, y * subComponentHeight,
						subComponentWidth, subComponentHeight);
			}
		}

		return imageComponents;
	}

	/**
	 * Converts an AWT image to SWT.
	 * 
	 * @param image
	 *            the image (<code>null</code> not permitted).
	 * 
	 * @return Image data.
	 */
	public static ImageData convertAWTImageToSWT(Image image) {
		if (image == null) {
			throw new IllegalArgumentException("Null 'image' argument.");
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (w == -1 || h == -1) {
			return null;
		}
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return convertToSWT(bi);
	}

	/**
	 * Converts a buffered image to SWT <code>ImageData</code>.
	 * 
	 * @param bufferedImage
	 *            the buffered image (<code>null</code> not permitted).
	 * 
	 * @return The image data.
	 */
	public static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage
					.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(),
					colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(),
					bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0],
							pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage
					.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
						blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(),
					bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	public static BufferedImage shrinkImage(BufferedImage image, int newWidth, int newHeight) {
		// Instantiate new shrunken image
		BufferedImage shrunkenImg = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_ARGB);

		// Create a new Graphics2D object with new shrunken image
		Graphics2D shrunkenImgGraphics = shrunkenImg.createGraphics();

		// Scale the old image down
		shrunkenImgGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		shrunkenImgGraphics.drawImage(image, 0, 0, newWidth, newHeight, null);
		// Dispose of the old graphics object
		shrunkenImgGraphics.dispose();
		
		return shrunkenImg;
	}
	
	public static BufferedImage fitImage(BufferedImage image, int labelWidth,
			int labelHeight) {
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		if (imgWidth > imgHeight) {
			double aspectRatio = (double) imgHeight / imgWidth;
			imgWidth = labelWidth;
			imgHeight = (int) (imgWidth * aspectRatio);
		} else {
			double aspectRatio = (double) imgWidth / imgHeight;
			imgHeight = labelHeight;
			imgWidth = (int) (labelHeight * aspectRatio);
		}

		return ImageManipulator.shrinkImage(image, imgWidth, imgHeight);
	}

}
