package editor2d2.gui.fsysdialog;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * A wrapper class for settings that will be passed onto the Window
 * when accessing the file system dialog window. The same class can be
 * used for both opening and saving files or directories.
 * @author User
 *
 */
public class FileSystemDialogSettings {
	
	/**
	 * Only allows files to be selected in the open dialog.
	 * (See JFileChooser.FILES_ONLY)
	 */
	public static final int FILES_ONLY = JFileChooser.FILES_ONLY;
	
	/**
	 * Only allows directories to be selected in the open dialog.
	 * (See JFileChooser.DIRECTORIES_ONLY)
	 */
	public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
	
	/**
	 * Allows both files and directories to be selected in the open dialog.
	 * (See JFileChooser.FILES_AND_DIRECTORIES)
	 */
	public static final int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
	
	/**
	 * Selection mode that determines what types of file system items can be selected.
	 * Three types (that correspond to JFileChooser's types) are supported:
	 * <br/>
	 * - <b>FileSystemDialogSettings.FILES_ONLY <br/>
	 * - FileSystemDialogSettings.DIRECTORIES_ONLY <br/>
	 * - FileSystemDialogSettings.FILES_AND_DIRECTORIES</b>
	 */
	public int selectionMode = FILES_ONLY;
	
	/**
	 * Whether multiple selection is allowed when opening files/directories.
	 */
	public boolean allowMultiple = false;
	
	/**
	 * Whether to allow the all file types to be selected.
	 */
	public boolean allowAll = false;
	
	/**
	 * Array of file filters that determine which types of files can be opened or saved
	 * (and, as a result, displayed) in the file system dialog window.
	 */
	public FileFilter[] filters = null;
}
