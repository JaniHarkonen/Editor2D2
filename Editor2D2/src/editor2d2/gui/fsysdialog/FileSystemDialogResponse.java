package editor2d2.gui.fsysdialog;

import java.io.File;

public class FileSystemDialogResponse {

	/**
	 * Whether the file system dialog window was closed with approval.
	 */
	public boolean isApproved = false;
	
	/**
	 * Contains the paths of all the files and/or directories that were
	 * selected.
	 */
	public File[] filepaths = {};
}
