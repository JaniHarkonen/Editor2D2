package editor2d2.gui.modal;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.gui.components.ClickableButton;
import editor2d2.gui.components.requirements.RequireStringName;
import editor2d2.model.project.Asset;
import editor2d2.model.project.HasAsset;

public abstract class ModalView<A extends Asset> extends GUIComponent implements HasAsset {
	
		// Reference to the host Modal Window
	protected ModalWindow host;
	
		// Source asset that the settings will be stored / read from
	protected A source;
	
		// Text field containing the name of the Asset
	protected CTextField txtName;
	
		// Text field containing the identifier of the Asset
	protected CTextField txtIdentifier;
	
		// Whether an Asset is being edited
	protected boolean isEdited;
	
	
	public ModalView(ModalWindow host, boolean useFactorySettings) {
		if( useFactorySettings == true )
		setFactorySettings();
		
		this.host = host;
		this.txtName = new CTextField("Name:", new RequireStringName());
		this.txtIdentifier = new CTextField("Identifier:", new RequireUnusedIdentifier(Application.controller.getActiveProject()));
		this.isEdited = false;
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
		((RequireUnusedIdentifier) this.txtIdentifier.getRequirementFilter()).setAsset(this.source);
		this.txtIdentifier.setText(this.source.getIdentifier());
		
			// Control area (create, save, cancel)
		JPanel containerControls = new JPanel();
		
		if( this.isEdited )
		containerControls.add(new ClickableButton("Save", (e) -> { actionSave(); }));
		else
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
	
	public int validateInputs() {
		return GUIUtilities.checkMultiple(
				this.txtName.getRequirementFilter().checkValid(),
				this.txtIdentifier.getRequirementFilter().checkValid()
		);
	}
	
		// Configures the source Asset to correspond to the values input
		// in the Modal View
		// CAN BE OVERRIDDEN
	public boolean saveChanges(boolean doChecks) {
		if( doChecks )
		{
			int issues = validateInputs();
			
			if( issues > 0 )
			{
				GUIUtilities.showErrorIfInvalid("Invalid input!", issues);
				return false;
			}
		}
		
		String name = (String) this.txtName.getRequirementFilter().getValue();
		String id = (String) this.txtIdentifier.getRequirementFilter().getValue();
		
		this.source.setName(name);
		this.source.setIdentifier(id);
		
		return true;
	}
	
		// Called upon clicking "Create"
		// CAN BE OVERRIDDEN
	protected void actionCreate() {
		if( !saveChanges(true) )
		return;
		
		finalizeCreation();
		
		this.host.closeModalWindow(this);
	}
	
		// Called upon clicking "Save"
		// CAN BE OVERRIDDEN
	protected void actionSave() {
		if( !saveChanges(true) )
		return;
		
		this.host.closeModalWindow(this);
	}
	
		// Called upon clicking "Cancel"
		// CAN BE OVERRIDDEN
	protected void actionCancel() {
		this.host.closeModalWindow(this);
	}
	
		// Updates the Project and the Asset Pane to the creation
		// of a new Asset
	protected void finalizeCreation() {
		Application.controller.addNewAsset(this.source);
	}
	
	
		// Returns a reference to the asset being operated on
	public A getAsset() {
		return this.source;
	}
	
		// Sets the asset being operated on
	public void setAsset(A source) {
		this.source = source;
	}
	
		// Sets whether the Asset is being edited
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
}
