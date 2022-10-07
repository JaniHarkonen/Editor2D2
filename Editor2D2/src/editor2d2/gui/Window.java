package editor2d2.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import editor2d2.Application;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.gui.modal.views.ModalView;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.subservice.Vendor;

public class Window implements Vendor {

		// This class is a singleton, only instantiate if not already
	public static boolean isInstantiated = false;
	
		// Default window width
	public static final int DEFAULT_WINDOW_WIDTH = 640;
	
		// Default window height
	public static final int DEFAULT_WINDOW_HEIGHT = 480;
	
		// Default window title
	public static final String DEFAULT_TITLE = "Editor2D v.2.0.0";
	
		// JFrame representing the window
	private JFrame window;
	
		// Reference to the Modal Window
	private ModalWindow modal;
	
		// Reference to the JFileChooser that can be used to open a file system dialog
	private JFileChooser fileSystemDialog;
	
	
	private Window(int width, int height, String title) {
		this.window = new JFrame();
		this.window.setSize(width, height);
		this.window.setLocationRelativeTo(null);
		this.window.setTitle(title);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.window.setJMenuBar(new WindowToolbar());
		this.window.add((new Root()).render());
		
		this.window.setVisible(true);
		
		this.modal = new ModalWindow(this);
		this.fileSystemDialog = new JFileChooser();
		
		Application.subscriptionService.register("modal", this);
	}
	
	
		// Pops up a given Modal View in the window's Modal Window
	public void popup(ModalView<? extends Asset> mv) {
		this.modal.openModal(mv);
	}
	
		// Opens a file system dialog for opening files/directories with given settings
		// and returns the selected file(s)
	public FileSystemDialogResponse showOpenDialog(FileSystemDialogSettings settings) {
		FileSystemDialogResponse res = new FileSystemDialogResponse();
		
		if( settings == null )
		return res;
		
			// Apply settings
		this.fileSystemDialog.setMultiSelectionEnabled(settings.allowMultiple);	// multi-selection
		this.fileSystemDialog.setFileSelectionMode(settings.selectionMode);		// selection mode
		
		this.fileSystemDialog.resetChoosableFileFilters();	// clear filters first
		
		this.fileSystemDialog.setAcceptAllFileFilterUsed(settings.allowAll);
		
			// Apply new filters
		if( settings.filters != null )
		{
			this.fileSystemDialog.setFileFilter(settings.filters[0]);
			
			for( FileFilter ff : settings.filters )
			this.fileSystemDialog.addChoosableFileFilter(ff);
		}
		
			// Whether the dialog was closed with approval
		res.isApproved = (this.fileSystemDialog.showOpenDialog(this.window) == JFileChooser.APPROVE_OPTION);
		
		if( !res.isApproved )
		return res;
		
			// Insert the paths of selected files into the response object
		File[] paths = this.fileSystemDialog.getSelectedFiles();
		
			// getSelectedFile must be used in case of single-selection
		if( !settings.allowMultiple )
		{
			paths = new File[1];
			paths[0] = this.fileSystemDialog.getSelectedFile();
		}
		
		res.filepaths = paths;
		
		return res;
	}
	
		// Opens a file system dialog for saving files with given settings and returns
		// the saved file
	public FileSystemDialogResponse showSaveDialog(FileSystemDialogSettings settings) {
		FileSystemDialogResponse res = new FileSystemDialogResponse();
		
		if( settings == null )
		return res;
		
			// APPLY FILTERS HERE
		
			// Whether the dialog was closed with approval
		res.isApproved = (this.fileSystemDialog.showSaveDialog(this.window) == JFileChooser.APPROVE_OPTION);
		
		if( !res.isApproved )
		return res;
		
			// Insert the path of the saved file
		File[] paths = { this.fileSystemDialog.getSelectedFile() };
		res.filepaths = paths;
		
		return res;
	}
	
		// Instantiates the window, if it's not been already
		// with default settings
	public static Window instantiate() {
		return instantiate(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, DEFAULT_TITLE);
	}
	
		// Instantiates the window with given settings
	public static Window instantiate(int width, int height, String title) {
		if( isInstantiated )
		return null;
		
		isInstantiated = true;
		
		return new Window(width, height, title);
	}
	
	
		// Returns a reference to the JFrame of the window
	public JFrame getFrame() {
		return this.window;
	}
	
		// Returns a reference to the Modal Window
	public ModalWindow getModalWindow() {
		return this.modal;
	}
}