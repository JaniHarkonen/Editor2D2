package editor2d2.gui.modal.views;

import javax.swing.JButton;
import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.gui.components.CTextField;
import editor2d2.model.project.assets.Asset;

public abstract class ModalView<A extends Asset> extends GUIComponent {
	
		// Source asset that the settings will be stored / read from
	protected A asset;

		// Creates a JPanel of a default modal view that wraps a given
		// JPanel
	protected JPanel createDefaultModalView(JPanel wrappedElement) {
		JPanel container = GUIUtilities.createDefaultPanel();
		
			// Name field
		CTextField txtName = new CTextField("Name:");
		txtName.setText("nameless"/*asset.getName()*/);
		
			// Identifier field
		CTextField txtIdentifier = new CTextField("Identifier:");
		txtIdentifier.setText("identityless"/*asset.getIdentifier()*/);
		
			// Control area (create, save, cancel)
		JPanel containerControls = new JPanel();
		containerControls.add(new JButton("Create"));
		containerControls.add(new JButton("Cancel"));
		
		container.add(txtName.render());
		container.add(txtIdentifier.render());
		container.add(wrappedElement);
		container.add(containerControls);
		
		return container;
	}
	
	
		// Returns a reference to the asset being operated on
	public A getAsset() {
		return this.asset;
	}
	
		// Sets the asset being operated on
	public void setAsset(A asset) {
		this.asset = asset;
	}
}
