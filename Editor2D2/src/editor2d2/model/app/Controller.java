package editor2d2.model.app;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.model.project.Project;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.subservice.Vendor;

public class Controller implements Vendor {

		// Reference to the currently open project
	private Project project;
	
		// (DEBUG) Reference to the currently selected Asset that will be placed
	private Asset DEBUGasset;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller() {
		this.project = null;
		this.DEBUGasset = null;
		
		DebugUtils.controllerDebugSetup(this);
	}
	
		// Instantiates the Controller
	public static Controller instantiate() {
		if( isInstantiated == true )
		return null;
		
		return new Controller();
	}
	
		// Returns a reference to the currently open project
	public Project getProject() {
		return this.project;
	}
	
		// (DEBUG) Returns a reference to the currently selected asset
	public Asset DEBUGgetAsset() {
		return this.DEBUGasset;
	}
	
		// Opens a new project and sets it as the active one
	public void openProject(Project project) {
		this.project = project;
		
		Application.subscriptionService.register("active-project", this);
	}
	
		// Selects an Asset that is to be placed
	public void DEBUGsetAsset(Asset asset) {
		this.DEBUGasset = asset;
		
		Application.subscriptionService.register("selected-asset", this);
	}
}
