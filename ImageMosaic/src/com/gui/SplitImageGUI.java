package com.gui;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;

import com.debug.Log;
import com.image.ImageManipulator;

public class SplitImageGUI {

	protected Shell splitImageShell;
	
	protected Spinner numYCompSpinner;
	protected Scale numYCompScale; 
	protected Scale numXCompScale;
	protected Spinner numXCompSpinner;
	protected Label mainImageLabel;

	protected BufferedImage image;
	protected BufferedImage dispImage;

	protected int numXSubcomponents;
	protected int numYSubcomponents;

	public SplitImageGUI() {
		splitImageShell = null;
		
		numYCompSpinner = null;
		numYCompScale = null;
		numXCompScale = null;
		numXCompSpinner = null;
		mainImageLabel = null;
		
		image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		dispImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

		numXSubcomponents = 0;
		numYSubcomponents = 0;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		splitImageShell.open();
		splitImageShell.layout();
		while (!splitImageShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		splitImageShell = new Shell();
		splitImageShell.setSize(500, 500);
		splitImageShell.setText("Image Mosaic");
		
		Log.print("Opening the split image GUI.");

		Group splitImageGroup = new Group(splitImageShell, SWT.NONE);
		splitImageGroup.setText("Split Main Image");
		splitImageGroup.setBounds(10, 10, 464, 400);

		numYCompSpinner = new Spinner(splitImageGroup, SWT.BORDER);
		numYCompScale = new Scale(splitImageGroup, SWT.VERTICAL);
		numXCompScale = new Scale(splitImageGroup, SWT.NONE);
		numXCompSpinner = new Spinner(splitImageGroup, SWT.BORDER);
		mainImageLabel = new Label(splitImageGroup, SWT.NONE);

		numYCompSpinner.setBounds(10, 20, 47, 22);
		numYCompSpinner.setMaximum(150);
		numYCompSpinner.setMinimum(1);
		numYCompSpinner.setSelection(1);
		numYCompSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				numYCompScale.setSelection(numYCompSpinner.getSelection());
				dispImage = outlineSplitImage(image,
						numXCompSpinner.getSelection(),
						numYCompScale.getSelection());
				final Image swtMainImage = new Image(Display.getDefault(),
						ImageManipulator.convertAWTImageToSWT(dispImage));
				mainImageLabel.setImage(swtMainImage);
			}
		});

		numYCompScale.setBounds(10, 48, 42, 300);
		numYCompScale.setMaximum(150);
		numYCompScale.setMinimum(1);
		numYCompScale.setSelection(1);
		numYCompScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				numYCompSpinner.setSelection(numYCompScale.getSelection());
				dispImage = outlineSplitImage(image,
						numXCompSpinner.getSelection(),
						numYCompSpinner.getSelection());
				final Image swtMainImage = new Image(Display.getDefault(),
						ImageManipulator.convertAWTImageToSWT(dispImage));
				mainImageLabel.setImage(swtMainImage);
			}
		});

		numXCompScale.setMaximum(150);
		numXCompScale.setMinimum(1);
		numXCompScale.setSelection(1);
		numXCompScale.setBounds(41, 348, 360, 42);
		numXCompScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				numXCompSpinner.setSelection(numXCompScale.getSelection());
				dispImage = outlineSplitImage(image,
						numXCompSpinner.getSelection(),
						numYCompSpinner.getSelection());
				final Image swtMainImage = new Image(Display.getDefault(),
						ImageManipulator.convertAWTImageToSWT(dispImage));
				mainImageLabel.setImage(swtMainImage);
			}
		});

		numXCompSpinner.setMaximum(150);
		numXCompSpinner.setMinimum(1);
		numXCompSpinner.setSelection(1);
		numXCompSpinner.setBounds(407, 357, 47, 22);
		numXCompSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				numXCompScale.setSelection(numXCompSpinner.getSelection());
				dispImage = outlineSplitImage(image,
						numXCompScale.getSelection(),
						numYCompSpinner.getSelection());
				final Image swtMainImage = new Image(Display.getDefault(),
						ImageManipulator.convertAWTImageToSWT(dispImage));
				mainImageLabel.setImage(swtMainImage);
			}
		});

		int mainImageLabelWidth = 391;
		int mainImageLabelHeight = 328;
		mainImageLabel.setBounds(63, 20, mainImageLabelWidth,
				mainImageLabelHeight);
		this.image = ImageManipulator.fitImage(this.image, mainImageLabelWidth,
				mainImageLabelHeight);
		dispImage = outlineSplitImage(this.image,
				numXCompSpinner.getSelection(), numYCompSpinner.getSelection());
		final Image swtMainImage = new Image(Display.getDefault(),
				ImageManipulator.convertAWTImageToSWT(dispImage));
		mainImageLabel.setImage(swtMainImage);

		Button cancelButton = new Button(splitImageShell, SWT.NONE);
		cancelButton.setBounds(399, 427, 75, 25);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Log.print("Cancelled splitting image. Closing split image GUI.");
				splitImageShell.dispose();
			}
		});

		Button okayButton = new Button(splitImageShell, SWT.NONE);
		okayButton.setBounds(318, 427, 75, 25);
		okayButton.setText("Okay");
		okayButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				numXSubcomponents = numXCompSpinner.getSelection();
				numYSubcomponents = numYCompSpinner.getSelection();
				
				Log.print("Image split has been confirmed.");
				
				splitImageShell.dispose();
			}
		});
	}
	
	protected BufferedImage outlineSplitImage(BufferedImage image,
			int numXComponents, int numYComponents) {
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		int xSpacing = imgWidth / numXComponents;
		int ySpacing = imgHeight / numYComponents;

		BufferedImage copy = new BufferedImage(imgWidth, imgHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = copy.createGraphics();

		graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);

		for (int x = xSpacing; x < imgWidth; x += xSpacing) {
			Line2D line = new Line2D.Float(x, 0, x, imgHeight);
			graphics.draw(line);
		}

		for (int y = ySpacing; y < imgHeight; y += ySpacing) {
			Line2D line = new Line2D.Float(0, y, imgWidth, y);
			graphics.draw(line);
		}

		graphics.dispose();

		return copy;
	}

	public final void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public final int getXComponents() {
		return numXSubcomponents;
	}
	
	public final int getYComponents() {
		return numYSubcomponents;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SplitImageGUI window = new SplitImageGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
