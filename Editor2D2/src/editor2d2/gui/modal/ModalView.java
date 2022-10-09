package editor2d2.gui.modal;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.model.project.Asset;

public abstract class ModalView<A extends Asset> extends GUIComponent {
	
		// Reference to the host Modal Window
	protected ModalWindow host;
	
		// Source asset that the settings will be stored / read from
	protected A source;
	
		// Text field containing the name of the Asset
	protected CTextField txtName;
	
		// Text field containing the identifier of the Asset
	protected CTextField txtIdentifier;
	
	
	public ModalView(ModalWindow host, boolean useFactorySettings) {
		if( useFactorySettings == true )
		setFactorySettings();
		
		this.host = host;
		this.txtName = new CTextField("Name:");
		this.txtIdentifier = new CTextField("Identifier:");
	}
	
	public ModalView(ModalWindow host) {
		this(host, false);
	}
	
	
		// Creates a JPanel of a default modal view that wraps a given
		// JPanel
	protected JPanel createDefaultModalView(JPanel wrappedElement) {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Name field
		this.txtName.setText(this.source.getName());
		
			// Identifier field
		this.txtIdentifier.setText(this.source.getIdentifier());
		
			// Control area (create, save, cancel)
		JPanel containerControls = new JPanel();
		containerControls.add(new ClickableButton("Create", (e) -> { actionCreate(); }));
		containerControls.add(new ClickableButton("Cancel", (e) -> { actionCancel(); }));
		
		container.add(this.txtName.render());
		container.add(this.txtIdentifier.render());
		container.add(wrappedElement);
		container.add(containerControls);
		
		return container;
	}
	
		// Creates a default Asset with default settings and sets it as
		// the source asset
		// TO BE OVERRIDDEN
	public abstract void setFactorySettings();
	
		// Configures the source Asset to correspond to the values input
		// in the Modal View
		// CAN BE OVERRIDDEN
	public void saveChanges(boolean doChecks) {
		String name = this.txtName.getText();
		String id = this.txtIdentifier.getText();
		
		if( doChecks && (name.equals("") || id.equals("")) )
		return;
		
		this.source.setName(name);
		this.source.setIdentifier(id);
	}
	
		// Called upon clicking "Create"
		// CAN BE OVERRIDDEN
	protected void actionCreate() {
		saveChanges(true);
		finalizeCreation();
		
		this.host.closeModalWindow();
	}
	
		// Called upon clicking "Cancel"
		// CAN BE OVERRIDDEN
	protected void actionCancel() {}
	
		// Updates the Project and the Asset Pane to the creation
		// of a new Asset
	protected void finalizeCreation() {
		Application.controller.getProject().addAsset(this.source);
		Application.window.getModalWindow().getAssetPane().update();
	}
	
	
		// Returns a reference to the asset being operated on
	public A getAsset() {
		return this.source;
	}
	
		// Sets the asset being operated on
	public void setAsset(A source) {
		this.source = source;
	}
}
