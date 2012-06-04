package com.image;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.utils.Utils;

public class ImageFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.jpeg) || extension.equals(Utils.jpg)
					|| extension.equals(Utils.png)
			// ||extension.equals(Utils.tiff)
			// || extension.equals(Utils.tif)
			// || extension.equals(Utils.gif)
			) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		String description =
		// "*.tiffs, " +
		// "*.tifs, " +
		"*.jpegs, " + "*.jpgs, " + "*.pngs";
		return description;
	}

}
