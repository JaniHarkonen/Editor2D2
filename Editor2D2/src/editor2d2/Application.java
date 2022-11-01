package editor2d2;

import javax.swing.UIManager;

import editor2d2.gui.Window;
import editor2d2.model.app.AppState;
import editor2d2.model.app.Controller;
import editor2d2.modules.FactoryService;
import editor2d2.resources.Resources;

public class Application {
	
		// Reference to the native Resources of the application
	public static Resources resources;
	
		// Reference to the application Controller
	public static Controller controller;
	
		// Reference to the application master Window
	public static Window window;
	
		// Reference to the state of the application that
		// functions as the model, the AppState is controlled
		// by the Controller
	private static AppState appState;
	
	
	public static void main(String[] args) {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception e ) { }
		
		FactoryService.initialize();
		resources = Resources.instantiate();
		appState = new AppState();
		controller = Controller.instantiate(appState);
		window = Window.instantiate();
		window.setup();
	}
}
