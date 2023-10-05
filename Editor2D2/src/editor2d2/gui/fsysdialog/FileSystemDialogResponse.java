package editor2d2.gui.fsysdialog;

import java.io.File;

/**
 * A container class that is returned by the Window when calling its 
 * showOpenDialog-method. When this method is called the file system 
 * dialog window is displayed. When the user takes an action in the 
 * file system prompt by either closing it or selecting files/folders.
 * The result of this prompt will return a FileSystemDialogResponse 
 * object. This object will contain whether the dialog was closed 
 * with approval (i.e. by choosing a file/folder) as well as the 
 * selected files/folders.
 * <br/><br/>
 * 
 * See the showOpenDialog-method of Window for more information on 
 * displaying the file system dialog window.
 * 
 * @author User
 *
 */
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
