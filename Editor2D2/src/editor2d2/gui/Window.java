package editor2d2.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import editor2d2.Application;
import editor2d2.gui.fsysdialog.FileSystemDialogResponse;
import editor2d2.gui.fsysdialog.FileSystemDialogSettings;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

/**
 * This class is one of the root components outlined in 
 * Application. Window is contains the root of the GUI as 
 * well as references to commonly used global GUI-
 * components such as the ModalWindow used by the modules 
 * and the JFileChooser used to display a file system 
 * dialog window. Window can be accessed by referencing 
 * the Application: Application.window.
 * <br/><br/>
 * 
 * This class is a singleton and must therefore be 
 * instantiated with the static instantiate-method. Once 
 * the Window has been instantiated, it can be set up by 
 * calling the setup-method which creates the instances 
 * of all the commonly used GUI-components and creates 
 * the master window.
 * <br/><br/>
 * 
 * Window also holds a public SubscriptionService that 
 * should mainly be used amongst the GUI-components to 
 * pass around information in cases where components 
 * may not be available for referencing due to their 
 * rendition order.
 * <br/><br/>
 * 
 * This application uses Swing for its GUI, thus the 
 * master window is a JFrame.
 * 
 * @author User
 *
 */
public class Window implements Vendor {

	/**
	 * Whether an instance of this class has been created.
	 * (The class is a singleton, so only one instance is 
	 * allowed.)
	 */
	public static boolean isInstantiated = false;

	/**
	 * Default width of the master window.
	 */
	public static final int DEFAULT_WINDOW_WIDTH = 640;
	
	/**
	 * Default height of the master window.
	 */
	public static final int DEFAULT_WINDOW_HEIGHT = 480;
	
	/**
	 * Default master window title.
	 */
	public static final String DEFAULT_TITLE = "Editor2D v.2.0.0";
	
	/**
	 * SubscriptionService used by the GUI-components to 
	 * reference each other.
	 */
	public final SubscriptionService subscriptionService;
	
	/**
	 * JFrame representing the master window.
	 */
	private JFrame window;
	
	/**
	 * ModalWindow that can be used to display different 
	 * types of ModalViews. Sometimes multiple ModalViews 
	 * can be open at the same time.
	 * 
	 * See ModalWindow and ModalView for more information.
	 */
	private ModalWindow modal;
	
	/**
	 * JFileChooser instance that can be used to display 
	 * the file system dialog window when loading or 
	 * saving data to external files, for example.
	 */
	private JFileChooser fileSystemDialog;
	
	/**
	 * The starting width of the master window.
	 * (Doesn't update when the window is resized.)
	 */
	private int width;
	
	/**
	 * The starting height of the master window.
	 * (Doesn't update when the window is resized.)
	 */
	private int height;
	
	/**
	 * The initial title of the master window.
	 * (Doesn't update if the window title changes.)
	 */
	private String title;
	
	
	/**
	 * Constructs an instance of the Window with a given 
	 * width, height and title. <b>Notice: </b>this 
	 * constructor is privated because the class is a 
	 * singleton, thus, an instance is only created when 
	 * the static instantiate-method is called.
	 * 
	 * This method alone doesn't set up the master 
	 * window, rather it only creates an instance and 
	 * configures some basic settings. See setup-method 
	 * for the setup itself.
	 * 
	 * See instantiate-method for more information.
	 * 
	 * @param width Initial width of the master window.
	 * @param height Initial height of the master window.
	 * @param title Initial title of the master window.
	 */
	private Window(int width, int height, String title) {
		this.subscriptionService = new SubscriptionService();
		this.width = width;
		this.height = height;
		this.title = title;
	}

	/**
	 * Sets up the master window and instantiates all the 
	 * relevant GUI-components. The master window JFrame 
	 * will be instantiated and configured here, and a 
	 * WindowFocusListener will be added to it to manage 
	 * hotkey presses when the window is out of focus 
	 * (hotkeys will be cleared when focus is lost).
	 * 
	 * After the master window JFrame is established, a
	 * Root instance will be created and rendered onto 
	 * it. Root will function as the base GUI-component, 
	 * a JPanel, where all the core components will be 
	 * rendered. A WindowToolBar instance is also 
	 * attatched to the master window that typically 
	 * holds the window dropdown menus.
	 * 
	 * Finally the JFrame will be set visible and a 
	 * ModalWindow and JFileChooser will be instantiated.
	 */
	public void setup() {
		this.window = new JFrame();
		this.window.setSize(this.width, this.height);
		this.window.setLocationRelativeTo(null);
		this.window.setTitle(this.title);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.addKeyListener(Application.controller.getHotkeyListener());
		this.window.setFocusable(true);
		
		this.window.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent e) { }

			@Override
			public void windowLostFocus(WindowEvent e) {
				Application.controller.getHotkeyListener().resetKeys();
			}
		});
		
		this.window.setJMenuBar(new WindowToolbar());
		this.window.add((new Root()).render());
		
		this.window.setVisible(true);
		
		this.modal = new ModalWindow(this);
		this.fileSystemDialog = new JFileChooser();
		
		this.subscriptionService.register(Handles.MODAL, this);
	}
	
	/**
	 * Pops up a given ModalView instance of an Asset in 
	 * the ModalWindow. The ModalView will be added to 
	 * the ModalWindow if it is already open.
	 * 
	 * @param mv ModalView of an Asset that is to be 
	 * displayed in the ModalWindow.
	 */
	public void popup(ModalView<? extends Asset> mv) {
		this.modal.openModal(mv);
	}

	/**
	 * Pops up a given ModalView instance of an Asset in 
	 * the ModalWindow using a specified title. The ModalView 
	 * will be added to the ModalWindow if it is already open.
	 * 
	 * @param title Title that the ModalWindow should use.
	 * 
	 * @param mv ModalView of an Asset that is to be 
	 * displayed in the ModalWindow.
	 */
	public void popup(String title, ModalView<? extends Asset> mv) {
		this.modal.openModal(title, mv);
	}
	
	/**
	 * Shows a file system dialog window for opening files 
	 * and/or directories with given FileSystemDialogSettings 
	 * and returns the selected files and/or directories.
	 * 
	 * FileSystemDialogSettings object will be used to 
	 * determine functionalities allowed by the window 
	 * including the available file types, whether directories 
	 * can be selected, whether multiple items can be selected 
	 * etc. If no FileSystemDialogSettings object is provided, 
	 * this method will return NULL.
	 * 
	 * See FileSystemDialogSettings for more information 
	 * on setting up a file system dialog window.
	 * 
	 * See FileSystemDialogResponse for more information on  
	 * file system dialog responses that will be returned by 
	 * this method.
	 * 
	 * @param settings FileSystemDialogSettings object that 
	 * will be used to determine the file system dialog 
	 * window features.
	 * 
	 * @return Returns a FileSystemDialogResponse object 
	 * containing information on the user selection, including 
	 * all selected files and/or directories, if there were 
	 * any.
	 */
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
	
	/**
	 * Shows a file system dialog window for saving files with 
	 * given FileSystemDialogSettings and returns the selected 
	 * files and/or directories.
	 * 
	 * FileSystemDialogSettings objects have a more limited 
	 * role in file saving, however, they are still used to 
	 * determine the available file types.
	 * 
	 * See FileSystemDialogSettings for more information 
	 * on setting up a file system dialog window.
	 * 
	 * See FileSystemDialogResponse for more information on  
	 * file system dialog responses that will be returned by 
	 * this method.
	 * 
	 * @param settings FileSystemDialogSettings object that 
	 * will be used to determine the file system dialog 
	 * window features.
	 * 
	 * @return Returns a FileSystemDialogResponse object 
	 * containing information on the user selection, including 
	 * the selected file.
	 */
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
	
	/**
	 * Creates an instance of Window if one has NOT been 
	 * created already. This class is a singleton, thus, 
	 * only one instance of Window is allowed. 
	 * 
	 * @return Returns a refrence to the created Window 
	 * instance.
	 */
	public static Window instantiate() {
		return instantiate(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, DEFAULT_TITLE);
	}
	
	/**
	 * Creates an instance of Window if one has NOT been 
	 * created already. The master window will be given 
	 * a specific initial width, height and title. 
	 * 
	 * This class is a singleton, thus, only one instance 
	 * of Window is allowed. 
	 * 
	 * @param width Initial width of the master window 
	 * (in pixels).
	 * @param height Initial height of the master window 
	 * (in pixels). 
	 * @param title Initial title of the master window.
	 * 
	 * @return Returns a reference to the created Window 
	 * instance.
	 */
	public static Window instantiate(int width, int height, String title) {
		if( isInstantiated )
		return null;
		
		isInstantiated = true;
		
		return new Window(width, height, title);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a refrence to the JFrame representing 
	 * the master window.
	 * 
	 * @return Returns the master window JFrame.
	 */
	public JFrame getFrame() {
		return this.window;
	}
	
	/**
	 * Returns a reference to the ModalWindow instance 
	 * that can be used to display different types of 
	 * ModalViews.
	 * 
	 * @return A reference to the ModalWindow.
	 */
	public ModalWindow getModalWindow() {
		return this.modal;
	}
	
	/**
	 * Steals the window focus away from all GUI-
	 * components and returns it back to the master 
	 * window JFrame.
	 */
	public void unfocusAllComponents() {
		this.window.requestFocusInWindow();
	}
}
