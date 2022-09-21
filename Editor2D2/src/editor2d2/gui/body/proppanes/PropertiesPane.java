package editor2d2.gui.body.proppanes;

import editor2d2.gui.GUIComponent;
import editor2d2.model.project.assets.Asset;
import editor2d2.model.project.assets.Data;
import editor2d2.model.project.assets.EObject;
import editor2d2.model.project.assets.Image;

public abstract class PropertiesPane extends GUIComponent {
	
		// Source Asset that the properties pane is representing
	protected Asset source;
	
	
	public static PropertiesPane createPropertiesPane(Asset source) {
		if( source instanceof Image )
		return new TilePropertiesPane(source);
		
		if( source instanceof EObject )
		return new InstancePropertiesPane(source);
		
		if( source instanceof Data )
		return new DataCellPropertiesPane(source);
		
		return null;
	}
}
