package com.gui;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

//import com.custom.Messages;
import com.debug.Log;
import com.image.ImageProcessor;
import com.main.ImageMosaic;
import com.utils.FileChooser;

public class LogGUI {

	protected Shell logShell;
	private Text logText;
	private Button splitImageButton;
	private Button selectImagesButton;

	private ImageMosaic imageMosaic;
	private Button saveButton;

	public LogGUI(ImageMosaic imageMosaic) {
		this.imageMosaic = imageMosaic;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		logShell.open();
		logShell.layout();
		while (!logShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		final ImageProcessor imageProcessor = imageMosaic.getImageProcessor();

		logShell = new Shell();
		logShell.setSize(450, 315);
		logShell.setText("Image Mosaic Log");
		logShell.setLayout(null);

		logText = new Text(logShell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		logText.setBounds(5, 5, 418, 223);
		logText.setEditable(false);
		Log.print("Made by Chao Zhang.");
		printInstructions();

		selectImagesButton = new Button(logShell, SWT.NONE);
		selectImagesButton.setBounds(5, 241, 100, 25);
		selectImagesButton.setText("Select Images");
		selectImagesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final FileSelectionGUI fsGUI = new FileSelectionGUI();

				fsGUI.open();

				imageMosaic.setMainImageFile(fsGUI.getMainImageFile());
				imageMosaic.setComponentImageFiles(fsGUI
						.getComponentImageFiles());

				if (imageMosaic.getMainImageFile() != null
						&& imageMosaic.getComponentImageFiles() != null) {
					splitImageButton.setEnabled(true);

					try {
						// Set the processor's main image from the main image
						// file
						imageProcessor.setMainImage(imageMosaic
								.getMainImageFile());
						// Set the processor's component images from the
						// component files
						imageProcessor.setComponentImages(imageMosaic
								.getComponentImageFiles());
					} catch (IOException e1) {
						final ErrorMessageGUI errGui = new ErrorMessageGUI();
						errGui.setErrorMessage("Error: Could not open image file.");
						errGui.open();

						Log.print(e1.getMessage());
					}

				}
			}
		});

		splitImageButton = new Button(logShell, SWT.NONE);
		splitImageButton.setBounds(111, 241, 100, 25);
		splitImageButton.setText("Split Main Image");
		splitImageButton.setEnabled(false);
		splitImageButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final SplitImageGUI siGUI = new SplitImageGUI();
				siGUI.setImage(imageProcessor.getMainImage());
				siGUI.open();

				int numXComponents = siGUI.getXComponents();
				int numYComponents = siGUI.getYComponents();

				imageMosaic.setMosaicImage(imageProcessor.process(
						numXComponents, numYComponents));
				if (imageMosaic.getMosaicImage() != null) {
					saveButton.setEnabled(true);
				}
			}
		});

		saveButton = new Button(logShell, SWT.NONE);
		saveButton.setEnabled(false);
		saveButton.setBounds(217, 241, 100, 25);
		saveButton.setText("Save Mosaic");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new FileChooser().saveImage(imageMosaic.getMosaicImage());
			}
		});

		Button exitButton = new Button(logShell, SWT.NONE);
		exitButton.setBounds(323, 241, 100, 25);
		exitButton.setText("Close Program");
		exitButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// logShell.dispose();
				Display.getDefault().dispose();
				Log.disposeLogGUI();
				Log.print("Closed Program. Disposed Display.");
			}
		});

	}

	public final void appendLog(String str) {
		logText.append(str);
	}

	public final boolean isLogTextNull() {
		return (logText == null ? true : false);
	}

	private final void printInstructions() {
		// appendLog(new Messages().getRandomMessage());

		final String instructions = "Instructions:\n"
				+ "1. Select a main image and component images.\n"
				+ "   The main image will be mosaic'ed with the component images.\n"
				+ "   (For best performance, please try not to select more than 200 ~ 250 images)\n"
				+ "\n"
				+ "2. Split the main image.\n"
				+ "   Choose how to break the main image down.\n"
				+ "   Try to keep the aspect ratio of the mosaic the same as the original image.\n"
				+ "   (For best performance, please try not to create mosaics of more than 100 X 100 components.\n"
				+ "\n"
				+ "3. Save the mosaic.\n"
				+ "   The mosaic will likely be a very large file, so saving may take some time.\n"
				+ "\n"
				+ "4. Repeat and enjoy :)";
		
		Log.print(instructions);
	}

}
