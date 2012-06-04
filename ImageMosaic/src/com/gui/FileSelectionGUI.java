package com.gui;

import java.io.File;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.debug.Log;
import com.image.ImageFilter;
import com.utils.FileChooser;

public class FileSelectionGUI {
	private final FileChooser fc = new FileChooser(new ImageFilter());
	
	protected Shell fileSelectShell;
	private Text mainImageFileTxtBox;
	private Text componentImageFilesTxtBox;

	private File mainImageFile;
	private File[] componenetImageFiles;

	public FileSelectionGUI() {
		fileSelectShell = null;
		mainImageFileTxtBox = null;
		componentImageFilesTxtBox = null;

		mainImageFile = null;
		componenetImageFiles = null;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		fileSelectShell.open();
		fileSelectShell.layout();
		while (!fileSelectShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		fileSelectShell = new Shell();
		fileSelectShell.setSize(280, 360);
		fileSelectShell.setText("Image Mosaic");
		Log.print("Opening the file selection GUI.");
		
		// /////////////////////////////////////////////////////////////////////////////////////////
		// Main Image Group
		// /////////////////////////////////////////////////////////////////////////////////////////
		Group mainImageGroup = new Group(fileSelectShell, SWT.NONE);
		mainImageGroup.setText("Main Image");
		mainImageGroup.setBounds(10, 10, 244, 85);

		Button mainImageButton = new Button(mainImageGroup, SWT.NONE);
		mainImageButton.setBounds(10, 20, 224, 25);
		mainImageButton.setText("Select Main Image");

		mainImageFileTxtBox = new Text(mainImageGroup, SWT.BORDER
				| SWT.READ_ONLY);
		mainImageFileTxtBox.setBounds(10, 51, 224, 25);

		mainImageButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Choose the main image file
				mainImageFile = fc.chooseImageFile();
				if (mainImageFile != null)
					mainImageFileTxtBox.setText(mainImageFile.getName());
			}
		});

		// /////////////////////////////////////////////////////////////////////////////////////////
		// Component Image Group
		// /////////////////////////////////////////////////////////////////////////////////////////
		Group ComponentImageGroup = new Group(fileSelectShell, SWT.NONE);
		ComponentImageGroup.setText("Component Images");
		ComponentImageGroup.setBounds(10, 101, 244, 150);

		Button componentImageButton = new Button(ComponentImageGroup, SWT.NONE);
		componentImageButton.setBounds(10, 20, 224, 25);
		componentImageButton.setText("Select Component Images");

		componentImageFilesTxtBox = new Text(ComponentImageGroup, SWT.BORDER
				| SWT.READ_ONLY | SWT.V_SCROLL);
		componentImageFilesTxtBox.setBounds(10, 51, 224, 89);

		componentImageButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Choose the component image files
				componenetImageFiles = fc
						.chooseImageFiles();
				if (componenetImageFiles != null) {
					for (File f : componenetImageFiles) {
						componentImageFilesTxtBox.append(f.getName() + "\n");
					}
				}
			}
		});

		// /////////////////////////////////////////////////////////////////////////////////////////
		// Cancel Button
		// /////////////////////////////////////////////////////////////////////////////////////////
		Button cancelButton = new Button(fileSelectShell, SWT.NONE);
		cancelButton.setBounds(179, 287, 75, 25);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Log.print("Cancelled file selection. Closing file selection GUI.");
				fileSelectShell.dispose();
			}
		});

		// /////////////////////////////////////////////////////////////////////////////////////////
		// Okay Button
		// /////////////////////////////////////////////////////////////////////////////////////////
		Button okayButton = new Button(fileSelectShell, SWT.NONE);
		okayButton.setBounds(98, 287, 75, 25);
		okayButton.setText("Okay");

		okayButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (mainImageFile == null) {
					ErrorMessageGUI errGUI = new ErrorMessageGUI();
					errGUI.setErrorMessage("Error: Need to specify an image file from which to make the mosaic.");
					errGUI.open();
				} else if (componenetImageFiles == null) {
					ErrorMessageGUI errGUI = new ErrorMessageGUI();
					errGUI.setErrorMessage("Error: Need to specify image files for the component images.");
					errGUI.open();
				} else {
					Log.print("Image file selections were valid. Continuing processing.");
					fileSelectShell.dispose();
				}
			}
		});

	}

	public final File getMainImageFile() {
		return mainImageFile;
	}

	public final File[] getComponentImageFiles() {
		return componenetImageFiles;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileSelectionGUI window = new FileSelectionGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
