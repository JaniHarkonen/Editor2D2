package editor2d2;

import javax.swing.UIManager;

import editor2d2.gui.Window;
import editor2d2.model.app.AppState;
import editor2d2.model.app.Controller;
import editor2d2.modules.FactoryService;
import editor2d2.resources.Resources;

/**
 * The entry point for the application containing the 
 * main-method. This class also contains references 
 * to the instances shared by the software components.
 * 
 * @author User
 *
 */
public class Application {
	
	/**
	 * Reference to the Resources of the application that 
	 * can be used by its components. Contains references 
	 * to assets loaded from external sources such as images.
	 */
	public static Resources resources;
	
	/**
	 * Reference to the Controller of the application that 
	 * the GUI-components can use to trigger changes in the 
	 * model.
	 */
	public static Controller controller;
	
	/**
	 * Reference to the application master Window which is 
	 * mainly used by the GUI-components to access each 
	 * other.
	 */
	public static Window window;
	
	/**
	 * Reference to the model of the application also known as 
	 * app state. AppState is not accessible directly by the 
	 * software components as it is designed to be changed 
	 * by methods in Controller.
	 */
	private static AppState appState;
	
	
	/**
	 * Main entry point of the application. Determines the 
	 * Swing look and feel and instantiates/initializes all 
	 * root components of the application including the master 
	 * Window.
	 * 
	 * @param args NOT USED.
	 */
	public static void main(String[] args) {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch( Exception e ) { }
		
			// Initializes module factories
		FactoryService.initialize();
		
			// Loads application resources
		resources = Resources.instantiate();
		
			// Creates application model
		appState = new AppState();
		
			// Instantiates application controller
		controller = Controller.instantiate(appState);
		
			// Instantiates master window and sets up relevant
			// GUI instances
		window = Window.instantiate();
		window.setup();
	}
}
