package com.gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.debug.Log;

public class ErrorMessageGUI {

	protected Shell errorMessageShell;
	
	private String errorMessage;

	public ErrorMessageGUI() {
		errorMessageShell = null;
		
		errorMessage = "";
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		errorMessageShell.open();
		errorMessageShell.layout();
		while (!errorMessageShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		errorMessageShell = new Shell();
		errorMessageShell.setSize(225, 120);
		errorMessageShell.setText("Error");
		
		Button okayButton = new Button(errorMessageShell, SWT.NONE);
		okayButton.setBounds(67, 51, 75, 25);
		okayButton.setText("Okay");
		okayButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				errorMessageShell.dispose();
			}
		});
		
		Text errorDisplayText = new Text(errorMessageShell, SWT.READ_ONLY | SWT.CENTER);
		errorDisplayText.setBounds(10, 10, 189, 35);
		errorDisplayText.setText(errorMessage);

	}
	
	public void setErrorMessage(String errMessage) {
		this.errorMessage = errMessage;
		Log.print(errMessage);
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ErrorMessageGUI window = new ErrorMessageGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
