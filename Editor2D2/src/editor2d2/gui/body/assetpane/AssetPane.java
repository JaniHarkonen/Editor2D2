package editor2d2.gui.body.assetpane;

import javax.swing.JLabel;
import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.subservice.Handle;
import editor2d2.subservice.Vendor;

public class AssetPane extends GUIComponent implements Vendor {
	
		// Reference to the project the asset pane is representing
	private Project source;
	
	
	public AssetPane(Project source) {
		this.source = source;
		
		Application.subscriptionService.register(Handle.ASSET_PANE, this);
	}
	

	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		container.add(new JLabel("Assets"));
		container.add(new JLabel("======================================================="));
		
		for( Asset a : this.source.getAllAssets() )
		container.add((new AssetItem(a)).render());
		
		return container;
	}
}
