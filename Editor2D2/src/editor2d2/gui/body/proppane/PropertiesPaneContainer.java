package editor2d2.gui.body.proppane;

import javax.swing.JPanel;

import editor2d2.Application;
import editor2d2.gui.GUIComponent;
import editor2d2.model.Handles;
import editor2d2.model.app.Controller;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.FactoryService;
import editor2d2.subservice.Subscriber;
import editor2d2.subservice.Vendor;

public class PropertiesPaneContainer extends GUIComponent implements Subscriber {
	
	private Placeable selectedPlaceable;
	

	public PropertiesPaneContainer() {
		Vendor v = Application.controller.subscriptionService.get(Handles.SELECTED_PLACEABLE, "PropertiesPaneContainer", this);
		
		if( v != null )
		this.selectedPlaceable = ((Controller) v).placeableSelectionManager.getSelectedItem();
		else
		this.selectedPlaceable = null;
	}

	@Override
	protected JPanel draw() {
		Placeable p = this.selectedPlaceable;
		
		if( p != null )
		{
			PropertiesPane pp = FactoryService.getFactories(p.getAsset().getAssetClassName()).createPropertiesPane(p);
			
			if( pp != null )
			return pp.render();
		}
		
		return new JPanel();
	}

	@Override
	public void onNotification(String handle, Vendor vendor) {
		if( Handles.handleEquals(handle, Handles.SELECTED_PLACEABLE) )
		{
			this.selectedPlaceable = ((Controller) vendor).placeableSelectionManager.getSelectedItem();
			update();
		}
	}
}
