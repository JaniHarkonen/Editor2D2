package editor2d2.model.app;

import editor2d2.DebugUtils;
import editor2d2.model.Handles;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

public class Controller implements Vendor {

		// Reference to the AppState that the Controller manipulates
	private AppState appState;
	
		// Reference to the SubscriptionService that the Controller uses
		// to notify the GUI when the AppState changes
	public final SubscriptionService subscriptionService;
	
		// Reference to the currently open project
	private Project project;
	
		// Reference to the currently active Layer
	private Layer layer;
	
		// Reference to the selected placeable
	private Placeable selectedPlaceable;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller(AppState appState) {
		this.appState = appState;
		this.subscriptionService = new SubscriptionService();
		this.project = null;
		this.selectedPlaceable = null;
		this.layer = null;
		
		DebugUtils.controllerDebugSetup(this);
	}
	
		// Instantiates the Controller
	public static Controller instantiate(AppState appState) {
		if( isInstantiated == true )
		return null;
		
		return new Controller(appState);
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
	public Layer getLayer() {
		return this.layer;
	}
	
		// Opens a new project and sets it as the active one
	public void openProject(Project project) {
		this.project = project;
		
		this.subscriptionService.register(Handles.ACTIVE_PROJECT, this);
	}
	
		// Selects an Asset that is to be placed
	public void selectAsset(Asset asset) {
		this.selectedPlaceable = asset.createPlaceable();
		
		this.subscriptionService.register(Handles.SELECTED_PLACEABLE, this);
	}
	
		// Sets the currently active Layer
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
}
