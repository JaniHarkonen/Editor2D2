package editor2d2.model.app;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.model.project.Project;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.placeables.Placeable;
import editor2d2.model.subservice.Vendor;

public class Controller implements Vendor {

		// Reference to the currently open project
	private Project project;
	
		// Reference to the currently active Layer
	private Layer<? extends Placeable> layer;
	
		// Reference to the selected placeable
	private Placeable selectedPlaceable;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller() {
		this.project = null;
		this.selectedPlaceable = null;
		this.layer = null;
		
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
	
		// Returns a reference to the selected placeable
	public Placeable getSelectedPlaceable() {
		return this.selectedPlaceable;
	}
	
		// Returns a reference to the currently active Layer
	public Layer<? extends Placeable> getLayer() {
		return this.layer;
	}
	
		// Opens a new project and sets it as the active one
	public void openProject(Project project) {
		this.project = project;
		
		Application.subscriptionService.register("active-project", this);
	}
	
		// Selects an Asset that is to be placed
	public void selectAsset(Asset asset) {
		this.selectedPlaceable = asset.createPlaceable();
		
		Application.subscriptionService.register("selected-placeable", this);
	}
	
		// Sets the currently active Layer
	public void setLayer(Layer<? extends Placeable> layer) {
		this.layer = layer;
	}
}
